<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Manage Product</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <h1>Manage Product</h1><br>
            <form class="form-inline mb-3" action="pages/staffs/searchProduct" method="POST">
                <input class="form-control mr-2" type="text" name="searchQuery" placeholder="Enter product ID: "><br>
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="pages/staffs/addProduct" class="btn btn-success">Add new product</a>
            </form>

        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>

