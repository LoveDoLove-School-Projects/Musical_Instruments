<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="entities.Carts" %>
<%@ page import="java.util.Base64"%>
<%
response.setHeader("Cache-Control", "no-store");
%>
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Checkout</title>
    </head>

    <body>
        <h2 class="text-center">Checkout</h2>
        <section class="section">
            <table class="table">
                <thead class="text-center TitleBar">
                    <tr class="row justify-content-center align-content-center">
                        <th class="col p-3 Title">No.</th>
                        <th class="col p-3 Title">Image</th>
                        <th class="col p-3 Title">Product Name</th>
                        <th class="col p-3 Title">Product Color</th>
                        <th class="col p-3 Title">Quantity</th>
                        <th class="col p-3 Title">Price</th>
                        <th class="col p-3 Title">Total Price</th>
                    </tr>
                </thead>

                <tbody class="text-center">
                    <%
                        int numberCart=0;
                        double subTotal=0;
                        List<Carts> cartsDetails = (List<Carts>) request.getAttribute("cartList");
                        for (Carts carts : cartsDetails) {
                        numberCart++;
                        subTotal+=carts.getProductTotalprice();
                        String pictureBase64 = Base64.getEncoder().encodeToString(carts.getProductImage());
                        String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                    %>
                    <tr class="row justify-content-center">

                        <th class="col"><%=numberCart%></th>
                        <th class="col"><img src="<%=imageSrc%>" class="img-fluid w-50 mx-auto"></th>
                        <th class="col "><%=carts.getProductName()%></th>
                        <th class="col"><%=carts.getProductColor()%></th>
                        <th class="col"><%=carts.getProductQuantity()%></th>
                        <th class="col">RM<%=carts.getProductPrice()%></th>
                        <th class="col">RM<%=carts.getProductTotalprice()%></th>
                    </tr>
                    <%
                        }
                    %>
                </tbody>

                <div class="row justify-content-center align-items-center">
                    <tfoot class="col-12 text-center" style="float:right;">
                        <tr class="row">
                            <th class="col-10 d-flex justify-content-end">Subtotal :</th>
                            <th class="col-2">RM ${subtotal}</th>
                        </tr>
                        <tr class="row">
                            <th class="col-10 d-flex justify-content-end">Shipping fee :</th>
                            <th class="col-2">RM ${shipping}</th>
                        </tr>
                        <tr class="row">
                            <th class="col-10 d-flex justify-content-end">Tax 10% :</th>
                            <th class="col-2">RM ${tax}</th>
                        </tr>
                        <tr class="row">
                            <th class="col-10 d-flex justify-content-end">Total :</th>
                            <th class="col-2 ">RM ${total}</th>
                        </tr>
                    </tfoot>
                </div>
            </table>
        </section>

        <form action="payments/transaction" method="post">
            <section class="py-5">
                <div class="container justify-content-center align-items-center">
                    <div class="row">
                        <div class="col-md-6">

                            <h2>Billing Details</h2>
                            <div class="form-group">
                                <div class="form-group row">
                                    <div class="col">
                                        <label for="firstName">First Name</label><span style="color: red !important; display: inline; float: none;">*</span>
                                        <input type="text" class="form-control" value="${firstName}" />
                                    </div>
                                    <div class="col">
                                        <label for="lastName">Last Name</label><span style="color: red !important; display: inline; float: none;">*</span>
                                        <input type="text" class="form-control" value="${lastName}" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="country">Country / Region</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <select id="countries" name="countries" class="form-control"></select>
                                </div>

                                <div class="form-group row">
                                    <label for="streetAddress">Street Address</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <input type="text" name="streetAddress" class="form-control" value="${streetAddress}" />
                                </div>

                                <div class="form-group row">
                                    <label for="townCity">Town / City</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <input type="text" name="townCity" class="form-control" value="${townCity}" />
                                </div>

                                <div class="form-group row">
                                    <label for="state">State</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <select id="state" name="state" class="form-control"></select>
                                </div>

                                <div class="form-group row">
                                    <label for="zipCode">Zip Code</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <input type="text" name="zipCode" class="form-control" value="${zipCode}" />
                                </div>

                                <div class="form-group row">
                                    <label for="phone_number">Phone</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <input type="text" name="phone_number" class="form-control" value="${phone_number}" />
                                </div>

                                <div class="form-group row">
                                    <label for="email">Email</label><span style="color: red !important; display: inline; float: none;">*</span>
                                    <input type="email" name="email" class="form-control" value="${email}" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="py-5">
                <div class="container justify-content-center align-items-center">
                    <div class="row">
                        <div class="col-md-6">
                            <h2>Select Payment Method</h2>
                            <div class="form-group">
                                <label for="paymentMethod">Payment Method</label>
                                <select class="form-control" id="paymentMethod">
                                    <option value="Paypal" selected>PayPal</option>
                                    <option value="CreditOrDebitCard">Credit / Debit Card</option>
                                    <option value="CashOnDelivery">Cash On Delivery</option>
                                </select>
                            </div>
                            <div id="cardDetails" style="display: none;">
                                <div class="form-group">
                                    <label for="cardHolderName">Card Holder Name</label>
                                    <input type="text" class="form-control" id="cardHolderName" />
                                </div>
                                <div class="form-group row">
                                    <label for="cardNumber" class="col-form-label">Card Number</label>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card1" id="card1" maxlength="4" pattern="[0-9]{4}" title="First 4 digits" size="4" />
                                    </div>
                                    <div class="col-auto text-center my-auto">
                                        -
                                    </div>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card2" id="card2" maxlength="4" pattern="[0-9]{4}" title="Second 4 digits" size="4" />
                                    </div>
                                    <div class="col-auto text-center my-auto">
                                        -
                                    </div>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card3" id="card3" maxlength="4" pattern="[0-9]{4}" title="Third 4 digits" size="4" />
                                    </div>
                                    <div class="col-auto text-center my-auto">
                                        -
                                    </div>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card4" id="card4" maxlength="4" pattern="[0-9]{4}" title="Fourth 4 digits" size="4" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="cvv">CVV</label>
                                    <input type="text" name="CVV" class="form-control" id="CVV" />
                                </div>
                                <div class="form-group row">
                                    <label for="expDate" class="col-form-label">Expiration Date</label>
                                    <div class="col">
                                        <select class="form-control" id="expYear">
                                            <option value="">Year</option>
                                            <c:forEach var="year" begin="<%= Calendar.getInstance().get(Calendar.YEAR) %>" end="<%= Calendar.getInstance().get(Calendar.YEAR) + 30 %>">
                                                <option value="${year}">${year}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col">
                                        <select class="form-control" id="expMonth">
                                            <option value="">Month</option>
                                            <c:forEach var="month" begin="1" end="12">
                                                <option value="${month}">${month}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary mt-3" id="paymentButton">Proceed To Checkout</button>
                        </div>
                    </div>
                </div>
            </section>
        </form>
        <script>
            const paymentMethod = ['CreditOrDebitCard', 'Paypal', 'CashOnDelivery'];
            $(document).ready(function () {
                $('#paymentMethod').change(function () {
                    if ($(this).val() === paymentMethod[0]) {
                        $('#cardDetails').show();
                    } else {
                        $('#cardDetails').hide();
                    }
                });
            });
            $.ajax({
                url: 'https://lovedolove-restcountries-api.vercel.app/all',
                type: 'GET',
                success: function (data) {
                    data.sort(function (a, b) {
                        var countryA = a.name.common.toUpperCase();
                        var countryB = b.name.common.toUpperCase();
                        if (countryA < countryB) {
                            return -1;
                        }
                        if (countryA > countryB) {
                            return 1;
                        }
                        return 0;
                    });
                    data.forEach(function (country) {
                        $('#countries').append('<option value="' + country.name.common + '">' + country.name.common + '</option>');
                    });
                }
            });
        </script>
    </body>

</html>