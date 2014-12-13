## Async file uploading using the Servlet 3.1 API non-blocking I/O

This is a sample project on how to asynchronously upload and download files using the Servlet 3.1 API and the new non-blocking I/O support.

The files can be of any size and many can be uploaded at the same time without blocking the application.

The timeout and buffer size has to be adjusted according to the application's file needs.

The application server used for running this sample has to be adjusted as well for NIO worker thread pool size and timeout, and the request post max size.

This is just a sample, some error cases have to be taken into account to be used in a production application, like correctly catching each possible exception.

This sample uses the ExtJS 4.2.1 javascript framework and the upload widget from Ivan Novakov for the async simultaneous uploads GUI:

- http://www.sencha.com
- https://github.com/ivan-novakov/extjs-upload-widget


For adjusting the application's file upload timeout and stream buffer size you need to modify the following files:

## In Web Pages:
- /app.js
line 18: set uploadTimeout in milliseconds, default is 1 hour

## In Java source code:
- mx.jimi.asyncfileupload.FileHandlerServlet
line 24: set the timeout in milliseconds, default is 1 hour

- mx.jimi.asyncfileupload.FileUploadListener
line 36: set the upload buffer in bytes, default is 1MB

- mx.jimi.asyncfileupload.FileDownloadListener
line 32: set the download buffer in bytes, default is 1MB


## For uploading files in the application:
1. You have to use the web GUI, the default URL is http://localhost:PORT/upload where PORT is whatever you use on your application server's HTTP port
2. Set the server's upload path in the window toolbar's text field, i.e. C:\uploads
3. Click on the browse button and select as many files as you need, you can do this many times and the files are queued in the window grid.
4. When you're done, click on the upload button.
5. You can also abort uploads, remove certain files from the grid by selecting them using the checkbox column or you can remove all form the list.


## For downloading files in the application:
- This is done using a GET request, all you have to do is write the download URL on your browser's address bar like this:
- http://localhost:PORT/upload/file?path=FILE_SYSTEM_PATH
- i.e. http://localhost:8080/upload/file?path=C:\uploads\some-file.txt