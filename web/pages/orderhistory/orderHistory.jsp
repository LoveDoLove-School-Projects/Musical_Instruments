<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ include file="viewOrderHistory.jsp" %>--%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="entities.Orders"%>
<%@ page import="entities.Transactions"%>
<%@ page import="jakarta.persistence.EntityManager"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.lang.Exception" %>
<%
int numberOrder=0;
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Cart Page</title>
        <link rel="stylesheet" href="assets/css/cart.css" />
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <div class="container">
            <div class="row">
                <div class="col-md-12 m-3">
                    <div class="text-center panel-heading">
                        <h3 class="panel-title">Order History</h3>
                    </div>
                    <div class="panel panel-default">
                        <table class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>No.</th>
                                    <th>Order Number</th>
                                    <th>Total Number</th>
                                    <th>Order Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
                                List<Orders> orders = (List<Orders>) request.getAttribute("orderLists");
                                Set<String> processedOrderNumbers = new HashSet<>();
                                for (Orders orderDetails : orders) {
                                    String orderNumber = orderDetails.getOrderNumber();
                                    if (!processedOrderNumbers.contains(orderNumber)) {
                                        processedOrderNumbers.add(orderNumber);
                                        Transactions transactions = null;
                                        try {
                                            transactions = entityManager.createNamedQuery("Transactions.findByOrderNumber", Transactions.class)
                                                    .setParameter("orderNumber", orderDetails.getOrderNumber())
                                                    .getSingleResult();
                                        } catch (Exception e) {
                                            // If no result found, skip to the next iteration
                                            continue;
                                        }
                                        numberOrder++;
                                %>
                                <tr class="clickable-row" data-href="pages/orderhistory/viewOrderHistory?order_number=<%= orderDetails.getOrderNumber() %>">
                                    <td><%=numberOrder%></td>
                                    <td><%=orderDetails.getOrderNumber()%></td>
                                    <td>RM<%=transactions.getTotalAmount()%></td>
                                    <td><%=orderDetails.getOrderDate()%></td>
                                </tr>
                                <%
                                    }
                                }
                                %>
                            </tbody>

                        </table>
                    </div>
                </div>
            </div>
        </div>

    </section>
</main>
<jsp:include page="/defaults/footer.jsp" />
<script>
    $(document).ready(function () {
        $('.clickable-row').click(function () {
            window.location = $(this).data("href");
        });
    });
</script>

</body>
</html>