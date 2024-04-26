<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Cart Page</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <main>
            <section class="vh-100">
                <table class="table">
                    <thead class="text-center TitleBar">
                        <tr>
                            <th class="col p-3 Title">No.</th>
                            <th class="col p-3 Title">Product Name</th>
                            <th class="col p-3 Title">Varation</th>
                            <th class="col p-3 Title">Quantity</th>
                            <th class="col p-3 Title">Price</th>
                        </tr>
                    </thead>

                    <tbody class="text-center">
                        <tr>
                            <th class="col">1</th>
                            <th class="col">GTy6-3</th>
                            <th class="col">Piano</th>
                            <th class="col d-flex justify-content-center">
                                <input type="number" name="quant[1]" class="form-control w-25" value="1" min="1" max="?">
                            </th>
                            <th class="col">RM25.89</th>
                        </tr>

                        <tr>
                            <th class="col">1</th>
                            <th class="col">GTy6-3</th>
                            <th class="col">Piano</th>
                            <th class="col d-flex justify-content-center">
                                <input type="number" name="quant[1]" class="form-control w-25" value="1" min="1" max="?">
                            <th class="col">RM25.89</th>
                        </tr>
                    </tbody>
                    <div class="row justify-content-center align-items-center">
                        <tfoot class="col-12 text-center" style="float:right;">
                            <tr class="d-flex justify-content-end">
                                <th class="col">Subtotal :</th>
                                <th class="col">RM25.89</th>
                            </tr>
                            <tr class="d-flex justify-content-end">
                                <th class="col">delivery fee :</th>
                                <th class="col">RM25.89</th>
                            </tr>
                        </tfoot>
                    </div>
                </table>

                <div class="w-100 fixed-bottom CheckoutBar">
                    <div class="row align-items-center justify-content-center">
                        <h3 class="col-10 d-flex justify-content-end">Total Price: RM100.00</h3>
                        <input type="submit" class="col-2 p-4 text-center ChkoutBtn" name="W-100 chekout" value="Check Out">
                    </div>
                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
