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
        <jsp:include page="/WEB-INF/extras/profile.nav.jsp"></jsp:include>
        <div class="col-lg-7 profile-body">
            <div class="user-info">
                <p class="header"><i class="fa fa-cog" aria-hidden="true"></i> Setup your Dropbox</p>
                <div class="detail">
                    <p>You will be redirected to the Dropbox website and will be asked to give permission to our app. After that you'll be redirected back here.</p>
                    <small><i class="fa fa-info-circle" aria-hidden="true"></i> Your documents will be synced in your Dropbox account in a timely manner.</small>
                    <a href="/profile/dropbox/auth/start" type="button" class="btn"><i class="fa fa-hand-o-right" aria-hidden="true"></i> Proceed to Dropbox</a>
                </div>
            </div>
        </div>
    </div>
</main>
<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/jquery.storageapi.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
<script rel="script" src="${"/resources/js/sync.js"}"></script>
</body>
</html>