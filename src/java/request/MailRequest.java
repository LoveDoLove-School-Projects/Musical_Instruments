package request;

public class MailRequest {

    private String fromEmail;
    private String fromPassword;
    private String toEmail;
    private String subject;
    private String body;

    public MailRequest() {
    }

    public MailRequest(String fromEmail, String fromPassword) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
    }

    public MailRequest(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    public MailRequest(String fromEmail, String fromPassword, String toEmail, String subject, String body) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromPassword() {
        return fromPassword;
    }

    public void setFromPassword(String fromPassword) {
        this.fromPassword = fromPassword;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
