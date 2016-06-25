<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Docudile | Login</title>

    <link rel="stylesheet" href="${"/resources/css/bootstrap.min.css"}">
    <link rel="stylesheet" href="${"/resources/bootflat/css/bootflat.css"}  ">
    <link rel="stylesheet" href="${"/resources/fonts/font-awesome/css/font-awesome.min.css"}">
    <link rel="stylesheet" href="${"/resources/icheck/square/blue.css"}">
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
        <p class="title">Log in to our service.</p>
        <p class="info">If you haven't signed up yet to our awesome service, please proceed to our <a
                href="/register">Registration</a> page, it works like a miracle.</p>
        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>
        <form action="login" method="post">
            <div class="form-group">
                <label for="email"><i class="fa fa-user-secret" aria-hidden="true"></i> Email</label>
                <input type="text" class="form-control" id="email" name="email" placeholder="Email">
            </div>
            <div class="form-group">
                <label for="password"><i class="fa fa-unlock-alt" aria-hidden="true"></i> Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Password">
            </div>
            <div class="checkbox">
                <label class="checkbox-label"><input type="checkbox" id="remember-me" name="remember-me"> Remember me?</label>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-6">
                    <button type="submit" class="btn btn-block">Login</button>
                </div>
            </div>
        </form>
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
<script rel="script" src="${"/resources/js/login.js"}"></script>
<script rel="script" src="${"/resources/icheck/icheck.min.js"}"></script>
</body>
</html>