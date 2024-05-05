<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.security.Principal" %>
<%@ page import="entities.Session" %>
<%@ page import="entities.Role" %>
<%
Principal principal = request.getUserPrincipal();
String j_username = principal == null ? null : principal.getName();
boolean isAdmin = j_username != null ? true : false;
Session user_session = (Session) session.getAttribute("user_session");
%>
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Profile</title>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <section class="vh-100" style="background-color: #eee;">
            <div class="container py-5">
                <div class="row">
                    <div class="col-lg-4">
                        <div class="card mb-4">
                            <div class="card-header bg-white text-center">
                                <h5 class="mb-0">Profile Picture</h5>
                            </div>
                            <div class="card-body text-center">
                                <div>
                                    <c:choose>
                                        <c:when test="${empty pictureBase64}">
                                            <c:choose>
                                                <c:when test="${gender == 'Male'}">
                                                    <!-- Male -->
                                                    <img src="assets/image/maleDefaultImage.webp"
                                                         alt="avatar" class="rounded-circle img-fluid"
                                                         style="width: 150px; height: 150px;">
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Female -->
                                                    <img src="assets/image/femaleDefaultImage.webp"
                                                         alt="avatar" class="rounded-circle img-fluid"
                                                         style="width: 150px; height: 150px;">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="data:image/jpg;base64,${pictureBase64}" alt="avatar"
                                                 class="rounded-circle img-fluid"
                                                 style="width: 150px; height: 150px;">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="mt-3">
                                        <c:choose>
                                            <c:when test="${empty pictureBase64}">
                                                <form action="pages/profile/uploadPicture" method="post" enctype="multipart/form-data" id="uploadPictureForm">
                                                    <input type="file" name="uploadPicture" id="uploadPicture"
                                                           class="d-none" accept="image/*">
                                                    <label for="uploadPicture" class="btn btn-primary">Upload
                                                        Picture</label>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="pages/profile/removePicture" method="post" id="removePictureForm">
                                                    <button type="button" class="btn btn-danger"
                                                            id="removePicture">Remove Picture</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-8">
                        <form action="pages/profile/updateProfile" method="POST" id="updateProfileForm">
                            <div class="card mb-4">
                                <div class="card-header bg-white text-center">
                                    <h5 class="mb-0">Profile Information</h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <label for="username">Username</label>
                                        <div class="col-sm-9">
                                            <input type="text" id="username" name="username" class="form-control"
                                                   value="${username}" />
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="email">Email</label>
                                        <div class="col-sm-9">
                                            <p class="text-muted mb-0">${email}</p>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="address">Address</label>
                                        <div class="col-sm-9">
                                            <input type="text" id="address" name="address" class="form-control"
                                                   value="${address}" />
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="phone_number">Phone</label>
                                        <div class="col-sm-9">
                                            <input type="text" id="phone_number" name="phone_number" class="form-control"
                                                   value="${phone_number}" pattern="[0-9]+" />
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="gender">Gender</label>
                                        <div class="col-sm-9">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       id="maleGender" value="Male" <c:choose>
                                                           <c:when test="${gender eq 'Male'}">checked</c:when>
                                                       </c:choose> />
                                                <label class="form-check-label" for="maleGender">Male</label>
                                            </div>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       id="femaleGender" value="Female" <c:choose>
                                                           <c:when test="${gender eq 'Female'}">checked</c:when>
                                                       </c:choose> />
                                                <label class="form-check-label" for="femaleGender">Female</label>
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="gender">Two Factor Auth</label>
                                        <div class="col-sm-9">
                                            <div class="form-check form-check-inline">
                                                <input type="checkbox" name="two_factor_auth" id="two_factor_auth"
                                                       class="form-check-input" <c:choose>
                                                           <c:when test="${two_factor_auth}">checked</c:when>
                                                       </c:choose> />
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-md-4 text-left">
                                            <input type="submit" class="btn btn-primary" value="Update Profile" />
                                        </div>
                                        <div class="col-md-8 text-right">
                                            <%
                                            if ((user_session != null && user_session.getRole() != Role.STAFF) && !isAdmin) {
                                            %>
                                            <a href="pages/billingDetails" class="btn btn-primary">Update Billing Details</a>
                                            <%
                                            }
                                            %>
                                            <a href="pages/changePassword" class="btn btn-primary">Change Password</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
        </section>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/profile.js"></script>
    </body>

</html>