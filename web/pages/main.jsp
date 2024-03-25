<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Musical Instruments</title>
        <link rel="stylesheet" href="assets/css/index.css"/>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <div class="header">
                <h1>Welcome to our Musical Instruments E-commerce</h1>
                <h2>Damien start to do</h2>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>

</html>