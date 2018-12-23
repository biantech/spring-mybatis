package com.biantech.ssmd.es;

import java.util.Map;

/**
 * @author 
 * @date 2018/2/24 16:10
 */
public class ESResponse {

    private boolean isSucceeded;

    private String errorType;

    private String errorMessage;

    private Map<String, Object> source;

    private String resultAsJsonString;

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setSucceeded(boolean succeeded) {
        isSucceeded = succeeded;
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

    public Map<String, Object> getSource() {
        return source;
    }

    public void setSource(Map<String, Object> source) {
        this.source = source;
    }

    public String getResultAsJsonString() {
        return resultAsJsonString;
    }

    public void setResultAsJsonString(String resultAsJsonString) {
        this.resultAsJsonString = resultAsJsonString;
    }
}
