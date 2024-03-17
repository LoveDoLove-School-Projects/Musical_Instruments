<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <base href="${basePath}">
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
                                                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp"
                                                         alt="avatar" class="rounded-circle img-fluid" style="width: 150px; height: 150px;">
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Female -->
                                                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1.webp"
                                                         alt="avatar" class="rounded-circle img-fluid" style="width: 150px; height: 150px;">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="data:image/jpg;base64,${pictureBase64}" alt="avatar" class="rounded-circle img-fluid" style="width: 150px; height: 150px;">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="mt-3">
                                        <c:choose>
                                            <c:when test="${empty pictureBase64}">
                                                <!-- <form action="pages/profile/uploadPicture" method="post" enctype="multipart/form-data"> -->
                                                <input type="file" name="uploadPicture" id="uploadPicture" class="d-none" accept="image/*">
                                                <label for="uploadPicture" class="btn btn-primary">Upload Picture</label>
                                                <!-- </form> -->
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-danger" id="removePicture">Remove Picture</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-8">
                        <form action="pages/profile/updateProfile" method="POST">
                            <div class="card mb-4">
                                <div class="card-header bg-white text-center">
                                    <h5 class="mb-0">Profile Information</h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <label for="username">Username</label>
                                        <div class="col-sm-9">
                                            <input type="text" id="username" name="username" class="form-control" value="${username}" />
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
                                            <input type="text" id="address" name="address" class="form-control" value="${address}" />
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="phone">Phone</label>
                                        <div class="col-sm-9">
                                            <input type="text" id="phone" name="phone" class="form-control" value="${phone_number}" />
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="gender">Gender</label>
                                        <div class="col-sm-9">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       value="Male" checked/>
                                                <label class="form-check-label"
                                                       for="maleGender">Male</label>
                                            </div>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       value="Female" />
                                                <label class="form-check-label"
                                                       for="femaleGender">Female</label>
                                            </div>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <label for="2fa">2FA</label>
                                        <div class="col-sm-9">
                                            <input type="checkbox" id="2fa" name="2fa" ${twoFactorAuth == true ? 'checked' : ''} />
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-9">
                                            <button type="button" class="btn btn-primary" id="updateProfile">Update Profile</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/profile.js"></script>
    </body>

</html>