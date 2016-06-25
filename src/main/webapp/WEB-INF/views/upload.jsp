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
        <div class="col-lg-4 dd-filebox" id="filebox">
            <div class="filebox-header">
                <h4>Uploads</h4>
            </div>
            <form action="/document?_csrf=${_csrf.token}" id="upload-doc" class="dropzone">
                <div class="fallback">
                    <input name="document">
                </div>
            </form>
            <div id="upload-previews" class="upload-previews">
                <div id="upload-preview" class="upload-preview">
                    <img src="${"/resources/img/file-icon.png"}" class="dd-row-icon pull-left">
                    <p data-dz-name></p>
                    <small id="upload-status-label">Status:</small>
                    <small id="upload-status"></small>
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                             aria-valuemax="100" style="width: 0%;" data-dz-uploadprogress>
                        </div>
                    </div>
                    <small class="upload-remove-link" data-dz-remove><i class="fa fa-times"
                                                                                   aria-hidden="true"></i> Remove
                    </small>
                </div>
            </div>
            <div class="upload-controls">
                    <button type="button" id="commence-upload" class="btn"><i class="fa fa-upload" aria-hidden="true"></i> Upload</button>
                    <button type="button" id="clear-upload-queue" class="btn"><i class="fa fa-ban" aria-hidden="true"></i> Clear All</button>
            </div>
        </div>
        <div id="fileInfo" class="col-lg-6 file-info">
        </div>
    </div>
</main>
<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/jquery.storageapi.min.js"}"></script>
<script rel="script" src="${"/resources/js/dropzone.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
<script rel="script" src="${"/resources/js/upload.js"}"></script>
<script rel="script" src="${"/resources/js/sync.js"}"></script>
</body>
</html>