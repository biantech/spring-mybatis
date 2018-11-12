package com.biantech.ssmd.service;

import com.biantech.ssmd.domain.User;

import java.util.List;

/**
 * Created by Administrator on 2016/9/25.
 */
public interface UserService extends BaseService<User> {

    void add(User user) throws Exception;

    User findUser(User user) throws Exception;

    void updateLoginSession(String sessionId, String LoginId);

    List<User> findAll(int pageNum, int pageSize);
}
