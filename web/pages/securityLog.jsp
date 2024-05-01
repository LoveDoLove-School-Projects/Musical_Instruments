<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="entities.Securitylog"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Security Log</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <%
        List<Securitylog> securitylogs = (List<Securitylog>) request.getAttribute("securitylogs");
        %>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Recent events</h3>
                        </div>
                        <div class="panel-body table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>Id</th>
                                        <th>Username</th>
                                        <th>Ip Address</th>
                                        <th>Date/Time</th>
                                        <th>Action</th>
                                        <th>User Agent</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (securitylogs != null) {
                                        for (Securitylog securitylog : securitylogs) {
                                    %>
                                    <tr>
                                        <td><%=securitylog.getPkid()%></td>
                                        <td><%=securitylog.getUsername()%></td>
                                        <td><%=securitylog.getIpAddress()%></td>
                                        <td><%=securitylog.getActionDate()%></td>
                                        <td><%=securitylog.getAction()%></td>
                                        <td><%=securitylog.getUserAgent()%></td>
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