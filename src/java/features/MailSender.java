package features;

import entities.Mail;
import enviroments.Enviroment;
import utilities.HttpUtilities;

public class MailSender {

    private static final String SEND_MAIL_API = AesProtector.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public boolean sendEmail(Mail mail) {
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, mail);
    }

    public boolean sendEmail(String email, String subject, String body) {
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, body);
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, jsonPayload);
    }
}
