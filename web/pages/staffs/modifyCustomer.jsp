<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="entities.Customers" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Modify Customer Information</title>
        <style>
            body {
                padding-top: 20px;
            }
            .form-group {
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <div class="container">
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <h1 class="text-center mb-4">Modify Customer Information</h1>
                    <form id="modifyForm" action="pages/staffs/modifyCustomerServlet" method="POST" onsubmit="return confirmUpdate()">
                        <% Customers customerDetails = (Customers) session.getAttribute("customerDetails"); %>
                        <div class="form-group">
                            <label for="username">Username:</label>
                            <input type="text" class="form-control" name="username" value="<%=customerDetails.getUsername()%>">
                        </div>
                        <div class="form-group">
                            <label for="gender">Gender:</label>
                            <select class="form-control" id="gender" name="gender">
                                <option value="Male" <c:if test="${customerDetails.getGender().equals('Male')}">selected</c:if>>Male</option>
                                <option value="Female" <c:if test="${customerDetails.getGender().equals('Female')}">selected</c:if>>Female</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="address">Address:</label>
                                <textarea class="form-control" name="address" rows="3" required><%=customerDetails.getAddress()%></textarea>
                        </div>
                        <div class="form-group">
                            <label for="phoneNumber">Phone Number:</label>
                            <input type="text" class="form-control" name="phoneNumber" value="<%=customerDetails.getPhoneNumber()%>" pattern="[0-9]+" required>
                        </div>
                        <input type="hidden" name="userId" value="<%=customerDetails.getUserId()%>">
                        <div class="form-group">
                            <button type="submit" class="btn btn-success">Update</button>
                            <button type="button" class="btn btn-danger" onclick="confirmModification()">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="/defaults/footer.jsp" />

        <script>
            function confirmModification() {
                if (confirm("Are you sure you want to cancel the modification?")) {
                    window.location.href = "pages/staffs/manageCustomer";
                }
            }

            function confirmUpdate() {
                return confirm("Are you sure you want to submit the form?");
            }
        </script>

    </body>
</html>
