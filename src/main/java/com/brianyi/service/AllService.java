package com.brianyi.service;

import com.alibaba.fastjson.JSON;
import com.brianyi.dao.CommodityDao;
import com.brianyi.dao.UserDao;
import com.brianyi.entity.Costume;
import com.brianyi.entity.Household;
import com.brianyi.entity.Mountings;
import com.brianyi.entity.Shopping;
import com.brianyi.utils.ShoppingID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author 2020-10-21
 */
public class AllService {
    private Integer pageNum = 10;
    public String getAllCommodity(int curPage) throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();

        List<Object> commodity = commodityDao.findAll("costume", "com.brianyi.entity.Costume");
        commodity.addAll(commodityDao.findAll("household", "com.brianyi.entity.Household"));
        commodity.addAll(commodityDao.findAll("mountings", "com.brianyi.entity.Mountings"));
        Map result = new HashMap();

        result.put("allCommodity", commodity.subList((curPage - 1) * pageNum , Math.min(curPage * pageNum, commodity.size())));
        result.put("size",commodity.size());
        return JSON.toJSONString(result);
    }


    public String getAllCostume(int curPage) throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();
        List<Object> costume = commodityDao.findAll("costume", "com.brianyi.entity.Costume");
        Map result = new HashMap();

        result.put("costumeData", costume.subList((curPage - 1) * pageNum , Math.min(curPage * pageNum, costume.size())));
        result.put("size",costume.size());
        return JSON.toJSONString(result);
    }

    public String getAllHouseHold(int curPage) throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();
        List<Object> household = commodityDao.findAll("household", "com.brianyi.entity.Household");
        Map result = new HashMap();
        result.put("householdData", household.subList((curPage - 1) * pageNum , Math.min(curPage * pageNum, household.size())));
        result.put("size",household.size());
        return JSON.toJSONString(result);
    }

    public String getAllMountings(int curPage) throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();
        List<Object> mountings = commodityDao.findAll("mountings", "com.brianyi.entity.Mountings");
        Map result = new HashMap();
        result.put("mountingsData", mountings.subList((curPage - 1) * pageNum ,  Math.min(curPage * pageNum, mountings.size())));
        result.put("size",result.size());
        return JSON.toJSONString(result);
    }

    public String shoppingAdd(HttpServletRequest request) {
        String result = "false";
        //封装javabean
        String commodityType = request.getParameter("commodity_type");
        String commodityId = request.getParameter("commodity_id");
        Shopping shopping = new Shopping();
        shopping.setCommodityType(Integer.parseInt(commodityType));
        shopping.setCommodityId(Integer.parseInt(commodityId));
        shopping.setNum(1);

        shopping = findCommodity(shopping);
        if (checkLogin(request)) {
            //没登陆
            HttpSession session = request.getSession();
            //判断session中有没有购物车对象
            List<Shopping> shoppingCar = (List<Shopping>) session.getAttribute("shoppingCar");
            if (shoppingCar == null) {
                //没有创建一个
                List<Shopping> shoppingCarSaveToSession = new ArrayList<>();
                //设置id
                shopping.setId(ShoppingID.getShoppingId());
                shoppingCarSaveToSession.add(shopping);
                session.setAttribute("shoppingCar", shoppingCarSaveToSession);
                result = "success";
            } else {
                AtomicBoolean atomicBoolean = new AtomicBoolean(true);
                for (Shopping shopping1 : shoppingCar) {
                    if (shopping1.getCommodityType().equals(shopping.getCommodityType()) && shopping1.getCommodityId().equals(shopping.getCommodityId())) {
                        shopping1.setNum(shopping1.getNum() + 1);
                        atomicBoolean.set(false);
                        break;
                    }
                }
                //没有则添加
                if (atomicBoolean.get()) {
                    //设置id
                    shopping.setId(ShoppingID.getShoppingId());
                    shoppingCar.add(shopping);
                }
                result = "success";
            }
        } else {
            //登录逻辑 获取用户名
            HttpSession session = request.getSession();
            String userName = (String)session.getAttribute("user");
            shopping.setUname(userName);
            //判断用户购物车 有没有一样的商品
            CommodityDao commodityDao = new CommodityDao();
            try {
                Shopping commodityForShopping = commodityDao.findCommodityForShopping(shopping);
                if (commodityForShopping != null) {
                    //有一样的商品 此商品num++
                    commodityForShopping.setNum(commodityForShopping.getNum()+1);
                    commodityDao.updateShoppingNum(commodityForShopping);
                }else  {
                    //没有则插入
                    int i = commodityDao.insertShopping(shopping);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            result = "success";
        }
        return result;
    }

    public String shoppingDeleteAll(HttpServletRequest request) {
        String result = "false";
        if (checkLogin(request)) {
            //没有登录 清空session中的list的内容
            HttpSession session = request.getSession();
            session.setAttribute("shoppingCar", new ArrayList<Shopping>());
            result = "success";
        }else {
            HttpSession session = request.getSession();
            String userName = (String)session.getAttribute("user");
            CommodityDao commodityDao = new CommodityDao();
            try {
                commodityDao.deleteAllShoppingForUser(userName);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return result;
    }

    public String shoppingDeleteOne(HttpServletRequest request) {
        String result = "false";
        String commodityType = request.getParameter("commodity_type");
        String commodityId = request.getParameter("commodity_id");
        if (checkLogin(request)) {
            //没有登录处理
            HttpSession session = request.getSession();
            List<Shopping> shoppingCar = (List<Shopping>) session.getAttribute("shoppingCar");
            //过滤
            for (int i = 0; i < shoppingCar.size(); i++) {
                if (shoppingCar.get(i).getCommodityId() == Integer.parseInt(commodityId) && shoppingCar.get(i).getCommodityType() == Integer.parseInt(commodityType)){
                    //查看数量
                    if (shoppingCar.get(i).getNum() - 1 == 0) {
                        shoppingCar.remove(i);
                    } else {
                        shoppingCar.get(i).setNum(shoppingCar.get(i).getNum() - 1);
                    }
                }
            }
            result = "success";
        }else {
            //登录逻辑
            //获取用户名
            HttpSession session = request.getSession();
            String userName = (String)session.getAttribute("user");
            //查看此商品在数据库中的数量进行逻辑判断
            CommodityDao commodityDao  = new CommodityDao();
            Shopping shopping = new Shopping();
            shopping.setUname(userName);
            shopping.setCommodityId(Integer.valueOf(commodityId));
            shopping.setCommodityType(Integer.valueOf(commodityType));
            try {
                Shopping commodityForShopping = commodityDao.findCommodityForShopping(shopping);
                if (commodityForShopping.getNum() - 1 == 0) {
                    //删除为0 则删除此条记录
                    int i = commodityDao.deleteOneShopping(userName,commodityId,commodityType);
                }else {
                    //不为0 则更新此记录
                    commodityForShopping.setNum(commodityForShopping.getNum() - 1);
                    commodityDao.updateShoppingNum(commodityForShopping);
                }
                result = "success";
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return result;

    }

    public String shoppingShowAll(HttpServletRequest request) {
        List<Object> objects = new ArrayList<>();
        Map result = new HashMap(16);
        if (checkLogin(request)) {
            //未登录 查询所有物品
            HttpSession session = request.getSession();
            List<Shopping> shoppingCar = (List<Shopping>) session.getAttribute("shoppingCar");
            //判断有无购物车 防止 NPE
            if (shoppingCar == null) {
                shoppingCar = new ArrayList<>();
                session.setAttribute("shoppingCar", shoppingCar);
            }
            objects.addAll(shoppingCar);
            result.put("shoppings", objects);
        }else {
            //登录处理
            HttpSession session = request.getSession();
            String userName = (String)session.getAttribute("user");
            CommodityDao commodityDao = new CommodityDao();
            try {
                objects.addAll(commodityDao.findAllShoppingByUserName(userName));
                result.put("shoppings", objects);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return JSON.toJSONString(result);
    }

    //获取商品表的数据数量 为分页提供数据
    public Integer getAllCommodityNum() throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();

        List<Object> commodity = commodityDao.findAll("costume", "com.brianyi.entity.Costume");
        commodity.addAll(commodityDao.findAll("household", "com.brianyi.entity.Household"));
        commodity.addAll(commodityDao.findAll("mountings", "com.brianyi.entity.Mountings"));
        return commodity.size();
    }

    public Integer getAllCostumeNum() throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();
        return commodityDao.findAll("costume", "com.brianyi.entity.Costume").size();
    }

    public Integer getAllHouseHoldNum() throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();
        return  commodityDao.findAll("household", "com.brianyi.entity.Household").size();
    }

    public Integer getAllMountingsNum() throws SQLException, ClassNotFoundException {
        CommodityDao commodityDao = new CommodityDao();
        return  commodityDao.findAll("mountings", "com.brianyi.entity.Mountings").size();
    }


    private Boolean checkLogin(HttpServletRequest request) {
        return request.getSession().getAttribute("user") == null;
    }

    /**
     * 补全shopping 字段
     * @param shopping
     * @return
     */
    private Shopping findCommodity(Shopping shopping) {
        CommodityDao commodityDao = new CommodityDao();
        try {
            if (shopping.getCommodityType() == 1) {
                Household household = (Household) commodityDao.findCommodityOne("household", "com.brianyi.entity.Household", shopping.getCommodityId());
                shopping.setPicture(household.getPicture());
                shopping.setName(household.getName());
                shopping.setPrice(household.getPrice());

            } else {
                if (shopping.getCommodityType() == 2) {
                    Mountings mountings = (Mountings) commodityDao.findCommodityOne("mountings", "com.brianyi.entity.Mountings", shopping.getCommodityId());

                    shopping.setPicture(mountings.getPicture());
                    shopping.setName(mountings.getName());
                    shopping.setPrice(mountings.getPrice());
                } else {
                   Costume costume = (Costume)commodityDao.findCommodityOne("costume", "com.brianyi.entity.Costume", shopping.getCommodityId());
                    shopping.setPicture(costume.getPicture());
                    shopping.setName(costume.getName());
                    shopping.setPrice(costume.getPrice());
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return shopping;
    }

}
