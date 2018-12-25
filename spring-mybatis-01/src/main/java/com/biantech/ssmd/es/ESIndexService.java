package com.biantech.ssmd.es;

public class ESIndexBuilder {
    private Logger logger = LoggerFactory.getLogger(ESIndexBuilder.class);
    @Autowired
    private DefaultESRepository defaultESRepository;

    @Autowired
    private ESMappingBuilder esMappingBuilder;

    public ESResponse createRankIndex(String indexName, String indexCatalog) {
        ESResponse esResponse = new ESResponse();
        try {
            if (ESConstants.IndexCatalog_V1.equalsIgnoreCase(indexCatalog) || StringUtils.isBlank(indexCatalog)) {
                XContentBuilder xContentBuilder = esMappingBuilder.buildRankScoreMapping();
                esResponse = defaultESRepository.createIndexMapping(indexName, null, xContentBuilder);
            }
        } catch (Exception ex) {
            logger.warn("createIndexFailed", ex);
            esResponse.setSucceeded(false);
            //esResponse.setCause(ex);
        }
        return esResponse;
    }

    public ESResponse createRankIndexDetail(String indexNameDetail, String indexCatalog) {
        ESResponse esResponse = new ESResponse();
        try {
            if (ESConstants.IndexCatalog_V1.equalsIgnoreCase(indexCatalog) || StringUtils.isBlank(indexCatalog)) {
                XContentBuilder xContentBuilderDetail = esMappingBuilder.buildRankScoreDetailMapping();
                esResponse = defaultESRepository.createIndexMapping(indexNameDetail, null, xContentBuilderDetail);
            }
        } catch (Exception ex) {
            logger.warn("createIndexFailed", ex);
            esResponse.setSucceeded(false);
            //esResponse.setCause(ex);
        }
        return esResponse;
    }

}
