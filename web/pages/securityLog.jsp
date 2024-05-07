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
                <div class="col-md-12 m-3">
                    <div class="panel panel-default">
                        <div class="panel-heading d-flex justify-content-between align-items-center">
                            <h3 class="panel-title">Recent events</h3>
                            <button id="exportButton" class="btn btn-primary md-3">Export to CSV</button>
                        </div>
                        <div class="panel-body table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>Date/Time</th>
                                        <th>Username</th>
                                        <th>Action</th>
                                        <th>Ip Address</th>
                                        <th>User Agent</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (securitylogs != null) {
                                        for (Securitylog securitylog : securitylogs) {
                                    %>
                                    <tr>
                                        <td><%=securitylog.getActionDate()%></td>
                                        <td><%=securitylog.getUsername()%></td>
                                        <td><%=securitylog.getAction()%></td>
                                        <td><%=securitylog.getIpAddress()%></td>
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
        <script>
            document.getElementById('exportButton').addEventListener('click', function () {
                var table = document.querySelector('table');
                var data = [];
                for (var row of table.rows) {
                    var rowData = [];
                    for (var cell of row.cells) {
                        rowData.push('"' + cell.textContent + '"'); // wrap cell text in quotes to handle commas in cell text
                    }
                    data.push(rowData.join(','));
                }
                var csv = data.join('\n');
                var blob = new Blob([csv], {type: 'text/csv;charset=utf-8;'});
                saveAs(blob, 'security_log.csv');
            });
        </script>
    </body>
</html>