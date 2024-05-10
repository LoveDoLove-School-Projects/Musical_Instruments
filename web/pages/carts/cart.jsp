<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Carts" %>
<%@ page import="jakarta.persistence.EntityManager"%>
<%@ page import="entities.Products" %>
<%@ page import="java.lang.*" %>
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
                        <%
                             EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
                              List<Carts> cartsDetails = (List<Carts>) request.getAttribute("cartDetails");
                                
                             
                              for (Carts carts : cartsDetails) {
                              Products products = null;
                              try {
                                products = entityManager.createNamedQuery("Products.findByProductId", Products.class).setParameter("productId", carts.getProductId()).getSingleResult();
                                   } catch (Exception e) {
                                            // If no result found, skip to the next iteration
                                            continue;
                                   }
                                        if(products.getQuantity()!=0){
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
                            <th class="col">RM<%=String.format("%.2f",carts.getProductTotalprice())%></th>
                            <th class="col">
                                <div class="row align-content-center justify-content-center">
                                    <a href="pages/carts/editCart?cart_id=<%=carts.getCartId()%>" class="MusicInstruments row1-MusicInstruments1">
                                        <button type="button" class="w-50 mx-auto my-1 p-2 editBtn"><strong>Edit</strong></button>
                                    </a>
                                </div>
                                <form method="POST" action="pages/carts/deleteCartServlet" class="deleteCartForm">
                                    <input type="hidden" name="cartId" value="<%=carts.getCartId()%>"/>
                                    <button type="submit" class="w-50 mx-auto my-1 p-2 deleteBtn" id="deleteCartForm"><strong>Delete</strong></button>
                                </form>
                            </th>


                        </tr>
                    <script>
                        function setDeleteProductForms() {
                            let deleteCartForms = document.querySelectorAll(".deleteCartForm");
                            deleteCartForms.forEach(function (form) {
                                form.addEventListener("submit", function (event) {
                                    event.preventDefault();
                                    Swal.fire({
                                        title: "Are you sure you want to delete it?",
                                        text: "You won't be able to revert this!",
                                        icon: "warning",
                                        showCancelButton: true,
                                        confirmButtonColor: "#3085d6",
                                        cancelButtonColor: "#d33",
                                        confirmButtonText: "Yes",
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            form.submit();
                                        }
                                    });
                                });
                            });
                        }

                        setDeleteProductForms();
                    </script>



                    <%
                                        }else{
                    %>
                    <script>
                        let dynamicMessage = "Sorry , your product" + '<%= carts.getProductName() %>' + " has been sold out";
                        window.alert(dynamicMessage);
                    </script>

                    <%
                        }
                        }
                        
                    %>

                    </tbody>

                </table>
                <div class="text-center m-3">
                    <a href="pages/products" class="w-100 p-4 text-center"><button class="addProductBtn p-2">Add more product</button></a>
                </div>

                <div class="w-100 fixed-bottom CheckoutBar">
                    <div class="row align-items-center justify-content-center">
                        <h4 class="col-10 d-flex justify-content-end">Total : <strong class="chkOutPrice">  RM<%=String.format("%.2f",subTotal)%></strong></h4>
                        <form action="payments/checkout" method="post" class="col-2 text-center">
                            <input type="submit" class="w-100 p-4 text-center ChkoutBtn" name="chekout" value="Check Out"/>
                            <input type="hidden" name="subTotal" value="<%=subTotal%>"/>
                        </form>



                    </div>
                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />

    </body>
</html>
