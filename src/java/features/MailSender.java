package features;

import entities.Enviroment;
import java.util.Optional;
import utilities.HttpUtilities;

public class MailSender {

    private static final String SEND_MAIL_API = AesProtector.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public boolean sendEmail(String email, String subject, String body) {
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, body);
        return Optional.ofNullable(HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, jsonPayload)).isPresent();
    }
}
