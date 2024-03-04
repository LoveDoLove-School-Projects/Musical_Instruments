<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Login Page</title>
        <link rel="stylesheet" href="./css/simpleForm.css" />
    </head>
    <body>
    <center>
        <form method="POST" action="LoginServlet">
            <section class="vh-100 gradient-custom">
                <jsp:include page="header.jsp" />
                <div class="container py-5 h-100">
                    <div class="row justify-content-center align-items-center h-100">
                        <div class="col-12 col-lg-9 col-xl-7">
                            <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                <div class="card-body p-4 p-md-5">
                                    <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Login Form</h3>

                                    <div class="row">
                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <input type="email" name="email" class="form-control form-control-lg" />
                                                <label class="form-label" for="email">Email</label>
                                            </div>
                                        </div>

                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <input type="password" name="password" class="form-control form-control-lg" />
                                                <label class="form-label" for="password">Password</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mt-4 pt-2">
                                        <input class="btn btn-primary btn-lg" type="submit" value="Submit" />
                                    </div>

                                    <div class="mt-4 pt-2">
                                        <p>
                                            No Account?
                                            <a href="register.jsp">Register Here</a>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </form>
    </center>
    <jsp:include page="footer.jsp" />
</body>
</html>
