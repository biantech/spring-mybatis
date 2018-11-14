package com.biantech.ssmd.test.service;

import com.biantech.ssmd.domain.User;
import com.biantech.ssmd.service.serviceImpl.UserServiceImpl;
import com.biantech.ssmd.test.BaseTest;
import com.biantech.ssmd.exception.OtherThingsException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/9/25.
 */
public class UserServiceTest extends BaseTest {
    Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testAdd() {
        User user = new User();
        try {
            userService.add(user);
        } catch (OtherThingsException e) {
            //其他综合异常或是不能处理的异常
            e.printStackTrace();
        }
    }

    @Test
    public void testSelect() {
        User user = new User();
        user.setLoginId("111111");
        user=userService.findUser(user);
        logger.info("user="+user);
    }
}
