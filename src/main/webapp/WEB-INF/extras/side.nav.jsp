<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-sm-2 dd-navtree">
    <div class="navtree-header">
        <h4>Docudile</h4>
    </div>
    <ul>
        <li <c:if test="${sideNav eq 'search'}">class="active"</c:if>>
            <a href="/search">
                <p><i class="fa fa-search" aria-hidden="true"></i> Search</p>
            </a>
        </li>
        <li <c:if test="${sideNav eq 'storage'}">class="active"</c:if>>
            <a href="/">
                <p><i class="fa fa-hdd-o" aria-hidden="true"></i> Storage</p>
            </a>
        </li>
    </ul>
    <c:choose>
        <c:when test="${sideNav eq 'storage'}">
            <div id="treeview"></div>
        </c:when>
        <c:otherwise>
            <span class="flex"></span>
        </c:otherwise>
    </c:choose>
    <ul>
        <li <c:if test="${sideNav eq 'upload'}">class="active"</c:if>>
            <a href="/upload">
                <p><i class="fa fa-upload" aria-hidden="true"></i> Upload</p>
            </a>
        </li>
        <li>
            <a id="sync">
                <p id="sync-nav"><i class="fa fa-refresh" aria-hidden="true"></i> Sync</p>
            </a>
        </li>
        <li <c:if test="${sideNav eq 'profile'}">class="active"</c:if>>
            <a href="/profile">
                <p><i class="fa fa-user" aria-hidden="true"></i> ${user.email}</p>
            </a>
        </li>
        <li>
            <a href="/logout">
                <p><i class="fa fa-sign-out" aria-hidden="true"></i> Logout</p>
            </a>
        </li>
    </ul>
</div>