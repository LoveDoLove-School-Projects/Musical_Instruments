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
        <section class="section">
            <h2 class="text-center">Checkout</h2>
            <table class="table">
                <thead class="text-center TitleBar">
                    <tr class="row justify-content-center align-content-center">
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">No.</th>
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Image</th>
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Name</th>
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Color</th>
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Quantity</th>
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Price</th>
                        <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Total Price</th>
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

        <section class="py-5">
            <div class="container justify-content-center align-items-center">
                <div class="row">
                    <div class="col-md-6">
                        <h2>Billing Details</h2>
                        <form action="pages/billingDetails" method="post" id="updateBillingDetailsForm">
                            <div class="form-group">

                                <div class="form-group row mb-3">
                                    <label for="email">Email: ${email}</label>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="country">Country: ${country}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="firstName">First Name: ${firstName}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="lastName">Last Name: ${lastName}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="address">Address: ${address}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="city">City: ${city}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="state">State: ${state}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="zipCode">Zip Code: ${zipCode}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="phone_number">Phone: ${phone_number}</label>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <a href="pages/billingDetails" class="btn btn-primary">Update Billing Details</a>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>

        <section class="py-5">
            <div class="container justify-content-center align-items-center">
                <div class="row">
                    <div class="col-md-6">
                        <form action="payments/transaction" method="post">
                            <h2>Select Payment Method</h2>
                            <div class="form-group">
                                <label for="paymentMethod">Payment Method</label>
                                <select class="form-control" id="paymentMethod" name="paymentMethod" required>
                                    <option value="Paypal" selected>PayPal</option>
                                    <option value="CreditOrDebitCard">Credit / Debit Card</option>
                                </select>
                            </div>
                            <div id="cardDetails" style="display: none;">
                                <div class="form-group">
                                    <label for="cardHolderName">Card Holder Name</label>
                                    <input type="text" class="form-control" id="cardHolderName" name="cardHolderName" />
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
                                    <input type="text" name="cvv" class="form-control" id="cvv" maxlength="3" pattern="[0-9]{3}" title="3 digits" size="3" />
                                </div>
                                <div class="form-group row">
                                    <label for="expDate" class="col-form-label">Expiration Date</label>
                                    <div class="col">
                                        <select class="form-control" id="expYear" name="expYear">
                                            <option value="">Year</option>
                                            <c:forEach var="year" begin="<%= Calendar.getInstance().get(Calendar.YEAR) %>" end="<%= Calendar.getInstance().get(Calendar.YEAR) + 30 %>">
                                                <option value="${year}">${year}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col">
                                        <select class="form-control" id="expMonth" name="expMonth">
                                            <option value="">Month</option>
                                            <c:forEach var="month" begin="1" end="12">
                                                <option value="${month}">${month}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <button type="submit" class="btn btn-primary mt-3" id="paymentButton">Proceed To Checkout</button>
                                </div>
                                <div class="col">
                                    <a href="pages/cart" class="btn btn-primary mt-3">Go back</a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <script>
            const paymentMethod = ['CreditOrDebitCard', 'Paypal'];
            $(document).ready(function () {
                $('#paymentMethod').change(function () {
                    if ($(this).val() === paymentMethod[0]) {
                        $('#cardDetails').show();
                    } else {
                        $('#cardDetails').hide();
                    }
                });
                $('#paymentButton').click(function () {
                    if ($('#paymentMethod').val() !== paymentMethod[0]) {
                        $('#cardHolderName').val('');
                        $('#card1').val('');
                        $('#card2').val('');
                        $('#card3').val('');
                        $('#card4').val('');
                        $('#cvv').val('');
                        $('#expYear').val('');
                        $('#expMonth').val('');
                    }
                });
            });
        </script>
    </body>

</html>