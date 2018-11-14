package com.biantech.ssmd.test.dao;

import com.biantech.ssmd.esdao.RankScoreEsDao;
import com.biantech.ssmd.test.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 
 * @date 2018/11/13
 */
public class ESRankScoreDaoTest  extends BaseTest {
    private Logger logger = LoggerFactory.getLogger(ESRankScoreDaoTest.class);
    @Autowired
    RankScoreEsDao rankScoreEsDao;
    @Test
    public void testRankScoreIndexCreate() throws Exception{
        rankScoreEsDao.createESIndex();
    }
}
