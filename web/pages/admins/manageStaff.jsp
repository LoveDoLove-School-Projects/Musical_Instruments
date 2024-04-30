<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List" %>
<%@ page import="entities.Staffs" %>
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
                        <div class="card-body">
                            <h5 class="card-title"><strong><%=staffDetails.getUsername() %></strong></h5>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item"><strong>ID:</strong> <%=staffDetails.getUserId() %></li>
                                <li class="list-group-item"><strong>Gender:</strong> <%=staffDetails.getGender() %></li>
                                <li class="list-group-item"><strong>Email:</strong> <%=staffDetails.getEmail() %></li>
                                <li class="list-group-item"><strong>Address:</strong> <%=staffDetails.getAddress() %></li>
                                <li class="list-group-item"><strong>Phone Number:</strong> <%=staffDetails.getPhoneNumber() %></li>
                                <li class="list-group-item"><strong>Two Factor Authentication:</strong> <%=staffDetails.getTwoFactorAuth() ? "Activated" : "Deactivated" %></li>
                                <li class="list-group-item"><strong>Account Creation Date:</strong> <%=staffDetails.getAccountCreationDate() %></li>
                            </ul>
                            <div class="row">
                                <div class="col-md-2">
                                    <a href="pages/admins/searchStaff" class="btn btn-primary mr-2">Go back</a>
                                </div>
                                <div class="col-md-2">
                                    <a href="pages/admins/modifyStaff" class="btn btn-success mr-2">Modify</a>
                                </div>
                                <div class="col-md-1">

                                    <form action="pages/admins/deleteStaff" method="post" id="deleteStaffForm">
                                        <input type="hidden" name="userId" value="<%=staffDetails.getUserId() %>"/>
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
                                return
                            }
                            deleteStaffFormElement.submit();
                        });
                    }
                }
                setDeleteStaffForm();
        </script>
        <%
            }
        %>
    </body>
</html>
