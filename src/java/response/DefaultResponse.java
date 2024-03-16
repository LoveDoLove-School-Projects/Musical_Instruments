package response;

import common.Common;

public class DefaultResponse {

    protected Common.Status status;

    public DefaultResponse() {
    }

    public DefaultResponse(Common.Status status) {
        this.status = status;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }

}
