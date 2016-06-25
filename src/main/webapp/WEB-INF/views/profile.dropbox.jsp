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
        <div class="col-lg-7">
            <div class="profile-body">
                <div class="user-info">
                    <p class="header"><i class="fa fa-user" aria-hidden="true"></i> Your Dropbox Information</p>
                    <div class="detail">
                        <p>${dropbox.name}</p>
                        <small>Name</small>
                    </div>
                    <div class="detail">
                        <p>${dropbox.email}</p>
                        <small>Email</small>
                    </div>
                    <div class="detail">
                        <p><c:choose>
                            <c:when test="${dropbox.emailVerified}">Yes</c:when>
                            <c:otherwise>No</c:otherwise>
                        </c:choose></p>
                        <small>Email Verified</small>
                    </div>
                    <div class="detail">
                        <p>${dropbox.accountType}</p>
                        <small>Account Type</small>
                    </div>
                    <div class="detail">
                        <p><c:choose>
                            <c:when test="${dropbox.paired}">Yes</c:when>
                            <c:otherwise>No</c:otherwise>
                        </c:choose></p>
                        <small>Paired</small>
                    </div>
                    <c:if test="${paired}">
                        <div class="detail">
                            <p>${dropbox.teamName}</p>
                            <small>Team</small>
                        </div>
                    </c:if>
                </div>
                <div class="user-info">
                    <p class="header"><i class="fa fa-hdd-o" aria-hidden="true"></i> Storage Information</p>
                    <div class="detail">
                        <p>${dropbox.usedSpace / 1000000} MB</p>
                        <small>Used Space</small>
                    </div>
                    <div class="detail">
                        <p>${dropbox.totalSpace / 1000000} MB</p>
                        <small>Total Account Storage Space</small>
                    </div>
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