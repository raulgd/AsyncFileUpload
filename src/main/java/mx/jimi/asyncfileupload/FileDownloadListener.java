package mx.jimi.asyncfileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 * This is the Async File Download WriteListener
 *
 * @author Raul Guerrero
 */
public class FileDownloadListener implements WriteListener
{

	//servlet request context properties
	private final AsyncContext context;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ServletOutputStream out;
	private final File file;

	//read stream properties, uses a 1MB buffer
	private final byte[] buffer = new byte[1024 * 1024];
	private final InputStream in;

	/**
	 * The File Download Listener Constructor
	 *
	 * @param context the servlet async context
	 * @param request the servlet request
	 * @param response the servlet response
	 * @param fileNameWithPath the full filename with path to download
	 * @throws IOException
	 */
	public FileDownloadListener(AsyncContext context, HttpServletRequest request, HttpServletResponse response,
															String fileNameWithPath) throws IOException
	{
		this.context = context;
		this.request = request;
		this.response = response;
		this.out = response.getOutputStream();
		this.file = new File(fileNameWithPath);
		this.in = new FileInputStream(file);

		//set the file response headers to return the correct file name and size
		response.addHeader("Content-Disposition", "attachment; filename=\"" + FileUtils.getFileName(fileNameWithPath) + "\"");
		response.addHeader("Content-Length", String.valueOf(this.file.length()));
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	}

	/**
	 * Write out the file content to the user
	 *
	 * @throws IOException
	 */
	@Override
	public void onWritePossible() throws IOException
	{
		//wait for the output to be ready before input
		while (out.isReady())
		{
			int length = in.read(buffer);

			if (length == -1)
			{
				//finished writing out the file
				in.close();

				context.complete();
				break;
			}
			else
			{
				//do write in the end to avoid exceptions, as the container takes ownership of the buffer
				//if we do any buffer operation after write() and before the next isReady(), it throws a container exception
				out.write(buffer, 0, length);
			}
		}
	}

	/**
	 * If there was a connection error, or the request was canceled close the input stream
	 *
	 * @param t the exception message that caused the problem
	 */
	@Override
	public void onError(Throwable t)
	{
		try
		{
			in.close();
		}
		catch (IOException ex)
		{
			Logger.getLogger(FileUploadListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
