package controllers;

import domain.common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;
import utilities.StringUtilities;

public class OtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleOtp(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleOtp(request, response);
    }

    private void setOtpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (StringUtilities.anyNullOrBlank(email)) {
            RedirectUtilities.redirectWithError(request, response, "Invalid request!", Constants.MAIN_URL);
            return;
        }
        request.getRequestDispatcher(Constants.OTP_JSP_URL).forward(request, response);
    }

    private void handleOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            switch (path) {
                case Constants.OTP_URL:
                    verifyOtp(request, response);
                    return;
            }
        }
        setOtpPage(request, response);
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        request.setAttribute("email", email);

        if (StringUtilities.anyNullOrBlank(email, otp)) {
            RedirectUtilities.setErrorMessage(request, "Email and OTP are required!");
            setOtpPage(request, response);
            return;
        }

        setOtpPage(request, response);
    }
}
