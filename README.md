<h3>Async file uploading using the Servlet 3.1 API non-blocking I/O<h3>
<p>This is a sample project on how to asynchronously upload and download files using the Servlet 3.1 API and the new non-blocking I/O support.</p>
<p>The files can be of any size and many can be uploaded at the same time without blocking the application.</p>
<p>The timeout and buffer size has to be adjusted according to the application's file needs.</p>
<p>The application server used for running this sample has to be adjusted as well for NIO worker thread pool size and timeout, and the request post max size.</p>
<p>This is just a sample, some error cases have to be taken into account to be used in a production application, like correctly catching each possible exception.</p>
<p>This sample uses the ExtJS 4.2.1 javascript framework and the upload widget from Ivan Novakov for the async simultaneous uploads GUI:<br/>
<a href="http://www.sencha.com">http://www.sencha.com</a><br/>
<a href="https://github.com/ivan-novakov/extjs-upload-widget">https://github.com/ivan-novakov/extjs-upload-widget</a><br/>
</p>
</p>For adjusting the application's file upload timeout and stream buffer size you need to modify the following files:</p>
<h3>In Web Pages:</h3>
<dl>
	<li>/app.js
		<ul>
			<li><strong>line 18:</strong> set uploadTimeout in milliseconds, default is 1 hour</li>
		</ul>
	</li>
</dl>
<h3>In Java source code:</h3>
<dl>
	<li>mx.jimi.asyncfileupload.FileHandlerServlet
		<ul>
			<li><strong>line 24:</strong> set the timeout in milliseconds, default is 1 hour</li>
		</ul>
	</li>
	<li>mx.jimi.asyncfileupload.FileUploadListener
		<ul>
			<li><strong>line 36:</strong> set the upload buffer in bytes, default is 1MB</li>
		</ul>
	</li>
	<li>mx.jimi.asyncfileupload.FileDownloadListener
		<ul>
			<li><strong>line 32:</strong> set the download buffer in bytes, default is 1MB</li>
		</ul>
	</li>
</dl>
<h3>For uploading files in the application:</h3>
<ol type="1">
	<li>You have to use the web GUI, the default URL is http://localhost<i>:PORT</i>/upload</li>
	<li>Set the server's upload path in the window toolbar's text field, <i>i.e. C:\uploads</i></li>
	<li>Click on the browse button and select as many files as you need, you can do this many times and the files are queued in the window grid.</li>
	<li>When you're done, click on the upload button.</li>
	<li>You can also abort uploads, remove certain files from the grid by selecting them using the checkbox column or you can remove all form the list.</li>
</ol>
<h3>For downloading files in the application:</h3>
<dl>
	<li>This is done using a GET request, all you have to do is write the download URL on your browser's address bar like this:
		<ul>
			<li>http://localhost:<i>PORT</i>/upload/file?path=<i>FILE_SYSTEM_PATH</i>
				<ul style="list-style-type:square">
					<li>i.e. http://localhost:8080/upload/file?path=C:\uploads\some-file.txt</li>
				</ul>
			</li>
		<ul>
	</li>
<dl>
