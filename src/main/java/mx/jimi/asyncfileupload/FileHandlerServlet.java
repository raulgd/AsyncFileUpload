package mx.jimi.asyncfileupload;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raul Guerrero
 */
@WebServlet(urlPatterns =
{
	"/file"
}, asyncSupported = true)
public class FileHandlerServlet extends HttpServlet
{

	/**
	 * Handles file downloads
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException
	{
		AsyncContext context = request.startAsync();
		//set the async task timeout to 1 hour
		context.setTimeout(3600000);
		String path = request.getParameter("path");
		response.getOutputStream().setWriteListener(new FileDownloadListener(context, request, response, path));
	}

	/**
	 * Handles file uploads
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException
	{
		AsyncContext context = request.startAsync();
		//set the async task timeout to 1 hour
		context.setTimeout(3600000);
		String path = request.getParameter("path");
		String fileName = request.getHeader("X-File-Name");
		request.getInputStream().setReadListener(new FileUploadListener(context, request, response, path, fileName));
	}
}
