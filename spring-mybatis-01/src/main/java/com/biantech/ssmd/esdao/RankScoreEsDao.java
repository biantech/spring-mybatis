package com.biantech.ssmd.esdao;

import com.biantech.ssmd.es.DefaultESRepository;
import com.biantech.ssmd.esdomain.RankScoreModel;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 
 * @date 2018/11/13
 */
@Component
public class RankScoreEsDao {
    @Autowired
    private DefaultESRepository esRepository;
    //private TransportClient esClient;
    public void addRankScore(RankScoreModel rankScoreModel){
        //esRepository.
        return ;
    }

    public void createESIndex() throws Exception {
        XContentBuilder xContentBuilder = esRepository.buildMapping("rank");
        String jsonString = xContentBuilder.toString();
        esRepository.createIndexMapping("rank-score","rank",xContentBuilder);
    }
}
