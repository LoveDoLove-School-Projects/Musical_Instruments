package features;

import entities.Environment;
import java.util.Optional;
import utilities.HttpUtilities;

public final class MailSender {

    private static final String SEND_MAIL_API = AesProtector.aes256EcbDecrypt(Environment.SEND_MAIL_API);

    public static boolean sendEmail(String email, String subject, String body) {
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, body);
        return Optional.ofNullable(HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, jsonPayload)).isPresent();
    }
}
