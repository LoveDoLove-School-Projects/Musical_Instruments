package controllers;

import domain.common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import services.AdminServices;

public class AdminServlet extends HttpServlet {

    private final AdminServices adminServices = new AdminServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case Constants.ADMIN_URL:
                viewAdminMainPage(request, response);
                return;
            case Constants.ADMIN_CONTROL_PANEL_URL:
                viewControlPanelPage(request, response);
                return;
            case Constants.ADMIN_MANAGE_CUSTOMER_URL:
                viewManageCustomerPage(request, response);
                return;
            case Constants.ADMIN_MANAGE_STAFF_URL:
                viewManageStaffPage(request, response);
                return;
            case Constants.ADMIN_MANAGE_STOCK_URL:
                viewManageStockPage(request, response);
                return;
            case Constants.ADMIN_SALES_URL:
                viewSalesPage(request, response);
                return;
            case Constants.ADMIN_VIEW_TRANSACTION_URL:
                viewTransactionPage(request, response);
        }
    }

    private void viewAdminMainPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_JSP_URL).forward(request, response);
    }

    private void viewControlPanelPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_CONTROL_PANEL_JSP_URL).forward(request, response);
    }

    private void viewManageCustomerPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_MANAGE_CUSTOMER_JSP_URL).forward(request, response);
    }

    private void viewManageStaffPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_MANAGE_STAFF_JSP_URL).forward(request, response);
    }

    private void viewManageStockPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_MANAGE_STOCK_JSP_URL).forward(request, response);
    }

    private void viewSalesPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_SALES_JSP_URL).forward(request, response);
    }

    private void viewTransactionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_VIEW_TRANSACTION_JSP_URL).forward(request, response);
    }
}
