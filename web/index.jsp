<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath"
       value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <base href="${basePath}">
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Musical Instruments</title>
        <link rel="stylesheet" href="assets/css/index.css"/>
    </head>

    <body>
        <jsp:include page="defaults/header.jsp" />
        <div class="container">
            <div class="header">
                <h1>Welcome to our Musical Instruments E-commerce</h1>
            </div>
        </div>
        <jsp:include page="defaults/footer.jsp" />
    </body>

</html>