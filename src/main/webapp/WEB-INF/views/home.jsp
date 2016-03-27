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
<c:set var="folder-empty" value="${'/resources/img/folder-empty.png'}"/>
<c:set var="folder-filled" value="${'/resources/img/folder-filled.png'}"/>
<header>
    <div id="sync-overlay" class="sync-overlay">
        <div id="sync-loading" class="sync-container">
            <h2>Syncing your changes to your Dropbox account</h2>
            <img src="${"/resources/img/balls.svg"}" height="300" width="300">
            <h3>This would probably take a while. <br> Have a short break first.</h3>
        </div>
        <div id="sync-finished" class="sync-container">
            <h2>Finished Syncing your files</h2>
            <i class="fa fa-check-circle-o fa10"></i>
            <h3>You're ready to go!</h3>
            <button id="finish-sync" class="btn btn-primary">Got it!</button>
        </div>
        <div id="sync-back-paint" class="sync-back-paint"></div>
    </div>
    <nav class="navbar dd-home-navbar">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-2" aria-expanded="false"><span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand dd-brand" href="/home"><strong>docudile</strong></a></div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right dd-nav-links">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <li>
                            <button class="btn navbar-btn" data-toggle="modal" data-target="#uploadModal"><i
                                    class="fa fa-upload"></i> Upload
                            </button>
                        </li>
                        <li role="presentation" class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-haspopup="true" aria-expanded="false"><i class="fa fa-user"></i>
                                <small> ${user.firstname} ${user.lastname}</small>
                            </a>
                            <ul class="dropdown-menu">
                                <li class="dropdown-header">Menu</li>
                                <li>
                                    <a href="#" id="sync"><i class="fa fa-refresh"></i> Sync</a>
                                </li>
                                <li>
                                    <a href="/${spring_security_logout}"><i class="fa fa-sign-out"></i> Logout</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</header>
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="uploadModalTitle">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="uploadModalTitle">Upload Document(s)</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <form action="/home/file?_csrf=${_csrf.token}" id="upload_doc" class="dropzone dropzone-blue">
                        <div class="fallback">
                            <input name="document">
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer"></div>
        </div>
    </div>
</div>
<main class="container-fluid">
    <div class="row">
        <div class="col-lg-2 dd-navtree">
            <div class="navtree-header">
                <h4>Navigation</h4>
            </div>
            <div id="treeview"></div>
        </div>
        <div class="col-lg-4 dd-filebox" id="filebox">
            <div class="filebox-header">
                <h4 id="filebox-header-text">Lol</h4>
            </div>
            <table class="table table-hover" id="dd-filebox-table">
                <tbody id="dd-filebox-id">
                </tbody>
            </table>
        </div>
        <div id="fileInfo" class="col-lg-6 file-info"></div>
    </div>
</main>
<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/dropzone.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
<script rel="script" src="${"/resources/treeview/bootstrap-treeview.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap-treenav.js"}"></script>
<script rel="script" src="${"/resources/js/custom.js"}"></script>
</body>
</html>