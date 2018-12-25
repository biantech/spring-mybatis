package com.biantech.ssmd.es;

import java.io.IOException;
import java.util.*;

import com.biantech.ssmd.utils.GsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;



/**
 * 官方api-es访问接口实现
 *
 * @author 
 * @date 2018/2/27 14:03
 */
public class DefaultESRepository implements InitializingBean{
    Logger logger = LoggerFactory.getLogger(DefaultESRepository.class);

    private TransportClient client;
    GsonUtils gsonUtils =new GsonUtils();

    @Override
    public void afterPropertiesSet() {
        //
        logger.info("init defaultES finished");
    }

    public TransportClient getClient() {
        if (client == null) {
            throw new RuntimeException("es-client can not be null");
        }
       return client;
    }

    public void setClient(TransportClient client) throws Exception {
        this.client = client;
    }

    public ESResponse createIndex(String indexName) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        CreateIndexRequestBuilder createIndexRequestBuilder = getClient().admin().indices().prepareCreate(indexName);
        if (logger.isDebugEnabled()) {
            logger.debug("createIndex,indexName:{}", indexName);
        }
        ESResponse esResponse = new ESResponse();
        try {
            CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
            esResponse.setSucceeded(createIndexResponse.isAcknowledged());
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("createIndex,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse deleteIndex(String indexName) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        DeleteIndexRequestBuilder deleteIndexRequestBuilder = getClient().admin().indices().prepareDelete(indexName);
        if (logger.isDebugEnabled()) {
            logger.debug("deleteIndex,indexName:{}", indexName);
        }
        ESResponse esResponse = new ESResponse();
        try {
            AcknowledgedResponse deleteIndexResponse = deleteIndexRequestBuilder.execute().actionGet();
            esResponse.setSucceeded(deleteIndexResponse.isAcknowledged());
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("deleteIndex,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }


    public ESResponse existIndex(String indexName) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(indexName);
        if (logger.isDebugEnabled()) {
            logger.debug("existIndex,indexName:{}", indexName);
        }
        ESResponse esResponse = new ESResponse();
        try {
            IndicesExistsResponse indicesExistsResponse = getClient().admin().indices().exists(indicesExistsRequest).actionGet();
            esResponse.setSucceeded(indicesExistsResponse.isExists());
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("existIndex,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public static final String FIELD_PROPERTIES = "properties";
    private static final String FIELD_TYPE="type";
    public XContentBuilder buildMapping(String indexType) throws IOException {
        //String json = builder.
        //XContentBuilder builder=
        //XContentBuilder source = XContentFactory.jsonBuilder().startObject(indexName).field()
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(indexType);
        XContentBuilder builder = mapping.startObject(FIELD_PROPERTIES);
        //mapping.startObject("appId");
        builder.startObject("appId");
        builder.field(FIELD_TYPE,ESConstants.FIELD_TYPE_INT);
        builder.endObject();
        builder.startObject("keyNo");
        builder.field(FIELD_TYPE,ESConstants.TYPE_VALUE_KEYWORD);
        builder.endObject();
        builder.startObject("score");
        builder.field(FIELD_TYPE,ESConstants.FIELD_TYPE_LONG);
        builder.endObject();
        builder.startObject("bizTag");
        builder.field(FIELD_TYPE,ESConstants.TYPE_VALUE_KEYWORD);
        builder.endObject();
        return mapping.endObject().endObject().endObject();
    }

    public ESResponse createIndexMapping(String indexName,String indexType,XContentBuilder builder){
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        CreateIndexRequestBuilder createIndexRequestBuilder= getClient().admin().indices().prepareCreate(indexName);
        CreateIndexResponse createIndexResponse=createIndexRequestBuilder.execute().actionGet();
        PutMappingRequestBuilder putMappingRequestBuilder = getClient().admin().indices().preparePutMapping(indexName);
        putMappingRequestBuilder.setSource(builder);
        putMappingRequestBuilder.setType(indexType);
        AcknowledgedResponse putMappingResponse = putMappingRequestBuilder.execute().actionGet();
        ESResponse esResponse = new ESResponse();
        esResponse.setSucceeded(putMappingResponse.isAcknowledged());
        return  esResponse;
    }

    public ESResponse createIndexMapping(String indexName, String typeName, String source) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        if (StringUtils.isBlank(source)) {
            throw new NullPointerException("source can not be null");
        }
        PutMappingRequestBuilder putMappingRequestBuilder = getClient().admin().indices().preparePutMapping(indexName);
        putMappingRequestBuilder.setType(typeName);
        putMappingRequestBuilder.setSource(source, XContentType.JSON);
        //putMappingRequestBuilder.setSource()
        if (logger.isDebugEnabled()) {
            logger.debug("createIndexMapping,indexName:{},typeName:{},source:{}", indexName, typeName, source);
        }
        ESResponse esResponse = new ESResponse();
        try {
            AcknowledgedResponse putMappingResponse = putMappingRequestBuilder.execute().actionGet();
            esResponse.setSucceeded(putMappingResponse.isAcknowledged());
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("existIndex,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse createIndexMapping(String indexName, String source) throws Exception {
        return createIndexMapping(indexName, indexName, source);
    }

    public ESResponse getIndexMapping(String indexName, String typeName) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        GetMappingsRequestBuilder getMappingsRequestBuilder = getClient().admin().indices().prepareGetMappings(indexName);
        getMappingsRequestBuilder.setTypes(typeName);
        if (logger.isDebugEnabled()) {
            logger.debug("getIndexMapping,indexName:{},typeName:{}", indexName, typeName);
        }
        ESResponse esResponse = new ESResponse();
        try {
            GetMappingsResponse getMappingsResponse = getMappingsRequestBuilder.execute().actionGet();
            ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = getMappingsResponse.getMappings();
            esResponse.setSucceeded(mappings != null ? true : false);
            Map<String, Object> source = new HashMap<>();
            Iterator<String> keysIt = mappings.keysIt();
            do {
                String key = keysIt.next();
                if (key != null) {
                    source.put(key, mappings.get(key));
                }
            } while (keysIt.hasNext());
            esResponse.setSource(source);
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getIndexMapping,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse getIndexMapping(String indexName) throws Exception {
        return getIndexMapping(indexName, indexName);
    }

    public ESResponse createDocument(String indexName, String typeName, List docs) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        if (CollectionUtils.isEmpty(docs)) {
            throw new NullPointerException("docs can not be null");
        }
        BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
        for (Object doc : docs) {
            bulkRequestBuilder.add(getClient().prepareIndex(indexName, typeName).setSource(gsonUtils.toJson(doc)));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("createDocument,Bulk:{}", bulkRequestBuilder.toString());
        }
        ESResponse esResponse = new ESResponse();
        try {
            BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            esResponse.setSucceeded(true);
            Map<String, Object> source = new HashMap<>();
            source.put("items", bulkResponse.getItems());
            esResponse.setSource(source);
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("createDocument,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse createDocument(String indexName, List docs) throws Exception {
        return createDocument(indexName, indexName, docs);
    }

    public ESResponse getDocumentById(String indexName, String typeName, String id) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        if (StringUtils.isBlank(id)) {
            throw new NullPointerException("id can not be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getDocumentById,indexName:{},typeName:{},id:{}", indexName, typeName, id);
        }
        ESResponse esResponse = new ESResponse();
        try {
            GetResponse getResponse = getClient().prepareGet(indexName, typeName, id).get();
            esResponse.setSucceeded(true);
            esResponse.setSource(getResponse.getSourceAsMap());
            esResponse.setResultAsJsonString(getResponse.getSourceAsString());
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getDocumentById,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse getDocumentById(String indexName, String id) throws Exception {
        return getDocumentById(indexName, indexName, id);
    }

    public ESResponse getDocuments(List<ESDocumentMeta> esDocumentMetas) throws Exception {
        if (esDocumentMetas == null || esDocumentMetas.size() == 0) {
            throw new NullPointerException("esDocumentMetas can not be null");
        }
        for (ESDocumentMeta esDocumentMeta : esDocumentMetas) {
            if (StringUtils.isBlank(esDocumentMeta.getIndex())) {
                throw new NullPointerException("indexName can not be null");
            }
            if (StringUtils.isBlank(esDocumentMeta.getType())) {
                throw new NullPointerException("typeName can not be null");
            }
            if (StringUtils.isBlank(esDocumentMeta.getId())) {
                throw new NullPointerException("id can not be null");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getDocuments,esDocumentMetas:{}", esDocumentMetas);
        }
        ESResponse esResponse = new ESResponse();
        try {
            MultiGetRequestBuilder multiGetRequestBuilder = getClient().prepareMultiGet();
            for (ESDocumentMeta esDocumentMeta : esDocumentMetas) {
                MultiGetRequest.Item item = new MultiGetRequest.Item(esDocumentMeta.getIndex(), esDocumentMeta.getType(), esDocumentMeta.getId());
                if(esDocumentMeta.getFieldIncludes()!=null && esDocumentMeta.getFieldIncludes().length>0){
                    item.fetchSourceContext(new FetchSourceContext(true,esDocumentMeta.getFieldIncludes(),null));
                }
                multiGetRequestBuilder.add(item);
            }
            MultiGetResponse responses = multiGetRequestBuilder.get();
            esResponse.setSucceeded(true);
            Map<String,Object> sourcesMap = new HashMap<>();
            List<Object> items = new ArrayList<>();
            sourcesMap.put("items",items);
            for (MultiGetItemResponse itemResponse : responses) {
                items.add(itemResponse);
            }
            esResponse.setSource(sourcesMap);
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getDocuments,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse deleteDocumentById(String indexName, String typeName, String id) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        if (StringUtils.isBlank(id)) {
            throw new NullPointerException("id can not be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("deleteDocumentById,indexName:{},typeName:{},id:{}", indexName, typeName, id);
        }
        ESResponse esResponse = new ESResponse();
        try {
            DeleteResponse deleteResponse = getClient().prepareDelete(indexName, typeName, id).get();
            esResponse.setSucceeded(true);
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("deleteDocumentById,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }


    public ESResponse deleteDocumentById(String indexName, String id) throws Exception {
        return deleteDocumentById(indexName, indexName, id);
    }


    public ESResponse searchDocument(String indexName, String typeName, String query) throws Exception {
        throw new RuntimeException("未实现");
    }


    public ESResponse searchDocument(String indexName, String query) throws Exception {
        throw new RuntimeException("未实现");
    }


    public ESResponse searchDocument(String indexName, String typeName, SearchSourceBuilder searchSourceBuilder) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        if (searchSourceBuilder == null) {
            throw new NullPointerException("searchSourceBuilder can not be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("searchDocument,indexName:{},typeName:{},searchSourceBuilder:{}", indexName, typeName, searchSourceBuilder.toString());
        }
        ESResponse esResponse = new ESResponse();
        try {
            SearchResponse searchResponse = getClient().prepareSearch(indexName).setTypes(typeName).setSource(searchSourceBuilder).get();
            esResponse.setSucceeded(true);
            Map<String, Object> source = new HashMap<>();
            source.put("items", searchResponse.getHits());
            esResponse.setSource(source);
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("searchDocument,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }


    public ESResponse searchDocument(String indexName, SearchSourceBuilder searchSourceBuilder) throws Exception {
        return searchDocument(indexName, indexName, searchSourceBuilder);
    }


    public ESResponse countDocument(String indexName, String typeName, String query) throws Exception {
        throw new RuntimeException("未实现");
    }


    public ESResponse countDocument(String indexName, String query) throws Exception {
        throw new RuntimeException("未实现");
    }

    public ESResponse countDocument(String indexName, String typeName, SearchSourceBuilder searchSourceBuilder) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            throw new NullPointerException("indexName can not be null");
        }
        if (StringUtils.isBlank(typeName)) {
            throw new NullPointerException("typeName can not be null");
        }
        if (searchSourceBuilder == null) {
            throw new NullPointerException("searchSourceBuilder can not be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("countDocument,indexName:{},typeName:{},searchSourceBuilder:{}", indexName, typeName, searchSourceBuilder.toString());
        }
        ESResponse esResponse = new ESResponse();
        try {
            SearchResponse searchResponse = getClient().prepareSearch(indexName).setTypes(typeName).setSource(searchSourceBuilder).get();
            esResponse.setSucceeded(true);
            Map<String, Object> source = new HashMap<>();
            source.put("count", searchResponse.getHits().getTotalHits());
            esResponse.setSource(source);
        } catch (Throwable e) {
            esResponse.setSucceeded(false);
            esResponse.setErrorType(e.getClass().getSimpleName());
            esResponse.setErrorMessage(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("countDocument,result status:{},result errormsg:{}", esResponse.isSucceeded(), esResponse.getErrorMessage());
        }
        return esResponse;
    }

    public ESResponse countDocument(String indexName, SearchSourceBuilder searchSourceBuilder) throws Exception {
        return countDocument(indexName, indexName, searchSourceBuilder);
    }

}
