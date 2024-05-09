package controllers.admins;

import entities.Constants;
import entities.Staffs;
import exceptions.DatabaseException;
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
import utilities.AesUtilities;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class AddStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/superAdmin/addStaff.jsp").forward(request, response);
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
        if (!validateStaffDetails(staff)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Staff email address already existed!", "/pages/superAdmin/addStaff");
            return;
        }
        if (!addStaffToDb(staff)) {
            SecurityLog.addInternalSecurityLog(request, "Failed to add staff: " + staff.getUsername() + ".");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Error adding staff.", Constants.ADMIN_SEARCH_STAFF_URL);
            return;
        }
        SecurityLog.addInternalSecurityLog(request, "Staff: " + staff.getUsername() + " added successfully.");
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Staff Added successful!", Constants.ADMIN_SEARCH_STAFF_URL);
    }

    private boolean addStaffToDb(Staffs staff) {
        try {
            Staffs existingStaff = entityManager.createQuery("SELECT s FROM Staffs s WHERE s.email = :email", Staffs.class)
                    .setParameter("email", staff.getEmail())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (existingStaff != null) {
                return false;
            }

            userTransaction.begin();
            staff.setUsername(staff.getUsername());
            staff.setPassword(AesUtilities.aes256EcbEncrypt(staff.getEmail()));
            staff.setEmail(staff.getEmail());
            staff.setGender(staff.getGender());
            staff.setAddress(staff.getAddress());
            staff.setPhoneNumber(staff.getAddress());
            staff.setTwoFactorAuth(false);
            staff.setAccountCreationDate(new Date());
            entityManager.persist(staff);
            userTransaction.commit();
            return true;
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException ex) {
            throw new DatabaseException(ex.getMessage());
        }
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
        if (!ValidationUtilities.isPhoneNumberValid(staff.getPhoneNumber())) {
            return false;
        }
        Staffs existingStaff = entityManager.createQuery("SELECT s FROM Staffs s WHERE s.email = :email", Staffs.class)
                .setParameter("email", staff.getEmail())
                .getResultStream()
                .findFirst()
                .orElse(null);
        return existingStaff == null;
    }

}
