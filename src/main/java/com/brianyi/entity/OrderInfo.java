package com.brianyi.entity;

import java.io.Serializable;

/**
 * (OrderInfo)实体类
 *
 * @author makejava
 * @since 2020-10-20 14:40:15
 */
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = -21634414646793509L;

    private Integer id;

    private Integer commodityId;

    private Integer commodityType;

    private Integer commodityNum;

    private Integer orderId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(Integer commodityType) {
        this.commodityType = commodityType;
    }

    public Integer getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(Integer commodityNum) {
        this.commodityNum = commodityNum;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

}