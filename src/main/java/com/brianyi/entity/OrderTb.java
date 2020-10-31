package com.brianyi.entity;

import java.io.Serializable;

/**
 * (OrderTb)实体类
 *
 * @author makejava
 * @since 2020-10-20 14:40:15
 */
public class OrderTb implements Serializable {
    private static final long serialVersionUID = 122252307339387381L;

    private Integer id;

    private String address;

    private String telnum;

    private Integer num;

    private Object orderDate;

    private Double price;

    private String userName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Object getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Object orderDate) {
        this.orderDate = orderDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}