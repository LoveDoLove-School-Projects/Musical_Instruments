package controllers;

import domain.common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleTwoStepVerification(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleTwoStepVerification(request, response);
    }

    private void setSessionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.VERIFIED_SESSION_JSP_URL).forward(request, response);
    }

    private void handleTwoStepVerification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String path = request.getServletPath();
            switch (path) {
            }
        }
        setSessionPage(request, response);
    }

}
