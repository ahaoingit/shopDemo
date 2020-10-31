package com.brianyi.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * (Userdb)实体类
 *
 * @author makejava
 * @since 2020-10-25 10:36:58
 */
public class User implements Serializable {
    private static final long serialVersionUID = 323944354862252176L;

    private String uid;

    private String uname;

    private String password;

    private String email;

    private Date registerTime;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

}