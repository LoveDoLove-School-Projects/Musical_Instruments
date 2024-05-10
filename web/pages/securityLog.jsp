<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Security Log</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <div class="row">
                <div class="col-md-12 m-3">
                    <div class="panel panel-default">
                        <div class="panel-heading d-flex justify-content-between align-items-center">
                            <h3 class="panel-title">Recent events</h3>
                            <button id="exportButton" class="btn btn-primary md-3">Export to CSV</button>
                        </div>
                        <div class="panel-body table-responsive">
                            <table id="securityLogTable" class="table table-striped table-bordered table-hover">
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
                                    <c:forEach var="securitylog" items="${securitylogs}">
                                        <tr>
                                            <td>${securitylog.actionDate}</td>
                                            <td>${securitylog.username}</td>
                                            <td>${securitylog.action}</td>
                                            <td>${securitylog.ipAddress}</td>
                                            <td>${securitylog.userAgent}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module">
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
                saveAs(blob, `securityLog.csv`);
            });
            $(document).ready(function () {
                $('#securityLogTable').DataTable();
            });
        </script>
    </body>
</html>