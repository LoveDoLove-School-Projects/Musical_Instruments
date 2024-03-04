<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    </head>
    <body>
        <jsp:include page="navigation.jsp" />
        <% String message = (String) request.getAttribute("message"); if (message != null && !message.isEmpty()) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } %>
    </body>
</html>
