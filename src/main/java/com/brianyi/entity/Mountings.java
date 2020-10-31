package com.brianyi.entity;

import java.io.Serializable;

/**
 * (Mountings)实体类
 *
 * @author makejava
 * @since 2020-10-20 14:40:15
 */
public class Mountings implements Serializable {
    private static final long serialVersionUID = -47916542929709216L;

    private Integer id;

    private String name;

    private Double price;

    private String picture;

    private Integer commodityType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}