
Ext.Loader.setConfig({
	enabled: true,
	paths: {
		'Ext.ux': appRoot + '/extjs/ux/',
		'Ext.ux.upload': appRoot + '/3rdparty/upload'
	}
});

Ext.onReady(function () {
	uploadwindow = Ext.create('Ext.ux.upload.Dialog',
					{
						id: 'uploadwindow',
						dialogTitle: 'Asynchronous Upload File(s)',
						width: 600,
						height: 250,
						synchronous: false,
						uploadTimeout: 3600000,
						uploadUrl: appRoot + '/file?path=' + encodeURIComponent(filepath)
					});

	uploadwindow.show();
});
