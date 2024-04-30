<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Manage Customer</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <h1>Manage Customer</h1><br>

            <form class="form-inline mb-3" action="pages/staffs/searchCustomerServlet" method="POST">
                <input class="form-control mr-2" type="text" name="searchQuery" placeholder="Enter customer email: "><br>
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>

