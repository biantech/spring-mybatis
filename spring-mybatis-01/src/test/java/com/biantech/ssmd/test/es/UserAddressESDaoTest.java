package com.biantech.ssmd.test.es;

import com.biantech.ssmd.esdao.UserAddressESDao;
import com.biantech.ssmd.esdomain.UserAddressModel;
import com.biantech.ssmd.test.BaseTest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author BIANJIANQUAN223
 * @date 2018/12/27
 */
public class UserAddressESDaoTest extends BaseTest {
    Logger logger = LoggerFactory.getLogger(UserAddressESDaoTest.class);
    @Autowired
    UserAddressESDao userAddressESDao;
    //https://blog.csdn.net/HcJsJqJSSM/article/details/83686997
    //https://blog.csdn.net/chengyuqiang/article/details/78991570
    String indexName = "test-user-address";
    String analyzeText = "中华人民共和国港珠澳大桥建成通车";
    @Test
    public void testAnalyzeAddress(){
        //String analyzerPattern="standard";
        //String analyzerPattern="ik_max_word";
        String analyzerPattern="ik_smart";
        List<AnalyzeResponse.AnalyzeToken> list=userAddressESDao.analyerAddress(indexName,analyzeText,analyzerPattern);
        for(AnalyzeResponse.AnalyzeToken token :list){
            logger.info(token.getTerm()+","+token.getType());
        }
    }

    @Test
    public void testSearchAddress(){
        //String searchText = "上海市闵行区长江镇梅陇村";
        String searchText="上海";
        List<UserAddressModel> list=userAddressESDao.searchAddress(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
        searchText="闵行区";
        list=userAddressESDao.searchAddress(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
        searchText="长江";
        list=userAddressESDao.searchAddress(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
        searchText="梅陇";
        list=userAddressESDao.searchAddress(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
    }

      @Test
    public void testSearchAddressMatch(){
        //String searchText = "上海市闵行区长江镇梅陇村";
        String searchText="上海";
        List<UserAddressModel> list=userAddressESDao.searchAddressMatch(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
        searchText="闵行区";
        list=userAddressESDao.searchAddressMatch(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
        searchText="长江";
        list=userAddressESDao.searchAddressMatch(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
        searchText="梅陇";
        list=userAddressESDao.searchAddressMatch(indexName,searchText);
        logger.info(searchText+",queryResult="+list);
    }

    @Test
    public void testAll(){
        testSearchAddress();
        testSearchAddressMatch();
    }
}
