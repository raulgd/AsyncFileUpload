package mx.jimi.asyncfileupload;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Adds the context attribute "appRoot" for the pages to call the web application root using only the ${appRoot} tag
 *
 * @author Raul Guerrero
 */
@WebListener()
public class AppRootContextListener implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		try
		{
			//set the appRoot variable to use it in JSP pages
			sce.getServletContext().setAttribute("appRoot", sce.getServletContext().getContextPath());
		}
		catch (UnsupportedOperationException ex)
		{
			Logger.getLogger(AppRootContextListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		try
		{
			sce.getServletContext().removeAttribute("appRoot");
		}
		catch (UnsupportedOperationException ex)
		{
			Logger.getLogger(AppRootContextListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
