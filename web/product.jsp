<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Page</title>
    </head>


    <body>
        <jsp:include page="message.jsp" />
        <main class="main">
            <section class="section1">
                <div class="container-fluid">
                    <div class="row justify-content-center align-items-center">
                        <div class="d-block my-auto p-5 col-12 col-xl-5">
                            <!--(h1)content-->
                            <!--(p)content-->
                            <button class="m-2 px-4 py-2 button"><strong>Shop now</strong></button>
                            <button class="m-2 px-4 py-2 button"><strong>Explore</strong></button>
                        </div>

                        <div class="col-12 col-xl-7">
                            <div class="m-3 mx-auto" style="animation:bounceInRight 2s">
                                <!--image-->
                            </div>
                        </div>
                    </div>
            </section>

            <section class="section2">
                <div class="container-fluid">
                    <div class="row justify-content-center align-items-center">
                        <div class="m-5 col-12 col-xl-6 col-xxl-5">
                            <div class="d-block p-5  sec2_content"  style="animation:bounce 2s">
                                <h1>Want to be member ?</h1><br>
                                <!--(p)write member benefit-->
                            </div>
                        </div>
                        <div class="col-12 col-xl-6 col-xxl-5 d-flex align-content-center justify-content-center">
                            <div class="m-5 p-5 d-block text-center PremiumBox">
                                <h1>Premium</h1>
                                <ul>
                                    <!--<li>write benefit</li>-->
                                    <!--<li>write benefit</li>-->
                                    <!--<li>write benefit</li>-->
                                    <!--<li>write benefit</li>-->
                                </ul>
                                <a href="#"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Login</strong></button></a>
                                <a href="#"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Sign up</strong></button></a>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section3">
                <div class="container-fluid">
                    <div class="row m-2 m-xxl-5 p-0 p-xxl-5 d-flex align-items-center justify-content-center">


                        <div class="col-12 col-xxl-12">
                            <div class="m-5 d-block text-center" style="animation:fadeInUp 3s">
                                <!--(h1)content-->
                                <!--(h5)content-->
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </main>
        <jsp:include page="foot.jsp" />
    </body>
</html>
