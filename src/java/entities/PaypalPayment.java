package entities;

import java.util.ArrayList;
import java.util.Date;

public class PaypalPayment {

    public String id;
    public String intent;
    public String state;
    public String cart;
    public Payer payer;
    public ArrayList<Transaction> transactions;
    public ArrayList<Object> failed_transactions;
    public RedirectUrls redirect_urls;
    public Date create_time;
    public Date update_time;
    public ArrayList<Link> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public RedirectUrls getRedirect_urls() {
        return redirect_urls;
    }

    public void setRedirect_urls(RedirectUrls redirect_urls) {
        this.redirect_urls = redirect_urls;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(id).append("\",");
        sb.append("\"intent\":\"").append(intent).append("\",");
        sb.append("\"state\":\"").append(state).append("\",");
        sb.append("\"cart\":\"").append(cart).append("\",");
        sb.append("\"payer\":").append(payer.toJSON()).append(",");
        sb.append("\"transactions\":[");
        for (int i = 0; i < transactions.size(); i++) {
            sb.append(transactions.get(i).toJSON());
            if (i < transactions.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("],");
        sb.append("\"redirect_urls\":").append(redirect_urls.toJSON()).append(",");
        sb.append("\"create_time\":\"").append(create_time).append("\",");
        sb.append("\"update_time\":\"").append(update_time).append("\",");
        sb.append("\"links\":[");
        for (int i = 0; i < links.size(); i++) {
            sb.append(links.get(i).toJSON());
            if (i < links.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    public class Amount {

        public String total;
        public String currency;
        public Details details;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"total\":\"").append(total).append("\",");
            sb.append("\"currency\":\"").append(currency).append("\",");
            sb.append("\"details\":").append(details.toJSON());
            sb.append("}");
            return sb.toString();
        }
    }

    public class Details {

        public String subtotal;
        public String tax;
        public String shipping;

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getShipping() {
            return shipping;
        }

        public void setShipping(String shipping) {
            this.shipping = shipping;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"subtotal\":\"").append(subtotal).append("\",");
            sb.append("\"tax\":\"").append(tax).append("\",");
            sb.append("\"shipping\":\"").append(shipping).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class Item {

        public String name;
        public String price;
        public String currency;
        public String tax;
        public int quantity;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"name\":\"").append(name).append("\",");
            sb.append("\"price\":\"").append(price).append("\",");
            sb.append("\"currency\":\"").append(currency).append("\",");
            sb.append("\"tax\":\"").append(tax).append("\",");
            sb.append("\"quantity\":").append(quantity);
            sb.append("}");
            return sb.toString();
        }
    }

    public class ItemList {

        public ArrayList<Item> items;
        public ShippingAddress shipping_address;

        public ArrayList<Item> getItems() {
            return items;
        }

        public void setItems(ArrayList<Item> items) {
            this.items = items;
        }

        public ShippingAddress getShipping_address() {
            return shipping_address;
        }

        public void setShipping_address(ShippingAddress shipping_address) {
            this.shipping_address = shipping_address;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"items\":[");
            for (int i = 0; i < items.size(); i++) {
                sb.append(items.get(i).toJSON());
                if (i < items.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("],");
            sb.append("\"shipping_address\":").append(shipping_address.toJSON());
            sb.append("}");
            return sb.toString();
        }
    }

    public class Link {

        public String href;
        public String rel;
        public String method;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getRel() {
            return rel;
        }

        public void setRel(String rel) {
            this.rel = rel;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"href\":\"").append(href).append("\",");
            sb.append("\"rel\":\"").append(rel).append("\",");
            sb.append("\"method\":\"").append(method).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class Payee {

        public String merchant_id;
        public String email;

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"merchant_id\":\"").append(merchant_id).append("\",");
            sb.append("\"email\":\"").append(email).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class Payer {

        public String payment_method;
        public String status;
        public PayerInfo payer_info;

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public PayerInfo getPayer_info() {
            return payer_info;
        }

        public void setPayer_info(PayerInfo payer_info) {
            this.payer_info = payer_info;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"payment_method\":\"").append(payment_method).append("\",");
            sb.append("\"status\":\"").append(status).append("\",");
            sb.append("\"payer_info\":").append(payer_info.toJSON());
            sb.append("}");
            return sb.toString();
        }
    }

    public class PayerInfo {

        public String email;
        public String first_name;
        public String last_name;
        public String payer_id;
        public ShippingAddress shipping_address;
        public String country_code;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getPayer_id() {
            return payer_id;
        }

        public void setPayer_id(String payer_id) {
            this.payer_id = payer_id;
        }

        public ShippingAddress getShipping_address() {
            return shipping_address;
        }

        public void setShipping_address(ShippingAddress shipping_address) {
            this.shipping_address = shipping_address;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"email\":\"").append(email).append("\",");
            sb.append("\"first_name\":\"").append(first_name).append("\",");
            sb.append("\"last_name\":\"").append(last_name).append("\",");
            sb.append("\"payer_id\":\"").append(payer_id).append("\",");
            sb.append("\"shipping_address\":").append(shipping_address.toJSON()).append(",");
            sb.append("\"country_code\":\"").append(country_code).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class RedirectUrls {

        public String return_url;
        public String cancel_url;

        public String getReturn_url() {
            return return_url;
        }

        public void setReturn_url(String return_url) {
            this.return_url = return_url;
        }

        public String getCancel_url() {
            return cancel_url;
        }

        public void setCancel_url(String cancel_url) {
            this.cancel_url = cancel_url;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"return_url\":\"").append(return_url).append("\",");
            sb.append("\"cancel_url\":\"").append(cancel_url).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class ShippingAddress {

        public String recipient_name;
        public String line1;
        public String city;
        public String state;
        public String postal_code;
        public String country_code;

        public String getRecipient_name() {
            return recipient_name;
        }

        public void setRecipient_name(String recipient_name) {
            this.recipient_name = recipient_name;
        }

        public String getLine1() {
            return line1;
        }

        public void setLine1(String line1) {
            this.line1 = line1;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostal_code() {
            return postal_code;
        }

        public void setPostal_code(String postal_code) {
            this.postal_code = postal_code;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"recipient_name\":\"").append(recipient_name).append("\",");
            sb.append("\"line1\":\"").append(line1).append("\",");
            sb.append("\"city\":\"").append(city).append("\",");
            sb.append("\"state\":\"").append(state).append("\",");
            sb.append("\"postal_code\":\"").append(postal_code).append("\",");
            sb.append("\"country_code\":\"").append(country_code).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class Transaction {

        public Amount amount;
        public Payee payee;
        public String description;
        public ItemList item_list;
        public ArrayList<RelatedResource> related_resources;

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Payee getPayee() {
            return payee;
        }

        public void setPayee(Payee payee) {
            this.payee = payee;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ItemList getItem_list() {
            return item_list;
        }

        public void setItem_list(ItemList item_list) {
            this.item_list = item_list;
        }

        public ArrayList<RelatedResource> getRelated_resources() {
            return related_resources;
        }

        public void setRelated_resources(ArrayList<RelatedResource> related_resources) {
            this.related_resources = related_resources;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"amount\":").append(amount.toJSON()).append(",");
            sb.append("\"payee\":").append(payee.toJSON()).append(",");
            sb.append("\"description\":\"").append(description).append("\",");
            sb.append("\"item_list\":").append(item_list.toJSON()).append(",");
            sb.append("\"related_resources\":").append(related_resources);
            sb.append("}");
            return sb.toString();
        }
    }

    public class RelatedResource {

        public Sale sale;

        public Sale getSale() {
            return sale;
        }

        public void setSale(Sale sale) {
            this.sale = sale;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"sale\":").append(sale.toJSON());
            sb.append("}");
            return sb.toString();
        }
    }

    public class Sale {

        public String id;
        public String state;
        public Amount amount;
        public String payment_mode;
        public String reason_code;
        public String protection_eligibility;
        public TransactionFee transaction_fee;
        public String parent_payment;
        public Date create_time;
        public Date update_time;
        public ArrayList<Link> links;
        public String soft_descriptor;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public String getPayment_mode() {
            return payment_mode;
        }

        public void setPayment_mode(String payment_mode) {
            this.payment_mode = payment_mode;
        }

        public String getReason_code() {
            return reason_code;
        }

        public void setReason_code(String reason_code) {
            this.reason_code = reason_code;
        }

        public String getProtection_eligibility() {
            return protection_eligibility;
        }

        public void setProtection_eligibility(String protection_eligibility) {
            this.protection_eligibility = protection_eligibility;
        }

        public TransactionFee getTransaction_fee() {
            return transaction_fee;
        }

        public void setTransaction_fee(TransactionFee transaction_fee) {
            this.transaction_fee = transaction_fee;
        }

        public String getParent_payment() {
            return parent_payment;
        }

        public void setParent_payment(String parent_payment) {
            this.parent_payment = parent_payment;
        }

        public Date getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Date create_time) {
            this.create_time = create_time;
        }

        public Date getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Date update_time) {
            this.update_time = update_time;
        }

        public ArrayList<Link> getLinks() {
            return links;
        }

        public void setLinks(ArrayList<Link> links) {
            this.links = links;
        }

        public String getSoft_descriptor() {
            return soft_descriptor;
        }

        public void setSoft_descriptor(String soft_descriptor) {
            this.soft_descriptor = soft_descriptor;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"id\":\"").append(id).append("\",");
            sb.append("\"state\":\"").append(state).append("\",");
            sb.append("\"amount\":").append(amount.toJSON()).append(",");
            sb.append("\"payment_mode\":\"").append(payment_mode).append("\",");
            sb.append("\"reason_code\":\"").append(reason_code).append("\",");
            sb.append("\"protection_eligibility\":\"").append(protection_eligibility).append("\",");
            sb.append("\"transaction_fee\":").append(transaction_fee.toJSON()).append(",");
            sb.append("\"parent_payment\":\"").append(parent_payment).append("\",");
            sb.append("\"create_time\":\"").append(create_time).append("\",");
            sb.append("\"update_time\":\"").append(update_time).append("\",");
            sb.append("\"links\":[");
            for (int i = 0; i < links.size(); i++) {
                sb.append(links.get(i).toJSON());
                if (i < links.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("],");
            sb.append("\"soft_descriptor\":\"").append(soft_descriptor).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public class TransactionFee {

        public String value;
        public String currency;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"value\":\"").append(value).append("\",");
            sb.append("\"currency\":\"").append(currency).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }
}
