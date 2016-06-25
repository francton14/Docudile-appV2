/**
 * Created by franc on 5/27/2016.
 */
window.fileRowTemplate = null;
window.filePreviewTemplate = null;
$(document).on('ready', function () {
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    });
    prepareView();
    search();
    bindEsc();
});

function prepareView() {
    var extractedFileTemplate = $('#file-row-template');
    extractedFileTemplate.removeAttr('id');
    window.fileRowTemplate = extractedFileTemplate.parent().html();
    extractedFileTemplate.remove();
    var extractedPreviewTemplate = $('#file-preview-template');
    extractedPreviewTemplate.removeAttr('id');
    window.filePreviewTemplate = extractedPreviewTemplate.parent().html();
    extractedPreviewTemplate.remove();
}

function search() {
    var searchForm = $('#search-form');
    searchForm.submit(function (e) {
        e.preventDefault();
        $.when(submitSearch(searchForm)).then(function (data) {
            updateFilebox(data);
        });
    });
}

function submitSearch(form) {
    var data = form.serializeJSON();
    return $.ajax({
        method: 'POST',
        url: '/search',
        headers: {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json'
        },
        data: JSON.stringify(data)
    });
}

function updateFilebox(files) {
    var body = $('#dd-filebox-table > tbody');
    body.empty();
    $.each(files, function (index, value) {
        var parsedTemplate = $(window.fileRowTemplate);
        $('[file-icon]', parsedTemplate).attr('src', '/resources/img/file-icon.png');
        $('[file-name]', parsedTemplate).text(value.filename);
        $('[file-date]', parsedTemplate).text(value.dateUploaded);
        parsedTemplate.click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            viewDetailsFile(value);
        });
        body.append(parsedTemplate);
    });
}

function viewDetailsFile(file) {
    var body = $('#file-preview-container');
    var parsedTemplate = $(window.filePreviewTemplate);
    parsedTemplate.attr('id', 'file-preview');
    $('[file-name]', parsedTemplate).text(file.filename);
    $('[file-download]', parsedTemplate).attr('href', '/document/' + file.id);
    $('[file-download]', parsedTemplate).attr('download', file.filename);
    $('[file-img]', parsedTemplate).attr('src', '/document/' + file.id);
    $.when(removeDetails()).then(function () {
        body.append(parsedTemplate.children());
    });
}

function bindEsc() {
    $(document).keyup(function (e) {
        if (e.keyCode == 27) {
            removeDetails();
        }
    });
}

function removeDetails() {
    var fileInfo = $("#file-preview-container");
    var promise = fileInfo.children().fadeOut(200).promise();
    $.when(promise).done(function () {
        fileInfo.empty();
    });
    return promise;
}