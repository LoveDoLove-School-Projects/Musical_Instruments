<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Review</title>
        <style type="text/css">
            table {
                border: 0;
            }
            table td {
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <div align="center">
            <h1>Please Review Before Paying</h1>
            <form action="payments/paypal/execute" method="post">
                <table>
                    <tr>
                        <td colspan="2"><b>Transaction Details:</b></td>
                        <td>
                            <input type="hidden" name="paymentId" value="${param.paymentId}" />
                            <input type="hidden" name="PayerID" value="${param.PayerID}" />
                        </td>
                    </tr>
                    <tr>
                        <td>Description:</td>
                        <td>${description}</td>
                    </tr>
                    <tr>
                        <td>Subtotal:</td>
                        <td>${subtotal} MYR</td>
                    </tr>
                    <tr>
                        <td>Shipping:</td>
                        <td>${shipping} MYR</td>
                    </tr>
                    <tr>
                        <td>Tax:</td>
                        <td>${tax} MYR</td>
                    </tr>
                    <tr>
                        <td>Total:</td>
                        <td>${total} MYR</td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td colspan="2"><b>Payer Information:</b></td>
                    </tr>
                    <tr>
                        <td>First Name:</td>
                        <td>${firstName}</td>
                    </tr>
                    <tr>
                        <td>Last Name:</td>
                        <td>${lastName}</td>
                    </tr>
                    <tr>
                        <td>Email:</td>
                        <td>${email}</td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td colspan="2"><b>Shipping Address:</b></td>
                    </tr>
                    <tr>
                        <td>Recipient Name:</td>
                        <td>${recipientName}</td>
                    </tr>
                    <tr>
                        <td>Line 1:</td>
                        <td>${line1}</td>
                    </tr>
                    <tr>
                        <td>City:</td>
                        <td>${city}</td>
                    </tr>
                    <tr>
                        <td>State:</td>
                        <td>${state}</td>
                    </tr>
                    <tr>
                        <td>Country Code:</td>
                        <td>${countryCode}</td>
                    </tr>
                    <tr>
                        <td>Postal Code:</td>
                        <td>${postalCode}</td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="submit" value="Pay Now" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>