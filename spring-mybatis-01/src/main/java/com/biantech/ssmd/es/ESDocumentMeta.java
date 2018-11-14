package com.biantech.ssmd.es;


/**
 * @author 
 * @date 2018/3/14 10:22
 */
public class ESDocumentMeta {

    protected String index;//es index
    protected String type;//es type
    protected String id;//es id
    protected String version;//es version
    protected String[] fieldIncludes;//es fieldIncludes

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getFieldIncludes() {
        return fieldIncludes;
    }

    public void setFieldIncludes(String[] fieldIncludes) {
        this.fieldIncludes = fieldIncludes;
    }
}
