package com.brianyi.entity;

import java.io.Serializable;

/**
 * (Shopping)实体类
 *
 * @author makejava
 * @since 2020-10-20 14:40:15
 */
public class Shopping implements Serializable {
    private static final long serialVersionUID = 561373884577679455L;

    private Integer id;

    private Integer num;

    private Integer commodityId;

    private String name;

    private Double price;

    private String picture;

    private Integer commodityType;

    private String uname;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(Integer commodityType) {
        this.commodityType = commodityType;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "Shopping{" +
                "id=" + id +
                ", num=" + num +
                ", commodityId=" + commodityId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", picture='" + picture + '\'' +
                ", commodityType=" + commodityType +
                ", uname='" + uname + '\'' +
                '}';
    }
}