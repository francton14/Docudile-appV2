/**
 * Created by franc on 2/9/2016.
 */
window.fileBoxTemplate = null;
window.fileRowTemplate = null;
window.filePreviewTemplate = null;
$(document).on('ready', function () {
    prepareView();
    displayStorage();
    bindEsc();
});

function prepareView() {
    var extractedFileBoxTemplate = $('#filebox-template');
    extractedFileBoxTemplate.removeAttr('id');
    window.fileBoxTemplate = extractedFileBoxTemplate.html();
    console.log(fileBoxTemplate)
    extractedFileBoxTemplate.remove();
    var extractedFileTemplate = $('#file-row-template', extractedFileBoxTemplate);
    extractedFileTemplate.removeAttr('id');
    window.fileRowTemplate = extractedFileTemplate.parent().html();
    extractedFileTemplate.remove();
    var extractedPreviewTemplate = $('#file-preview-template');
    extractedPreviewTemplate.removeAttr('id');
    window.filePreviewTemplate = extractedPreviewTemplate.parent().html();
    extractedPreviewTemplate.remove();
}

function displayStorage() {
    $.when(retrieveStorage()).then(function (data) {
        if (data.length > 0) {
            $('#filebox-container').append(window.fileBoxTemplate);
            var treeData = convertToTreeData(data);
            createTreeView(treeData);
            revealNode(treeData[0].text, treeData[0].id);
        } else {

        }
    })
}

function retrieveStorage() {
    return $.ajax({
        method: 'GET',
        url: '/folder'
    });
}

function convertToTreeData(data) {
    var obj = [];
    $.each(data, function (index, value) {
        if (value != null && typeof(value) == 'object') {
            var temp = {
                id: value.id,
                text: value.name,
                files: value.files,
                date: value.dateModified,
                nodes: {}
            };
            if (value.childFolders != null && value.childFolders.length > 0) {
                temp.nodes = convertToTreeData(value.childFolders);
            }
            obj.push(temp);
        }
    });
    return obj;
}

function createTreeView(data) {
    $('#treeview').treeview({
        collapseIcon: 'glyphicon glyphicon-folder-open',
        expandIcon: 'glyphicon glyphicon-folder-close',
        emptyIcon: 'glyphicon glyphicon-folder-close',
        color: '#fff',
        backColor: '#2e3942',
        selectedBackColor: '#465764',
        data: data,
        levels: 1,
        onNodeSelected: function (event, node) {
            updateFilebox(node);
        }
    });
}

function revealNodeById(id) {
    var treeview = $('#treeview');
    treeview.treeview('revealNode', [id, {silent: true}]);
    treeview.treeview('selectNode', [id, {silent: false}]);
    treeview.treeview('expandNode', [id, {silent: true}]);
    treeview.treeview('clearSearch');
}

function revealNode(nodeName, nodeId) {
    var result = findNode(nodeName, nodeId);
    var treeview = $('#treeview');
    treeview.treeview('revealNode', [result, {silent: true}]);
    treeview.treeview('selectNode', [result, {silent: false}]);
    treeview.treeview('expandNode', [result, {silent: true}]);
    treeview.treeview('clearSearch');
}

function findNode(nodeName, nodeId) {
    var results = $('#treeview').treeview('search', [nodeName, {
        ignoreCase: false,
        exactMatch: true,
        revealResults: false,
        silent: true
    }]);
    var matchingResult;
    $.each(results, function (index, value) {
        if (value.id === nodeId) {
            matchingResult = value;
        }
    });
    return matchingResult;
}

function updateFilebox(node) {
    $('#filebox-header-text').text(node.text);
    var moveup = $('#filebox-header-moveup');
    moveup.hide();
    if (typeof(node.parentId) === "number")    {
        moveup.click(function () {
            revealNodeById(node.parentId)
        });
        moveup.show();
    }
    var body = $('#dd-filebox-table > tbody');
    body.empty();
    $.each(node.nodes, function (index, value) {
        var parsedTemplate = $(window.fileRowTemplate);
        if (value.nodes.length == 0 && value.files.length == 0) {
            $('[file-icon]', parsedTemplate).attr('src', '/resources/img/folder-empty.png');
        } else {
            $('[file-icon]', parsedTemplate).attr('src', '/resources/img/folder-filled.png');
        }
        $('[file-name]', parsedTemplate).text(value.text);
        $('[file-date]', parsedTemplate).text(value.date);
        parsedTemplate.click(function () {
            $(this).addClass('active').siblings().removeClass('active');
        });
        parsedTemplate.dblclick(function () {
            updateFilebox(value);
            revealNode(value.text, value.id);
        });
        body.append(parsedTemplate);
    });
    $.each(node.files, function (index, value) {
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

function viewDetailsFile(node) {
    var body = $('#file-preview-container');
    var parsedTemplate = $(window.filePreviewTemplate);
    parsedTemplate.attr('id', 'file-preview');
    $('[file-name]', parsedTemplate).text(node.filename);
    $('[file-download]', parsedTemplate).attr('href', '/document/' + node.id);
    $('[file-download]', parsedTemplate).attr('download', node.filename);
    $('[file-img]', parsedTemplate).attr('src', '/document/' + node.id);
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