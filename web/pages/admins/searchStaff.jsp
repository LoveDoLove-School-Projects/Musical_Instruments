<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.*" %>
<%@ page import="entities.Staffs" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Manage Staff</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <style>
            body, html {
                margin: 0;
                padding: 0;
            }
            body {
                min-height: 100vh;
                background-color: #fbfbfb;
                font-family: 'Roboto', sans-serif;
            }

            .btn {
                width: 100%;
                padding: 15px;
                border: groove 10px black;
                background-color: grey;
                color: #fff;
                border-radius: 5px;
                transition: all 0.3s ease;
                font-size: 22px;
                font-weight: bold;
                text-decoration: none;
                justify-content: center;
                align-content: center;
            }
            .btn:hover {
                background-color: lightslategray;
            }

            .staff-count-box {
                background-color: #6666ff;
                color: #fff;
                border: 10px double lightcyan;
                border-radius: 10px;
                padding: 20px;
                text-align: center;
                margin-bottom: 20px;
            }
            .staff-count-box i {
                font-size: 36px;
                margin-bottom: 10px;
            }
            h3 > strong {
                font-size: 37px;
            }

        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <br><h1><i class="fas fa-user">  Staff Management</i></h1><br>
            <div class="staff-count-box">
                <i class="fas fa-user"></i>
                <h3>Number of Staffs: <strong>(<c:out value="${staffCount}"/>) </strong></h3>
            </div>
            <form class="form-inline mb-3" action="pages/admins/searchStaff" method="post">
                <input class="col-5 form-control mr-2" type="text" name="searchQuery" placeholder="Enter staff email: "><br>
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="pages/superAdmin/addStaff" class="btn btn-success"><i class="fas fa-user-plus"></i> Register new Staff</a>
            </form>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-body table-responsive">
                            <table id="searchStaffTable" class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Gender</th>
                                        <th>Email</th>
                                        <th>Phone Number</th>
                                        <th>Account Creation Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Staffs> staffList = (List<Staffs>) request.getAttribute("staffList");
                                        if (staffList != null) {
                                            for (Staffs staff : staffList) {
                                    %>
                                    <tr>
                                        <td><%=staff.getUserId()%></td>
                                        <td><%=staff.getUsername()%></td>
                                        <td><%=staff.getGender()%></td>
                                        <td><%=staff.getEmail()%></td>
                                        <td><%=staff.getPhoneNumber()%></td>
                                        <td><%=staff.getAccountCreationDate()%></td>
                                    </tr>
                                    <% }
                                    } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module">
            $(document).ready(function () {
                $('#searchStaffTable').DataTable();
            });
        </script>
    </body>
</html>
