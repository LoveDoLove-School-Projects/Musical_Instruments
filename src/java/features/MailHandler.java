package features;

import domain.common.Common;
import domain.common.Enviroment;
import domain.request.MailRequest;
import utilities.HttpUtilities;

public class MailHandler {

    private static final String SEND_MAIL_API = AesHandler.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public Common.Status sendEmail(MailRequest mailRequest) {
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, mailRequest);
    }

    public Common.Status sendEmail(String email, String subject, String body) {
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, body);
        return HttpUtilities.sendHttpJsonRequest(SEND_MAIL_API, jsonPayload);
    }
}
