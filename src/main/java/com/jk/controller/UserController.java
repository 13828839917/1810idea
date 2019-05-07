package com.jk.controller;
import com.alibaba.fastjson.JSON;
import com.jk.bean.User;
import com.jk.service.UserService;
import com.jk.util.Constant;
import com.jk.util.MenuTree;
import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Controller
public class UserController {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    StringRedisTemplate redis;

    @Autowired
    UserService userService;
    @RequestMapping("findUser")
    @ResponseBody
    public HashMap<String,Object> findUser(User user,Integer start,Integer pageSize) {

        return userService.findUser(user,start,pageSize);


    }
    @RequestMapping("getTree")
    @ResponseBody
    public String  getTree(){
        System.out.println();
       if(redis.hasKey("userTrue")){
            String userTree = redis.opsForValue().get("userTrue");
            return  userTree;
        }else{
            List<MenuTree> list=userService.getTree();

            redis.opsForValue().set("userTrue", JSON.toJSON(list).toString());
            String userTree = redis.opsForValue().get("userTrue");
            return    userTree;
        }

    }


    @RequestMapping("find")
    public  String find(String url) {

        return url;
    }
    @RequestMapping("deleteAll")
    @ResponseBody
    public void deleteAll(String id){
        userService.deleteAll(id);

    }
   @RequestMapping("findBootDialogById")
    public  String  findBootDialogById(String id, Model model){
        if(id!=null &&id!="" ){
       User user=     userService.findUserById(id);
            model.addAttribute("user",user);
            return  "updateOfAdd";
        }
        return   "updateOfAdd";

   }

   @RequestMapping("saveOrUpdate")
    @ResponseBody
    public  void saveOrUpdate(User user){
        if(user!=null &&user.getUserid()!=null&&user.getUserid()!=""){
            userService.updateUser(user);

        }else{
            userService.addUser( user);
        }


   }

    //图片上传
    @RequestMapping("imgUpload")
    @ResponseBody
    public HashMap<Object, Object>  uploadImg(MultipartFile userImg,  HttpServletResponse response){
        GridFS gridFS = new GridFS(mongoTemplate.getDb());
        String id = UUID.randomUUID().toString();
        String filename = userImg.getOriginalFilename();
        try {
            GridFSInputFile file = gridFS.createFile(userImg.getInputStream());
            file.setId(id);
            file.setFilename(filename);
            file.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("imgId", id);
        map.put("fileName", filename);
        return map;



    }

    //图片回显
    @RequestMapping("findImgById")
    public void findImgById(String imgId,HttpServletResponse response){

        BasicDBObject query = new BasicDBObject("_id",imgId);

        GridFS gridFS = new GridFS(mongoTemplate.getDb());

        GridFSDBFile findOne = gridFS.findOne(query);

        try {
            if(findOne!=null){

                response.setContentType("application/octet-stream");
                ServletOutputStream sos = response.getOutputStream();
                findOne.writeTo(sos);
                sos.flush();
                sos.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
    //去登录页面
    @RequestMapping("ToLogin")
    public  String ToLogin(HttpServletRequest request,Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null&& Constant.cookieName.equals(cookie.getName()) ) {
                    String cookieValue = cookie.getValue();
                    String[] split = cookieValue.split(Constant.cookiePws);
                    model.addAttribute("username",split[0]);
                    model.addAttribute("userpass",split[1]);

                }
            }


        }


        return "login";
    }

    //登录
    @RequestMapping("login")
        public  String login(User user,HttpServletResponse response ,Model model){

     if(user!=null&& user.getUserpass()!=""&& user.getUsername()!=""){
         //判断用户名和密码是否正确
         User user1= userService.login(user);
         if (user1 != null) {
             //正确  记住密码
             if (user.getStart() != null && "1".equals(user.getStart())) {
                 //是-->  用户名和密码都存到cookie中去
                 Cookie cookie = new Cookie(Constant.cookieName, user.getUsername() + Constant.cookiePws + user.getUserpass());
                 int yizhou=60*60*24*7;
                 cookie.setMaxAge(yizhou);
                 response.addCookie(cookie);
                 //正确 不否记住密码
             }else{

                 Cookie cookie = new Cookie(Constant.cookieName,"");
                 cookie.setMaxAge(0);
                 response.addCookie(cookie);

             }
             return "index";
             //登录密码错误失败
         }else{
             model.addAttribute("msg","登录密码错误失败");
             Cookie cookie = new Cookie(Constant.cookieName,"");
             cookie.setMaxAge(0);
             response.addCookie(cookie);

             return "login";
         }

     }
        model.addAttribute("msg","账户密码为空");
        return  "login";
        }


}
