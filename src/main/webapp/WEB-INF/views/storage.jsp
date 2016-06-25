<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Docudile</title>

    <link rel="stylesheet" href="${"/resources/css/bootstrap.min.css"}">
    <link rel="stylesheet" href="${"/resources/bootflat/css/bootflat.css"}">
    <link rel="stylesheet" href="${"/resources/treeview/bootstrap-treeview.min.css"}">
    <link rel="stylesheet" href="${"/resources/css/bootstrap-treenav.css"}">
    <link rel="stylesheet" href="${"/resources/fonts/font-awesome/css/font-awesome.min.css"}">
    <link rel="stylesheet" href="${"/resources/css/dropzone.css"}">
    <link rel="stylesheet" href="${"/resources/css/site.css"}">

    <link rel="icon"
          type="image/png"
          href="${"/resources/img/logo.png"}">
</head>
<body>
<main>
    <div class="row">
        <jsp:include page="/WEB-INF/extras/side.nav.jsp"></jsp:include>
        <div class="col-sm-4 dd-filebox" id="filebox-container">
            <div id="filebox-template">
                <div class="filebox-header">
                    <h4 id="filebox-header-text"></h4>
                    <span class="flex"></span>
                    <a href="#" id="filebox-header-moveup" disabled="disabled"><h4><i class="fa fa-arrow-up" aria-hidden="true"></i> Move up</h4></a>
                </div>
                <table class="table file-explorer-table table-hover" id="dd-filebox-table">
                    <tbody>
                    <tr class="clickable-row" id="file-row-template">
                        <td>
                            <img class="dd-row-icon pull-left" file-icon>
                            <span class="name" file-name></span>
                            <small><i class="fa fa-calendar-o" aria-hidden="true"></i> <span file-date></span></small>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div id="file-preview-container" class="col-sm-6 file-info">
            <div id="file-preview-template">
                <div class="file-info-header">
                    <h4 file-name></h4>
                    <span class="flex"></span>
                    <a file-download><h4><i class="fa fa-arrow-down" aria-hidden="true"></i> Download</h4></a>
                    <a file-delete><h4><i class="fa fa-trash-o" aria-hidden="true"></i> Delete</h4></a>
                </div>
                <div class="preview">
                    <img file-img>
                </div>
            </div>
        </div>
    </div>
</main>
<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/jquery.storageapi.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
<script rel="script" src="${"/resources/treeview/bootstrap-treeview.min.js"}"></script>
<script rel="script" src="${"/resources/js/storage.js"}"></script>
<script rel="script" src="${"/resources/js/sync.js"}"></script>
</body>
</html>