package controllers.admins;

import entities.Staffs;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class AddStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String phoneNumber = request.getParameter("phoneNumber");

            Staffs staff = new Staffs();

            if (!validateStaffDetails(username, password, email, gender, address, phoneNumber)) {
                throw new IllegalArgumentException("Invalid staff details.");
            }

            try {
                userTransaction.begin();
            } catch (NotSupportedException ex) {
                Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            staff.setUsername(username);
            staff.setPassword(password);
            staff.setEmail(email);
            staff.setGender(gender);
            staff.setAddress(address);
            staff.setPhoneNumber(phoneNumber);
            staff.setTwoFactorAuth(false);
            staff.setAccountCreationDate(new Date());
            entityManager.persist(staff);
            userTransaction.commit();

            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Staff Added successful!", "/pages/admins/searchStaff.jsp");
        } catch (RollbackException ex) {
            Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(AddStaffServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validateStaffDetails(String username, String password, String email, String gender, String address, String phoneNumber) {
        if (StringUtilities.anyNullOrBlank(username, password, email, gender, address, phoneNumber)) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        if (!ValidationUtilities.isEmailValid(email)) {
            return false;
        }
        return ValidationUtilities.isPhoneNumberValid(phoneNumber);
    }
}
