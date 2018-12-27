package com.biantech.ssmd.esdao;

import com.biantech.ssmd.es.DefaultESRepository;
import com.biantech.ssmd.es.ESResponse;
import com.biantech.ssmd.esdomain.RankScoreModel;
import com.biantech.ssmd.esdomain.UserAddressModel;
import com.biantech.ssmd.utils.GsonUtils;
import com.biantech.ssmd.utils.JsonUtil;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BIANJIANQUAN223
 * @date 2018/12/27
 */
@Component
public class UserAddressESDao {
    @Autowired
    DefaultESRepository defaultESRepository;

    public List<AnalyzeResponse.AnalyzeToken>  analyerAddress(String indexName,String analyzeText,String analyzerPattern){
        AnalyzeRequest analyzeRequest = new AnalyzeRequest(indexName).text(analyzeText).analyzer(analyzerPattern);
        List<AnalyzeResponse.AnalyzeToken> tokens = defaultESRepository.getClient().admin().indices()
                .analyze(analyzeRequest)
                .actionGet()
                .getTokens();
        //for (AnalyzeResponse.AnalyzeToken token : tokens) {
            //System.out.print(token.getTerm()+" ");
        //}
        return tokens;
    }

    public List<UserAddressModel>  searchAddressMatch(String indexName,String searchText){
        // 过滤条件
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("address1", searchText);
        MatchQueryBuilder matchQueryBuilder=QueryBuilders.matchQuery("address1",searchText);
        //BoolQueryBuilder boolFilterBuilder = QueryBuilders.boolQuery();
        //QueryBuilders.
        //boolFilterBuilder.must(termQueryBuilder);
        //QueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(boolFilterBuilder);
        //SearchResponse searchResponse = defaultESRepository.getClient().prepareSearch(indexName).setQuery(queryBuilder).setSize(0).get();
        SearchResponse searchResponse = defaultESRepository.getClient().prepareSearch(indexName).setQuery(matchQueryBuilder).setSize(10).get();
        SearchHits searchHits =searchResponse.getHits();
        List<UserAddressModel> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            UserAddressModel model = JsonUtil.parserJson(hit.getSourceAsString(), UserAddressModel.class);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    public List<UserAddressModel>  searchAddress(String indexName,String searchText){
        // 过滤条件
        //MatchQueryBuilder matchQueryBuilder=QueryBuilders.matchQuery("address1",searchText);
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termBuilder = QueryBuilders.termQuery("address1",searchText);
        boolBuilder.must(termBuilder);
        //QueryBuilders.
        //boolFilterBuilder.must(termQueryBuilder);
        //QueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(boolFilterBuilder);
        //SearchResponse searchResponse = defaultESRepository.getClient().prepareSearch(indexName).setQuery(queryBuilder).setSize(0).get();
        //MatchAllQueryBuilder matchAll= QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = defaultESRepository.getClient().prepareSearch(indexName).setQuery(boolBuilder).setSize(100).get();
        SearchHits searchHits =searchResponse.getHits();
        List<UserAddressModel> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            UserAddressModel model = JsonUtil.parserJson(hit.getSourceAsString(), UserAddressModel.class);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

}
