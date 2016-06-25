/**
 * Created by franc on 5/13/2016.
 */
$(document).on('ready', function () {
    var previewNode = document.querySelector("#upload-preview");
    previewNode.id = "";
    var previewTemplate = previewNode.parentNode.innerHTML;
    previewNode.parentNode.removeChild(previewNode);
    Dropzone.autoDiscover = false;
    $('#upload-doc').dropzone({
        paramName: 'document',
        clickable: true,
        parallelUploads: 1,
        previewTemplate: previewTemplate,
        previewsContainer: '#upload-previews',
        autoProcessQueue: false,
        init: function () {
            var dropzone = this;
            dropzone.on('addedfile', function (file) {
                console.log('Added: ' + file.name);
                file.previewElement.querySelector('#upload-status').innerText = 'Pending';
            });
            dropzone.on('error', function (file, error) {
                console.log('Error: ' + file.name + " Message: " + error);
                file.previewElement.querySelector('#upload-status').innerText = 'Some problems occurred';
            });
            dropzone.on('processing', function (file) {
                console.log('Processed: ' + file.name);
                file.previewElement.querySelector('#upload-status').innerText = 'Processing';
            });
            dropzone.on('success', function (file, data) {
                console.log('Success: ' + file.name);
                file.previewElement.querySelector('#upload-status-label').innerText = 'Stored in: ';
                file.previewElement.querySelector('#upload-status').innerText = data.message;
            });
            dropzone.on('complete', function () {
                dropzone.processQueue();
            });
            $('#commence-upload').click(function() {
                dropzone.processQueue();
            });
            $('#clear-upload-queue').click(function() {
                dropzone.removeAllFiles(false);
            });
        }
    });
});