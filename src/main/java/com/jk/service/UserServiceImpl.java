package com.jk.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.bean.User;
import com.jk.dao.UserMapper;
import com.jk.util.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Reference
    TreeService treeService;

    @Autowired
    private UserMapper  userMapperapper;
@Transactional(readOnly = true)
/*@Transactional(propagation = Propagation.SUPPORTS)*/

@Override
public HashMap<String,Object>  findUser(User user,Integer start,Integer pageSize){
    HashMap<String, Object> map = new HashMap<>();
    System.out.println("dddd");
    List<User> list=userMapperapper.findUser(user,start,pageSize);
      Long count= userMapperapper.findCount(user);
      map.put("rows",list);
      map.put("total",count);
    return map;
}

    @Override
    public List<MenuTree> getTree() {
        return treeService.getTree();

    }

    @Override
    public void deleteAll(String id) {
        userMapperapper.deleteAll(id.split(","));
    }

    @Override
    public User findUserById(String id) {
        return userMapperapper.findUserById( id);
    }

    @Override
    public void updateUser(User user) {
        userMapperapper.updateUser( user);

    }

    @Override
    public void addUser(User user) {

        userMapperapper.addUser(user);
    }
//登录用户名密码是否正确
    @Override
    public User login(User user) {
        return userMapperapper.findUserNameByUserpass(user);
    }


}
