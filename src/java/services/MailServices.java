package services;

import domain.common.Common;
import domain.common.Enviroment;
import domain.request.MailRequest;
import domain.response.DefaultResponse;
import domain.response.MailResponse;
import utilities.AesUtilities;
import utilities.HttpUtilities;

public class MailServices {

    private final String secretKey = AesUtilities.aes256EcbDecrypt(Enviroment.SECRET_KEY);

    private final String sendMailApi = AesUtilities.aes256EcbDecrypt(Enviroment.SEND_MAIL_API);

    public MailResponse sendEmail(MailRequest mailRequest) {
        try {
            mailRequest.setSecretKey(secretKey);
            DefaultResponse defaultResponse = HttpUtilities.sendHttpJsonRequest(sendMailApi, mailRequest);
            return new MailResponse(defaultResponse.getStatus());
        } catch (Exception ex) {
            return new MailResponse(Common.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
