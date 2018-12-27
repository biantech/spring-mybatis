package com.biantech.ssmd.test.es;

import com.biantech.ssmd.es.DefaultESRepository;
import com.biantech.ssmd.es.ESConstants;
import com.biantech.ssmd.es.ESIndexBuilder;
import com.biantech.ssmd.esdomain.UserAddressModel;
import com.biantech.ssmd.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author BIANJIANQUAN223
 * @date 2018/12/26
 */
public class ESIndexBuilderTest extends BaseTest {
   @Autowired
   ESIndexBuilder esIndexBuilder;

   @Autowired
   DefaultESRepository<UserAddressModel> defaultESRepository;

   String indexName = "test-user-address";

   @Test
   public void testBuildUserAddress(){
       esIndexBuilder.createUserAddressIndex(indexName);
   }

   @Test
   public void testBuildUserAddressItems() throws Exception{
      ArrayList<UserAddressModel> list = new ArrayList<>();
      UserAddressModel userAddressModel = new UserAddressModel();
      userAddressModel.setAddress1("安徽省长江流域");
      userAddressModel.setExpireTime(new Date());
      userAddressModel.setScore(100);
      userAddressModel.setUserId(1);
      userAddressModel.setUserName("安徽省长江流域");
      //defaultESRepository.createDocument(indexName,userAddressModel,"1");
      list.add(userAddressModel);
      UserAddressModel userAddressModel2 = new UserAddressModel();
      userAddressModel2.setAddress1("上海市闵行区长江镇梅陇村");
      userAddressModel2.setExpireTime(new Date());
      userAddressModel2.setScore(200);
      userAddressModel2.setUserId(2);
      userAddressModel2.setUserName("上海市闵行区长江镇梅陇村");
      list.add(userAddressModel2);
      //defaultESRepository.createDocument(indexName,userAddressModel2,"1");
      defaultESRepository.createDocument(indexName, ESConstants.DefaultIndexType,list);
   }

}
