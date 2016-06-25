<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-lg-3 profile-nav">
    <div class="profile-nav-header">
        <h4>Profile</h4>
    </div>
    <div class="profile-nav-content">
        <ul>
            <li <c:if test="${profileNav eq 'info'}">class="active"</c:if>>
                <a href="/profile">
                    <p><i class="fa fa-info-circle" aria-hidden="true"></i> Account Information</p>
                </a>
            </li>
            <li <c:if test="${profileNav eq 'manage'}">class="active"</c:if>>
                <a href="/profile/manage"><p><i class="fa fa-wrench" aria-hidden="true"></i> Manage</p></a>
            </li>
            <li <c:if test="${profileNav eq 'dropbox'}">class="active"</c:if>>
                <a href="/profile/dropbox"><p><i class="fa fa-dropbox" aria-hidden="true"></i> Dropbox</p></a>
            </li>
        </ul>
    </div>
</div>