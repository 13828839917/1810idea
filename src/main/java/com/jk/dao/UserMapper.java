package com.jk.dao;


import com.jk.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

  public   List<User> findUser(@Param("user") User user, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

  public         Long      findCount(User user);



    void deleteAll(@Param("id") String[] id);

  User findUserById(String id);


  void updateUser(@Param("user")User user);

  void addUser(@Param("user") User user);

    User findUserNameByUserpass(@Param("user")User user);
}
