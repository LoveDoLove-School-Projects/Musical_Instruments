package controllers.staffs;

import entities.Transactions;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import utilities.RedirectUtilities;

public class ViewTransactionServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionChecker.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
            return;
        }
        List<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findAll", Transactions.class).getResultList();
        Collections.reverse(transactionList);
        request.setAttribute("transactionList", transactionList);
        request.getRequestDispatcher("/pages/staffs/viewTransaction.jsp").forward(request, response);
    }
}
