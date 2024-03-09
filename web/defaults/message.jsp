<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
    </head>
    <body>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-${sessionScope.messageStatus} alert-dismissible fade show" role="alert">
            ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <%
            session.removeAttribute("message");
            session.removeAttribute("messageStatus");
        %>
    </c:if>
</body>
</html>