package com.biantech.ssmd.es;

public class ESMappingBuilder {
   public static final String FIELD_PROPERTIES = "properties";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_FORMAT ="format";

    public XContentBuilder buildRankScoreMapping() throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject();// .startObject(indexType);
        XContentBuilder builder = mapping.startObject(FIELD_PROPERTIES);
        buildRankScoreInternal(builder);
        mapping.endObject();
        return mapping.endObject();
    }


    public XContentBuilder buildRankScoreDetailMapping() throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject();// .startObject(indexType);
        XContentBuilder builder = mapping.startObject(FIELD_PROPERTIES);
        buildRankScoreInternal(builder);
        buildRankScoreDetailInternal(builder);
        mapping.endObject();
        return mapping.endObject();
    }

    private void buildRankScoreInternal(XContentBuilder builder) throws IOException {
        builder.startObject("id");
        builder.field(FIELD_TYPE, ESConstants.TYPE_VALUE_KEYWORD);
        builder.endObject();
        builder.startObject("bizGroup");
        builder.field(FIELD_TYPE, ESConstants.TYPE_VALUE_KEYWORD);
        builder.endObject();
        builder.startObject("sourceAppId");
        builder.field(FIELD_TYPE, ESConstants.FIELD_TYPE_INT);
        builder.endObject();
        builder.startObject("defineId");
        builder.field(FIELD_TYPE, ESConstants.TYPE_VALUE_KEYWORD);
        builder.endObject();
        builder.startObject("score");
        builder.field(FIELD_TYPE, ESConstants.FIELD_TYPE_DOUBLE);
        builder.endObject();
        builder.startObject("score2");
        builder.field(FIELD_TYPE, ESConstants.FIELD_TYPE_DOUBLE);
        builder.endObject();
        builder.startObject("groupExpireTime");
        builder.field(FIELD_TYPE, ESConstants.FIELD_TYPE_DATE);
        //builder.field(FIELD_FORMAT, "yyyy-MM-dd HH:mm:ss");
        builder.field(FIELD_FORMAT,"date_hour_minute_second");
        builder.endObject();
    }

    private void buildRankScoreDetailInternal(XContentBuilder builder) throws IOException {
        builder.startObject("rankVersion");
        builder.field(FIELD_TYPE,ESConstants.FIELD_TYPE_INT);
        builder.endObject();
        builder.startObject("rowId");
        builder.field(FIELD_TYPE,ESConstants.TYPE_VALUE_KEYWORD);
        builder.endObject();
    }
}
