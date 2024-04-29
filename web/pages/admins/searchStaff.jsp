<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Manage Staff</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <h1>Manage Staff</h1><br>
            <!-- Search form -->
            <form class="form-inline mb-3" action="pages/admins/StaffServlet" method="post">
                <input class="col-12 form-control mr-2" type="text" name="searchQuery" placeholder="Enter staff email: "><br>
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="pages/admins/addStaff.jsp" class="btn btn-success">Register new Staff</a>
            </form>

        </div>

        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>

