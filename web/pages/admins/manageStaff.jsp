<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List" %>
<%@ page import="entities.Staffs" %>
<%@ page import="java.util.Base64"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Staff Details</title>
        <style>
            body {
                padding-top: 20px;
            }
            .card {
                margin-bottom: 20px;
            }

            label {
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <div class="row">
                <div class="col-md-10 offset-md-1">
                    <h1 class="text-center mb-4">Staff Details</h1>
                    <% Staffs staff = (Staffs) session.getAttribute("staffDetails"); %>
                    <div class="card">
                        <%
                             String imageSrc = null;
                             if (staff.getPicture() != null) {
                                  String pictureBase64 = Base64.getEncoder().encodeToString(staff.getPicture());
                                  imageSrc = "data:image/png;base64," + pictureBase64;
                            }
                        %>
                        <div class="card-body">
                            <div class="col-3 p-1 d-flex justify-content-center align-content-centers productImage">
                                <img src="<%if(imageSrc!=null) { %><%=imageSrc%> <%}%>" class="img-fluid w-100 m-5" alt="<%=staff.getUsername() %>">
                            </div>
                            <h5 class="card-title"><strong><%= staff.getUsername() %></strong></h5>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item"><strong>ID:</strong> <%= staff.getUserId() %></li>
                                <li class="list-group-item"><strong>Gender:</strong> <%= staff.getGender() %></li>
                                <li class="list-group-item"><strong>Email:</strong> <%= staff.getEmail() %></li>
                                <li class="list-group-item"><strong>Address:</strong> <%= staff.getAddress() %></li>
                                <li class="list-group-item"><strong>Phone Number:</strong> <%= staff.getPhoneNumber() %></li>
                                <li class="list-group-item"><strong>Two Factor Authentication:</strong> <%= staff.getTwoFactorAuth() ? "Activated" : "Deactivated" %></li>
                                <li class="list-group-item"><strong>Account Creation Date:</strong> <%= staff.getAccountCreationDate() %></li>
                            </ul>
                            <div class="row">
                                <div class="col-md-2">
                                    <a href="pages/admins/searchStaff" class="btn btn-primary mr-2">Go back</a>
                                </div>
                                <div class="col-md-2">
                                    <a href="pages/superAdmin/modifyStaff" class="btn btn-success mr-2">Modify</a>
                                </div>
                                <div class="col-md-1">

                                    <form action="pages/superAdmin/deleteStaff" method="post" id="deleteStaffForm">
                                        <input type="hidden" name="userId" value="<%=staff.getUserId() %>"/>
                                        <button class="btn btn-danger">Delete</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />

        <script type="module">
            import { showErrorDialog } from "${basePath}assets/js/dialog.js";
            let deleteStaffFormElement = document.getElementById("deleteStaffForm");
            function setDeleteStaffForm() {
                if (deleteStaffFormElement === null) {
                    showErrorDialog("Delete button not found");
                    return;
                }
                deleteStaffFormElement.onsubmit = function (event) {
                    event.preventDefault();
                    Swal.fire({
                        title: "Are you sure?",
                        text: "You won't be able to revert this!",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#3085d6",
                        cancelButtonColor: "#d33",
                        confirmButtonText: "Yes, delete it!",
                    }).then((result) => {
                        if (!result.isConfirmed) {
                            event.preventDefault();
                            return;
                        }
                        deleteStaffFormElement.submit();
                    });
                }
            }
            setDeleteStaffForm();
        </script>
    </body>
</html>
