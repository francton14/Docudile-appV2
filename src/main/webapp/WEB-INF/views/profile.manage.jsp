<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
<main>
    <div class="row">
        <jsp:include page="/WEB-INF/extras/side.nav.jsp"></jsp:include>
        <jsp:include page="/WEB-INF/extras/profile.nav.jsp"></jsp:include>
        <div class="col-lg-7 profile-container">
            <div class="profile-body">
                <div class="user-info">
                    <p class="header"><i class="fa fa-user-secret" aria-hidden="true"></i> Authentication Credentials</p>
                    <div class="detail">
                        <p>Email Address</p>
                        <small id="show-edit-email"><i class="fa fa-pencil" aria-hidden="true"></i> Change</small>
                        <form id="edit-email">
                            <div class="form-group">
                                <input type="text" class="form-control" name="oldEmail" placeholder="New Email Address"/>
                                <span id="msg-edit-email-oldEmail" class="help-block"></span>
                            </div>
                            <div class="form-group">
                                <input type="text" class="form-control" name="emailObject[email]" placeholder="New Email Address"/>
                                <span id="msg-edit-email-emailObject-Email" class="help-block"></span>
                            </div>
                            <div class="form-group">
                                <input type="text" class="form-control" name="emailObject[confirmEmail]" placeholder="Confirm New Email Address"/>
                                <span id="msg-edit-email-emailObject-confirmEmail" class="help-block"></span>
                            </div>
                            <button type="submit" class="btn btn-primary" id="submit-edit-email"><i class="fa fa-paper-plane" aria-hidden="true"></i> Submit</button>
                            <button type="button" class="btn" id="cancel-edit-email"><i class="fa fa-times" aria-hidden="true"></i> Cancel</button>
                        </form>
                    </div>
                    <div class="detail">
                        <p>Password</p>
                        <small id="show-edit-password"><i class="fa fa-pencil" aria-hidden="true"></i> Change</small>
                        <form id="edit-password">
                            <div class="form-group">
                                <input type="password" class="form-control" name="oldPassword" placeholder="Old Password"/>
                                <span id="msg-edit-password-oldPassword" class="help-block"></span>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" name="passwordObject[password]" placeholder="New Password"/>
                                <span id="msg-edit-password-passwordObject-password" class="help-block"></span>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" name="passwordObject[confirmPassword]" placeholder="Confirm New Password"/>
                                <span id="msg-edit-password-passwordObject-confirmPassword" class="help-block"></span>
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fa fa-paper-plane" aria-hidden="true"></i> Submit</button>
                            <button type="button" class="btn" id="cancel-edit-password"><i class="fa fa-times" aria-hidden="true"></i> Cancel</button>
                        </form>
                    </div>
                </div>
                <div class="user-info">
                    <p class="header"><i class="fa fa-user" aria-hidden="true"></i> Personal Information</p>
                    <div class="detail">
                        <p>First Name</p>
                        <small id="show-edit-firstname"><i class="fa fa-pencil" aria-hidden="true"></i> Change</small>
                        <form id="edit-firstname">
                            <div class="form-group">
                                <input type="text" class="form-control" name="firstname" placeholder="New First Name">
                                <span id="msg-edit-firstname-firstname" class="help-block"></span>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" name="password" placeholder="Password">
                                <span id="msg-edit-firstname-password" class="help-block"></span>
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fa fa-paper-plane" aria-hidden="true"></i> Submit</button>
                            <button type="button" class="btn" id="cancel-edit-firstname"><i class="fa fa-times" aria-hidden="true"></i> Cancel</button>
                        </form>
                    </div>
                    <div class="detail">
                        <p>Last Name</p>
                        <small id="show-edit-lastname"><i class="fa fa-pencil" aria-hidden="true"></i> Change</small>
                        <form id="edit-lastname">
                            <div class="form-group">
                                <input type="text" class="form-control" name="lastname" placeholder="New Last Name">
                                <span id="msg-edit-lastname-lastname" class="help-block"></span>
                            </div>

                            <div class="form-group">
                                <input type="password" class="form-control" name="password" placeholder="Password">
                                <span id="msg-edit-lastname-password" class="help-block"></span>
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fa fa-paper-plane" aria-hidden="true"></i> Submit</button>
                            <button type="button" class="btn" id="cancel-edit-lastname"><i class="fa fa-times" aria-hidden="true"></i> Cancel</button>
                        </form>
                    </div>
                    <div class="detail">
                        <p>Organization</p>
                        <small id="show-edit-organization"><i class="fa fa-pencil" aria-hidden="true"></i> Change</small>
                        <form id="edit-organization">
                            <div class="form-group">
                                <input type="text" class="form-control" name="organization" placeholder="New Organization">
                                <span id="msg-edit-organization-organization" class="help-block"></span>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" name="password" placeholder="Password">
                                <span id="msg-edit-organization-password" class="help-block"></span>
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fa fa-paper-plane" aria-hidden="true"></i> Submit</button>
                            <button type="button" class="btn" id="cancel-edit-organization"><i class="fa fa-times" aria-hidden="true"></i> Cancel</button>
                        </form>
                    </div>
                </div>
                <div class="user-info">
                    <p class="header"><i class="fa fa-hdd-o" aria-hidden="true"></i> Storage Management</p>
                    <div class="detail">
                        <p>Pack and download all your documents</p>
                        <small><i class="fa fa-cogs" aria-hidden="true"></i> Proceed</small>
                    </div>
                    <div class="detail">
                        <p>Clear your storage</p>
                        <small><i class="fa fa-cogs" aria-hidden="true"></i> Proceed</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/jquery.storageapi.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
<script rel="script" src="${"/resources/js/jquery.serializejson.js"}"></script>
<script rel="script" src="${"/resources/js/manage.js"}"></script>
<script rel="script" src="${"/resources/js/sync.js"}"></script>
</body>
</html>