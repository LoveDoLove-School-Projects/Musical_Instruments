<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html> 
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Product Page</title>
        <link rel="stylesheet" href="assets/css/product.css" />
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        
         <style>
        section {
            background-color: #d9d9e1;
        }

        .addtocartbtn {
            background-color: #d9d9e1;
            border: solid rgb(82, 78, 78) 2px;
            border-radius: 20px;
            color: black;
        }

        .addtocartbtn:hover {
            background-color: #343333;
            color: #d9d9e1;
            transition: 0.5s ease;f
        }
        
        p{
            font-size: 20px;
        }
    </style>
    
        <main class="main">
        <section class="section">
            <div class="row m-5 d-flex align-items-center justify-content-center">
                ${productDetails}   
            </div>
        </section>
    </main>


        </main>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
