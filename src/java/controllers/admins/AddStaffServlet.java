package controllers.admins;

import common.Constants;
import entities.Staffs;
import exceptions.DatabaseException;
import features.AesProtector;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class AddStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(AddStaffServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/admins/addStaff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        Staffs staff = new Staffs(username, email, email, address, phoneNumber, gender); // Set default password as email
        LOG.info(staff.toString());
        if (!validateStaffDetails(staff)) {
            throw new IllegalArgumentException("Invalid staff details.");
        }
        try {
            userTransaction.begin();
            staff.setUsername(username);
            staff.setPassword(AesProtector.aes256EcbEncrypt(email));
            staff.setEmail(email);
            staff.setGender(gender);
            staff.setAddress(address);
            staff.setPhoneNumber(phoneNumber);
            staff.setTwoFactorAuth(false);
            staff.setAccountCreationDate(new Date());
            entityManager.persist(staff);
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException ex) {
            LOG.severe(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Staff Added successful!", Constants.ADMIN_SEARCH_STAFF_URL);
    }

    private boolean validateStaffDetails(Staffs staff) {
        if (StringUtilities.anyNullOrBlank(staff.getUsername(), staff.getPassword(), staff.getEmail(), staff.getGender(), staff.getAddress(), staff.getPhoneNumber())) {
            return false;
        }
        if (staff.getPassword().length() < 8) {
            return false;
        }
        if (!ValidationUtilities.isEmailValid(staff.getEmail())) {
            return false;
        }
        return ValidationUtilities.isPhoneNumberValid(staff.getPhoneNumber());
    }
}
