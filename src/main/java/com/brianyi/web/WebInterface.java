package com.brianyi.web;

import com.alibaba.fastjson.JSON;
import com.brianyi.dao.CommodityDao;
import com.brianyi.entity.Result;
import com.brianyi.entity.Shopping;
import com.brianyi.service.AllService;
import com.brianyi.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * TODO
 *
 * @author ahao 2020-10-21
 */
@WebServlet(urlPatterns = "*.do")
public class WebInterface extends BaseServlet {


    public void login(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException {
        Result result = new Result();
        if (checkVerificationCode(request)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            UserService userService  = new UserService();
            result = userService.login(username,password);
            //session 保存登录标志
            HttpSession session = request.getSession();
            session.setAttribute("user",username);
            //cookie 返回登录标志
            Cookie cookie = new Cookie("login","1");
            //返回前同步 session中的购物车
            List<Shopping> shoppingCar = (List<Shopping>) session.getAttribute("shoppingCar");
            if (shoppingCar != null) {
                CommodityDao commodityDao = new CommodityDao();
                for (Shopping shopping : shoppingCar) {
                    shopping.setUname(username);
                    Shopping commodityForShopping = commodityDao.findCommodityForShopping(shopping);
                    if (commodityForShopping != null) {
                        //有一样的商品 此商品num++
                        commodityForShopping.setNum(commodityForShopping.getNum()+1);
                        commodityDao.updateShoppingNum(commodityForShopping);
                    }else  {
                        //没有则插入
                        commodityDao.insertShopping(shopping);
                    }
                }
            }
            response.addCookie(cookie);
        }else {
            result.setMessage("验证码错误");
            result.setCode(Result.FAILS);
        }
        response.getWriter().print(JSON.toJSONString(result));
    }

    public void allCommodity(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        Integer curPage = getCurPage(request);
        response.getWriter().print(new AllService().getAllCommodity(curPage));
    }

    public void costume(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {

        response.getWriter().print(new AllService().getAllCostume(getCurPage(request)));
    }

    public void household(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        response.getWriter().print(new AllService().getAllHouseHold(getCurPage(request)));
    }
    public void mountings(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        response.getWriter().print(new AllService().getAllMountings(getCurPage(request)));
    }

    public void shopping(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        //根据msg参数 进行任务分发 调用service层方法
        String msg = request.getParameter("msg");
        AllService allService = new AllService();
        String result ;
        if ("add".equals(msg)){
            result = allService.shoppingAdd(request);
        }else if ("delete_all".equals(msg)) {
            result = allService.shoppingDeleteAll(request);
        }else if ("delete".equals(msg)) {
            result = allService.shoppingDeleteOne(request);
        }else {
            result = allService.shoppingShowAll(request);
        }
        response.getWriter().print(result);
    }

    public void register(HttpServletRequest request ,HttpServletResponse response) throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        UserService userService = new UserService();
        Result result = new Result();
        if (checkVerificationCode(request)) {
           result =  userService.register(username,password,email);
        }else {
            result.setCode(Result.FAILS);
            result.setMessage("验证码错误");
        }
         response.getWriter().print(JSON.toJSON(result));
    }
    public void getCommodityNum(HttpServletRequest request,HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
        String type = request.getParameter("type");
        AllService allService = new AllService();
        Result result = new Result();
        // 0 所有   1 配件   2 家用  3服饰
        if ("0".equals(type)) {
            Integer allCommodityNum = allService.getAllCommodityNum();
            result.setObj(allCommodityNum);
        }else if ("1".equals(type)) {
            Integer allMountingsNum = allService.getAllMountingsNum();
            result.setObj(allMountingsNum);
        }else if ("2".equals(type)) {
            Integer allHouseHoldNum = allService.getAllHouseHoldNum();
            result.setObj(allHouseHoldNum);
        }else {
            Integer allMountingsNum = allService.getAllMountingsNum();
            result.setObj(allMountingsNum);
        }
        result.setCode(Result.SUCCESS);
        response.getWriter().print(JSON.toJSONString(result));
    }

    public void getCheckNum(HttpServletRequest request,HttpServletResponse response ) throws IOException {
        HttpSession session = request.getSession();
        //生成随机码 存在session中
        String checkNum = randomNum();
        session.setAttribute("checkNum",checkNum);

        //设置返回
        Result result = new Result();
        result.setCode(Result.SUCCESS);
        result.setObj(checkNum);
        response.getWriter().print(JSON.toJSONString(result));
    }

    public void loginOut(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("login".equals(cookie.getName())) {
                //删除cookei
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        //删除登录保存的session
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        response.getWriter().print("success");
    }

    private Boolean checkVerificationCode(HttpServletRequest request) {
        String checkNumFromUser = request.getParameter("checkNum");
        HttpSession session = request.getSession();
        String checkNum = (String)session.getAttribute("checkNum");
        return checkNumFromUser.equals(checkNum);
    }



    private String randomNum() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; ++i) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private Integer getCurPage(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("curPage")==null?"1":request.getParameter("curPage"));
    }
}
