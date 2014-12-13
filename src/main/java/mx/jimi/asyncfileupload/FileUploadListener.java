package mx.jimi.asyncfileupload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 * The read listener used to buffer file upload requests and handle a file upload of any size
 *
 * @author Raul Guerrero
 */
public class FileUploadListener implements ReadListener
{

	//servlet request context properties
	private final AsyncContext context;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ServletInputStream in;
	private final String path;
	private final String fileName;

	//read stream properties, uses a 1MB buffer
	private final byte[] buffer = new byte[1024 * 1024];
	private final File tempFile;
	private final FileOutputStream tempFileOutputStream;

	public FileUploadListener(AsyncContext context, HttpServletRequest request, HttpServletResponse response,
														String path, String fileName) throws FileNotFoundException, IOException
	{
		this.context = context;
		this.request = request;
		this.response = response;
		this.in = request.getInputStream();
		this.path = path;
		this.fileName = fileName;
		this.tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
		this.tempFileOutputStream = new FileOutputStream(this.tempFile, true);
	}

	/**
	 * Writes all the received data to the temp file
	 *
	 * @throws IOException
	 */
	@Override
	public void onDataAvailable() throws IOException
	{
		while (in.isReady())
		{
			int length = in.read(buffer);
			tempFileOutputStream.write(buffer, 0, length);
		}
	}

	/**
	 * When the request is done, moves the temp file to the specified path and responds a "success" JSON
	 *
	 * @throws IOException
	 */
	@Override
	public void onAllDataRead() throws IOException
	{
		tempFileOutputStream.close();

		//move the temp file to the specified path
		final File target = new File(path + File.separator + fileName);

		//move the file using another thread in case the file is too big and takes a bit to execute
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Files.move(tempFile.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException ex)
				{
					Logger.getLogger(FileUploadListener.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}).start();

		//do the response
		response.setContentType(MediaType.APPLICATION_JSON);
		response.getWriter().print("{\"success\":true}");

		context.complete();
	}

	/**
	 * If there was a connection error, or the request was canceled, deletes the temp file and throws a 500 server error
	 *
	 * @param t the exception message that caused the problem
	 */
	@Override
	public void onError(Throwable t)
	{
		try
		{
			tempFileOutputStream.close();
			Files.delete(tempFile.toPath());

			//send a 500 error code with the exception message
			response.sendError(500, t.getMessage());

			Logger.getLogger(FileUploadListener.class.getName()).log(Level.SEVERE, null, t);
		}
		catch (IOException ex)
		{
			Logger.getLogger(FileUploadListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
