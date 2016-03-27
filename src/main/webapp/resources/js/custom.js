/**
 * Created by franc on 2/9/2016.
 */
window.treeData = null
window.tree = null;
window.selectedNode = null;
$(document).on('ready', function () {
    Dropzone.autoDiscover = false;
    initTree();
    $(document).keyup(function (e) {
        if (e.keyCode == 27) {
            removeDetails();
        }
    });
    $('#upload_doc').dropzone({
        paramName: 'document',
        clickable: true,
        parallelUploads: 1,
        init: function() {
            this.on('queuecomplete', function() {
                this.removeAllFiles(true);
                initTree();
            });
        }
    });
    $('#sync').click(function () {
        $.ajax({
            type: 'GET',
            url: '/home/sync',
            beforeSend: function () {
                $("#sync-overlay").css("display", "block");
                $("#sync-back-paint").css("opacity", "0.9");
                $("#sync-loading").slideDown(300);
            },
            complete: function () {
                $("#sync-loading").slideUp(200);
                $("#sync-finished").slideDown(300, function () {
                    $("#finish-sync").click(function () {
                        $("#sync-overlay").fadeOut(400, function() {
                            $("#sync-finished").css("display", "none");
                            $("#sync-overlay").css("display", "none");
                        });
                    });
                });
            }
        });
    });
});

function initTree() {
    window.treeData = $.retrieveTreeData();
    window.tree = createTreeView();
    var currSelectedNode = window.treeData[0] || window.selectedNode;
    revealNode(currSelectedNode.text, currSelectedNode.id);
}

$.extend({
    retrieveTreeData: function () {
        var response = null;
        $.ajax({
            url: "/home/folder", async: false, type: 'get', success: function (data) {
                response = convertToTreeData(data);
            }
        });
        return response;
    }
});

function convertToTreeData(data) {
    var obj = [];
    for (var i in data) {
        if (data[i] != null && typeof(data[i]) == "object") {
            var temp = {
                id: data[i].id,
                text: data[i].name
            }
            if (data[i].childFolders != null && data[i].childFolders.length > 0) {
                temp.nodes = convertToTreeData(data[i].childFolders);
            }
            obj.push(temp);
        }
    }
    return obj;
}

function createTreeView() {
    return $("#treeview").treeview({
        collapseIcon: "glyphicon glyphicon-folder-open",
        expandIcon: "glyphicon glyphicon-folder-close",
        emptyIcon: "glyphicon glyphicon-folder-close",
        color: "#fff",
        backColor: "#2196f3",
        selectedBackColor: "#35a2f9",
        data: window.treeData,
        levels: 1,
        onNodeSelected: function (event, node) {
            window.selectedNode = node;
            updateFilebox(node.id);
        }
    });
}

function revealNode(nodeName, parentNodeName) {
    var result = findNode(nodeName, parentNodeName);
    window.tree.treeview('revealNode', [result, {silent: true}]);
    window.tree.treeview('selectNode', [result, {silent: false}]);
    window.tree.treeview('expandNode', [result, {silent: true}]);
    window.tree.treeview('clearSearch');
}

function findNode(nodeName, nodeId) {
    var results = window.tree.treeview('search', [nodeName, {
        ignoreCase: false,
        exactMatch: true,
        revealResults: false,
        silent: true
    }]);
    for (var i in results) {
        if (results[i].id === nodeId) {
            return results[i];
        }
    }
}

function viewDetailsFile(data) {
    var header = '<div id="file-info-header" class="file-info-header">' +
        '<h4>' + data.filename + '</h4>' +
        '<span class="flex"></span>' +
        '<a href="/home/file/' + data.id + '" download="' + data.filename + '"><h4>Download</h4></a>' +
        '<a href="#"><h4>Delete</h4></a>' +
        '</div>';
    var preview = '<div id="preview" class="preview">' +
        '<img src="/home/file/' + data.id + '">' +
        '</div>';
    var clone = $(preview).clone(false).hide();
    var fileInfo = $('#fileInfo');
    $.when(removeDetails()).done(function() {
        fileInfo.append(header);
        fileInfo.append(clone);
        clone.fadeIn(300);
    });
}

function removeDetails() {
    var fileInfo = $("#fileInfo");
    var promise = fileInfo.children().fadeOut(400).promise();
    $.when(promise).done(function() {
        fileInfo.empty();
    });
    return promise;
}

function updateFilebox(id) {
    $.ajax({
        dataType: "json",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'GET',
        url: '/home/folder/' + id,
        success: function (response) {
            $('#dd-filebox-id').empty();
            $("#filebox-header-text").text(response.name);
            $.each(response.childFolders, function (key, inside) {
                var icon = '/resources/img/folder-filled.png';
                if (inside.childFolders.length == 0 && inside.files.length == 0) {
                    icon = '/resources/img/folder-empty.png';
                }
                var tablerow = $('<tr class="dd-file-row clickable-row">' +
                    '<td ><img src="' + icon + '" class="dd-row-icon" /> ' + inside.name + "</td>" +
                    '<td class="dd-row-details">' + inside.dateModified + '</td>');
                $('#dd-filebox-id').append(tablerow);
                tablerow.click(function () {
                    $(this).addClass('active').siblings().removeClass('active');
                });
                tablerow.dblclick(function () {
                    updateFilebox(inside.id);
                    revealNode(inside.name, inside.id);
                });
            });
            $.each(response.files, function (key, inside) {
                var tablerow = $('<tr id="file-' + inside.id + '" class="dd-file-row clickable-row">' +
                    '<td><img src="/resources/img/file-icon.png" class="dd-row-icon" /> ' + inside.filename + "</td>" +
                    '<td class="dd-row-details">' + inside.dateUploaded + '</td>');
                $('#dd-filebox-id').append(tablerow);
                tablerow.click(function () {
                    $(this).addClass('active').siblings().removeClass('active');
                    viewDetailsFile(inside);
                });
            });
        }
    });
}