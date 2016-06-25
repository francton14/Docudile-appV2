<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Docudile | Register</title>

    <link rel="stylesheet" href="${"/resources/css/bootstrap.min.css"}">
    <link rel="stylesheet" href="${"/resources/bootflat/css/bootflat.css"}">
    <link rel="stylesheet" href="${"/resources/fonts/font-awesome/css/font-awesome.min.css"}">
    <link rel="stylesheet" href="${"/resources/css/index.css"}">
    <link rel="stylesheet" href="${"/resources/css/login-register.css"}">

    <link rel="icon"
          type="image/png"
          href="${"/resources/img/logo.png"}">
</head>
<body>
<header>
    <div class="header">
        <a href="/">
            <div class="logo">
                <img src="${"/resources/img/logo.png"}" height="60px" width="60px">
                <h3>Docudile</h3>
            </div>
        </a>
    </div>
</header>
<main>
    <div class="small-container">
        <p class="title">Join us and explore.</p>
        <p class="info">Fill in the necessary information. If you have an already existing account, you can proceed
            to the <a href="/login">Login</a> page.</p>
        <form:form action="/register" commandName="userRegistration" modelAttribute="userRegistration" method="post">
            <div class="form-group">
                <form:input type="email" class="form-control" path="emailObject.email" placeholder="Email Address"/>
                <form:errors cssClass="help-block" path="emailObject.email"/>
            </div>
            <div class="form-group">
                <form:input type="email" class="form-control" path="emailObject.confirmEmail" placeholder="Confirm Email Address"/>
                <form:errors cssClass="help-block" path="emailObject.confirmEmail"/>
            </div>
            <div class="form-group">
                <form:input type="password" class="form-control" path="passwordObject.password" placeholder="Password"/>
                <form:errors cssClass="help-block" path="passwordObject.password"/>
            </div>
            <div class="form-group">
                <form:input type="password" class="form-control" path="passwordObject.confirmPassword" placeholder="Confirm Password"/>
                <form:errors cssClass="help-block" path="passwordObject.confirmPassword"/>
            </div>
            <div class="form-group">
                <form:input type="text" class="form-control" path="firstname" placeholder="First Name"/>
                <form:errors cssClass="help-block" path="firstname"/>
            </div>
            <div class="form-group">
                <form:input type="text" class="form-control" path="lastname" placeholder="Last Name"/>
                <form:errors cssClass="help-block" path="lastname"/>
            </div>
            <div class="form-group">
                <form:input type="text" class="form-control" path="organization" placeholder="Organization"/>
                <form:errors cssClass="help-block" path="organization"/>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <p class="agreement"><i class="fa fa-info-circle" aria-hidden="true"></i> AGREEMENT: All your documents will be stored securely. It is now up to your discretion on how you
                will handle your documents. We will not be held responsible for any document loss due to accidental
                misuse. If you so agree with the statements above, please click the button below to proceed.</p>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-6">
                    <button type="submit" class="btn btn-block"><i class="fa fa-paper-plane" aria-hidden="true"></i> Agree & Submit</button>
                </div>
            </div>
        </form:form>
    </div>
</main>
<footer class="footer">
    <div class="container dd-footer">
        <div class="row">
            <div class="col-sm-5">
                <h6>ABOUT</h6>
                <p>
                    Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium,
                    totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae at
                    <a href="!#">info@docudi.le</a>.
                </p>
            </div>
            <div class="col-sm-2 col-sm-offset-1">
                <h6>PRODUCT</h6>
                <ul>
                    <li>Features</li>
                    <li>Examples</li>
                    <li>Tour</li>
                    <li>Gallery</li>
                </ul>
            </div>
            <div class="col-sm-2">
                <h6>Technologies</h6>
                <ul>
                    <li>Dropbox API</li>
                    <li>Abbyy Finereader 12</li>
                    <li>Spring MVC</li>
                    <li>Hibernate</li>
                </ul>
            </div>
            <div class="col-sm-2">
                <h6>LEGAL</h6>
                <ul>
                    <li>Terms</li>
                    <li>Legal</li>
                    <li>Privacy</li>
                    <li>License</li>
                </ul>
            </div>
        </div>
    </div>
</footer>
<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
</body>
</html>
