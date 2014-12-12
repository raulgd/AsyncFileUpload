package mx.jimi.asyncfileupload;

import java.io.UnsupportedEncodingException;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author jz2n4h
 */
public class FileUtils
{

	/**
	 * Gets a file name from a path removing all invalid characters
	 *
	 * @param path the full file path with the file name
	 * @return the file name
	 * @throws UnsupportedEncodingException in case UTF-8 is not supported in the system
	 */
	public static String getFileName(String path) throws UnsupportedEncodingException
	{
		String filename = path;

		byte charsBytes[] = filename.getBytes();

		filename = new String(charsBytes, "UTF-8");
		filename = filename.replace("\\", "/");

		String pathtokens[] = filename.split("/");
		filename = pathtokens[pathtokens.length - 1];

		return replaceInvalidCharacters(filename);
	}

	/**
	 * Gets the path from an absolute path file name
	 *
	 * @param fullFileName the absolute path file name
	 * @return the path
	 * @throws UnsupportedEncodingException in case UTF-8 is not supported in the system
	 */
	public static String getPath(String fullFileName) throws UnsupportedEncodingException
	{
		String path = fullFileName;

		byte charsBytes[] = path.getBytes();

		path = new String(charsBytes, "UTF-8");
		path = path.replace("\\", "/");

		String pathtokens[] = path.split("/");
		path = pathtokens[pathtokens.length - 1];

		for (int i = 0; i < pathtokens.length; i++)
		{
			if (i == pathtokens.length - 1)
			{
				break;
			}

			if (i == 0)
			{
				path = pathtokens[i];
			}

			path += pathtokens[i];
		}

		return replaceInvalidCharacters(path);
	}

	/**
	 * Replaces some invalid characters with an underscore "_"
	 *
	 * @param string The string to replace on
	 * @return The string with the invalid characters replaced
	 */
	public static String replaceInvalidCharacters(String string)
	{
		return string.replace('\"', '_').replace('\'', '_').replace('\\', '_').replace('?', '_')
						.replace("/", "_").replace("<", "_").replace(">", "_").replace(":", "_")
						.replace("*", "_").replace("|", "_");
	}

	/**
	 * Gets the filename from the headers of the multipart form that contains the file that is being uploaded
	 *
	 * @param headers the Multimap value that contains the headers
	 * @return
	 */
	private static String getFileNameFromHeaders(MultivaluedMap<String, String> headers)
	{
		String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition)
		{
			if ((filename.trim().startsWith("filename")))
			{

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}

		return "";
	}
}
