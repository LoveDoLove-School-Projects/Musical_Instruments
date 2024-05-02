<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Carts" %>
<%
int numberCart=0;
double subTotal=0;
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

        <main>
            <section class="section">
                <table class="table">
                    <thead class="text-center">
                        <tr class="row justify-content-center align-content-center">
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">No.</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Image</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Name</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Color</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Quantity</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Price</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Total Price</th>
                            <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Function</th>
                        </tr>
                    </thead>

                    <tbody class="text-center">
                      //the content
                    </tbody>
                </table>

               
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />

    </body>
</html>
