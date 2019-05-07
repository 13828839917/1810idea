package com.jk.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {

    private  String   userid;
    private  String    username;
    private  String   userpass;
    private  String   usermobile;
    private  String   useremail;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyyy-MM-dd",timezone = "GTM+8")
    private Date useregentime;
    private  String  userurl;
    private  String  start;

}
