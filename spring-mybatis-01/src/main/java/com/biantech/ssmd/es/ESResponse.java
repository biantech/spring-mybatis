package com.biantech.ssmd.es;

import java.util.Map;

public class ESResponse {
    boolean succeeded;
    String errorType;
    String errorMessage;
    Map<String, Object> source;
    String resultAsJsonString;

    public String getResultAsJsonString() {
        return resultAsJsonString;
    }

    public void setResultAsJsonString(String resultAsJsonString) {
        this.resultAsJsonString = resultAsJsonString;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public void setSource(Map<String, Object> source) {
        this.source = source;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
