<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<footer class="footer mt-auto py-3 bg-light">
    <div class="container text-center">
        <span class="text-muted">© 2024 <%=application.getInitParameter("companyName")%>. <%=application.getInitParameter("companyCopyright")%></span>
        <br />
        <span class="text-muted">Phone: <%=application.getInitParameter("companyHotline")%></span>
        <br />
        <span class="text-muted">Email: <%=application.getInitParameter("companyEmail")%></span>
    </div>
</footer>
