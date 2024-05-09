<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Billing Details</title>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <section class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-6">
                        <h2>Billing Details</h2>
                        <form action="pages/billingDetails" method="post" id="updateBillingDetailsForm">
                            <div class="form-group">

                                <div class="form-group row mb-3">
                                    <label for="email">Email</label>
                                    <div class="col-sm-9">
                                        <p class="text-muted mb-0">${email}</p>
                                    </div>
                                </div>


                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="country">Country</label>
                                        <select name="country" id="country" class="form-control" required>
                                            <option value="Malaysia">Malaysia</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="firstName">First Name</label>
                                        <input type="text" name="firstName" id="firstName" class="form-control" value="${firstName}" required />
                                    </div>
                                    <div class="col">
                                        <label for="lastName">Last Name</label>
                                        <input type="text" name="lastName" id="lastName" class="form-control" value="${lastName}" required />
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="address">Address</label>
                                        <input type="text" name="address" id="address" class="form-control" value="${address}" required />
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="city">City</label>
                                        <input type="text" name="city" id="city" class="form-control" value="${city}" required />
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="state">State</label>
                                        <select id="state" name="state" id="state" class="form-control" required>
                                            <option value="Johor" <c:if test="${state == 'Johor'}">selected</c:if>>Johor</option>
                                            <option value="Kedah" <c:if test="${state == 'Kedah'}">selected</c:if>>Kedah</option>
                                            <option value="Kelantan" <c:if test="${state == 'Kelantan'}">selected</c:if>>Kelantan</option>
                                            <option value="Kuala Lumpur" <c:if test="${state == 'Kuala Lumpur'}">selected</c:if>>Kuala Lumpur</option>
                                            <option value="Labuan" <c:if test="${state == 'Labuan'}">selected</c:if>>Labuan</option>
                                            <option value="Melaka" <c:if test="${state == 'Melaka'}">selected</c:if>>Melaka</option>
                                            <option value="Negeri Sembilan" <c:if test="${state == 'Negeri Sembilan'}">selected</c:if>>Negeri Sembilan</option>
                                            <option value="Pahang" <c:if test="${state == 'Pahang'}">selected</c:if>>Pahang</option>
                                            <option value="Penang" <c:if test="${state == 'Penang'}">selected</c:if>>Penang</option>
                                            <option value="Perak" <c:if test="${state == 'Perak'}">selected</c:if>>Perak</option>
                                            <option value="Perlis" <c:if test="${state == 'Perlis'}">selected</c:if>>Perlis</option>
                                            <option value="Putrajaya" <c:if test="${state == 'Putrajaya'}">selected</c:if>>Putrajaya</option>
                                            <option value="Sabah" <c:if test="${state == 'Sabah'}">selected</c:if>>Sabah</option>
                                            <option value="Sarawak" <c:if test="${state == 'Sarawak'}">selected</c:if>>Sarawak</option>
                                            <option value="Selangor" <c:if test="${state == 'Selangor'}">selected</c:if>>Selangor</option>
                                            <option value="Terengganu" <c:if test="${state == 'Terengganu'}">selected</c:if>>Terengganu</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group row mb-3">
                                        <div class="col">
                                            <label for="zipCode">Zip Code</label>
                                            <input type="text" name="zipCode" id="zipCode" class="form-control" value="${zipCode}" maxlength="5"  pattern="[0-9]+" required />
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <label for="phone_number">Phone</label>
                                        <input type="text" name="phone_number" id="phone_number" class="form-control" value="${phone_number}"  pattern="[0-9]+" required />
                                    </div>
                                </div>

                                <div class="form-group row mb-3">
                                    <div class="col">
                                        <input type="submit" class="btn btn-primary" value="Update Billing Details" />
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/billingDetails.js"></script>
    </body>

</html>