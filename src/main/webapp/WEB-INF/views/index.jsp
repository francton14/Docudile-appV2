<%--
  Created by IntelliJ IDEA.
  User: PaulRyan
  Date: 2/8/2016
  Time: 12:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Docudile | Home</title>
    <link rel="stylesheet" href="${"/resources/fonts/font-awesome/css/font-awesome.min.css"}">
    <link rel="stylesheet" href="${"/resources/css/bootstrap.min.css"}">
    <link rel="stylesheet" href="${"/resources/bootflat/css/bootflat.css"}">
    <link rel="stylesheet" href="${"/resources/css/index.css"}">
    <link rel="icon"
          type="image/png"
          href="${"resources/img/logo.png"}">
</head>
<body>

<header class="dd-frontpage dd-wallpaper dd-onepage">
    <div class="header">
        <a href="/">
            <div class="logo">
                <img src="${"/resources/img/logo.png"}" height="60px" width="60px">
                <h3>Docudile</h3>
            </div>
        </a>
    </div>
    <div class="container">
        <div class="row dd-frontpage-welcome">
            <div class="col-md-6">
                <h1 class="article-title">Easy document management.</h1>
                <p>Forget about losing your documents when you have Docudile to classify and keep them in order for you.</p>
                <div class="row article-buttons">
                    <div class="col-sm-5">
                        <a href="/register" type="button" class="btn btn-primary btn-block"><i class="fa fa-hand-o-right" aria-hidden="true"></i> Join Us</a>
                    </div>
                    <div class="col-sm-5">
                        <a href="/login" type="button" class="btn btn-block"><i class="fa fa-sign-in" aria-hidden="true"></i> Login</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<main>
    <div class="container dd-about">
        <div class="row">
            <div class="col-sm-6">
                <img src="${"resources/img/files.jpg"}" class="img-responsive">
            </div>
            <div class="col-sm-5 dd-about-text">
                <h6>DOCUMENT CLASSIFICATION</h6>
                <h3 class="article-title">Organize your documents with our document classification algo.</h3>
                <p>We utilize a unique algorithm for document classification to help you with the toughest things like losing your documents and documents.</p>

                <div class="dd-about-supp row">
                    <div class="col-sm-6">
                        <h5>Structure based</h5>
                        <p>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.
                            <a href="!#">Learn more.</a>
                        </p>
                    </div>
                    <div class="col-sm-6">
                        <h5>Reliability & uptime</h5>
                        <p>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.
                            <a href="!#">Learn more.</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="footer">
    <div class="container dd-footer">
        <div class="row">
            <div class="col-sm-5">
                <h6>ABOUT</h6>
                <p>
                    Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae at
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


<script rel="script" src="${"resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"resources/js/bootstrap.min.js"}"></script>
</body>
</html>
