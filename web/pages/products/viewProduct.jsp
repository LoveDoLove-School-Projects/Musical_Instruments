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

            .addtocartbtn {
                background-color: #d9d9e1;
                border: solid rgb(82, 78, 78) 1px;
                color: black;
                font-size: 20px;
            }

            .addtocartbtn:hover {
                background-color: #343333;
                color: #d9d9e1;
                transition: 0.5s ease;
            }

            .productImage{
                background-color: #d9d9e1;
                border: solid black 0spx;
            }



            .qty-container .input-qty{
                border: 1px solid #d4d4d4;
                height: 38px;
                font-size: 20px;
            }
            
            .qty-container .qty-btn-plus,
            .qty-container .qty-btn-minus{
                border: 1px solid #d4d4d4;
                font-size: 20px;
                height: 38px;
                width: 38px;
                transition: 0.3s;
            }

        </style>

        <main class="main">
            <section class="section">
                <div class="container">
                    <div class="row m-5 d-flex align-items-center justify-content-center">
                        ${productDetails}   
                    </div>
                </div>
            </section>
        </main>

        <script>
            var buttonPlus = $(".qty-btn-plus");
            var buttonMinus = $(".qty-btn-minus");

            var incrementPlus = buttonPlus.click(function () {
                var $n = $(this)
                        .parent(".qty-container")
                        .find(".input-qty");
                $n.val(Number($n.val()) + 1);
            });

            var incrementMinus = buttonMinus.click(function () {
                var $n = $(this)
                        .parent(".qty-container")
                        .find(".input-qty");
                var amount = Number($n.val());
                if (amount > 0) {
                    $n.val(amount - 1);
                }
            });
        </script>

        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
