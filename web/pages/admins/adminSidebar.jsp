<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <body>
        <!-- Sidebar -->
        <nav id="sidebarMenu" class="collapse d-lg-block sidebar collapse bg-white">
            <div class="position-sticky ">
                <div class="list-group list-group-flush mx-3 mt-4">
                    <a
                        href="#"
                        class="list-group-item list-group-item-action py-2 ripple"
                        aria-current="true"
                        >
                        <i class="fas fa-tachometer-alt fa-fw me-3"></i><span>Main dashboard</span>
                    </a>
                    <a href="#" class="list-group-item list-group-item-action py-2 ripple active">
                        <i class="fas fa-chart-area fa-fw me-3"></i><span>Manage Staff</span>
                    </a>
                    <a href="#" class="list-group-item list-group-item-action py-2 ripple"
                       ><i class="fas fa-lock fa-fw me-3"></i><span>Manager Customer</span></a
                    >
                    <a href="#" class="list-group-item list-group-item-action py-2 ripple"
                       ><i class="fas fa-chart-line fa-fw me-3"></i><span>Manage Stock</span></a
                    >
                    <a href="#" class="list-group-item list-group-item-action py-2 ripple">
                        <i class="fas fa-chart-pie fa-fw me-3"></i><span>View Transaction</span>
                    </a>
                    <a href="#" class="list-group-item list-group-item-action py-2 ripple"
                       ><i class="fas fa-chart-bar fa-fw me-3"></i><span>Control Panel</span></a
                    >
                    <a href="#" class="list-group-item list-group-item-action py-2 ripple"
                       ><i class="fas fa-money-bill fa-fw me-3"></i><span>Sales</span></a
                    >
                </div>
        </nav>
        <!-- Sidebar -->
    </body>
</html>
