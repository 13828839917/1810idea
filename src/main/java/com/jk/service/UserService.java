package com.jk.service;

import com.jk.bean.User;
import com.jk.util.MenuTree;

import java.util.HashMap;
import java.util.List;

public interface UserService {


    HashMap<String,Object> findUser(User user,Integer start,Integer pageSize);

    List<MenuTree> getTree();

    void deleteAll(String id);

    User findUserById(String id);


    void updateUser(User user);

    void addUser(User user);

    User login(User user);
}
