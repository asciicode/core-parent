package nz.co.logicons.tlp.core.spring.web;

import java.util.Map;

public class ErrorResponse {

    private String errorMessage;

    private String errorCode;

    private Map<String, Object> errorFields;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getErrorFields() {
        return errorFields;
    }

    public void setErrorFields(Map<String, Object> errorFields) {
        this.errorFields = errorFields;
    }
}
