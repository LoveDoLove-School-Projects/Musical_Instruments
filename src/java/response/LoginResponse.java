package response;

import common.Common;
import models.Customer;

public class LoginResponse {

    private common.Common.Status status;

    private Customer customer;

    public LoginResponse() {
    }

    public LoginResponse(Common.Status status, Customer customer) {
        this.status = status;
        this.customer = customer;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
