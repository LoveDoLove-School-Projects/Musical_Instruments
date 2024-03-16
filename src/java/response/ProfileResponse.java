package response;

import models.Customer;

public class ProfileResponse extends DefaultResponse {

    private Customer customer;

    public ProfileResponse() {
    }

    public ProfileResponse(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
