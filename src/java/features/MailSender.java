package features;

import common.Common;
import enviroments.Enviroment;
import entities.Mail;
import utilities.HttpUtilities;

public class MailSender {

    private static final String SEND_MAIL_API = AesProtector.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public Common.Status sendEmail(Mail mail) {
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, mail);
    }

    public Common.Status sendEmail(String email, String subject, String body) {
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, body);
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, jsonPayload);
    }
}
