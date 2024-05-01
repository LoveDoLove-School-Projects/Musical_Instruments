<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="entities.Internalsecuritylog"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Security Log</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <%
        List<Internalsecuritylog> internalSecuritylogs = (List<Internalsecuritylog>) request.getAttribute("internalSecuritylogs");
        %>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Recent Event(s)</h3>
                        </div>
                        <div class="panel-body table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>Date/Time</th>
                                        <th>Username</th>
                                        <th>Action</th>
                                        <th>IP Address</th>
                                        <th>User Agent</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (internalSecuritylogs != null) {
                                        for (Internalsecuritylog internalSecuritylog : internalSecuritylogs) {
                                    %>
                                    <tr>
                                        <td><%=internalSecuritylog.getActionDate()%></td>
                                        <td><%=internalSecuritylog.getUsername()%></td>
                                        <td><%=internalSecuritylog.getAction()%></td>
                                        <td><%=internalSecuritylog.getIpAddress()%></td>
                                        <td><%=internalSecuritylog.getUserAgent()%></td>
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
    </body>
</html>