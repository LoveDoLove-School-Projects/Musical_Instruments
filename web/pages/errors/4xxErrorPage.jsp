<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>4xx Error</title>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center align-items-center" style="height:100vh">
                <div class="col-6">
                    <div class="card">
                        <div class="card-body">
                            <h2 class="card-title text-center">The things you are looking for are not here</h2>
                            <div class="text-center">
                                <a href="#" class="btn btn-primary">Go back to main page</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>