package com.brianyi.dao;

import com.brianyi.entity.Shopping;
import com.brianyi.utils.DruidUtils;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * @author ahao
 */
public class CommodityDao {
    QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
    private final BeanProcessor beanProcessor = new GenerousBeanProcessor();
    private final RowProcessor processor = new BasicRowProcessor(beanProcessor);

    public List<Object> findAll(String tableName,String className) throws ClassNotFoundException, SQLException {
        String sql = "select * from "+tableName;
        Class<?> aClass = Class.forName(className);
        List<?> query = queryRunner.query(sql, new BeanListHandler<>(aClass, processor));
        return (List<Object>) query;
    }

    public Object findCommodityOne(String tableName ,String className , Integer commodityId) throws ClassNotFoundException, SQLException {
        String sql = "select * from "+tableName+" where id = ?";
        Class<?> aClass = Class.forName(className);
        Object query = queryRunner.query(sql, new BeanHandler<>(aClass, processor),commodityId);
        return query;
    }

    public int insertShopping(Shopping shopping) throws SQLException {
        String sql = "insert into shopping values(?,?,?,?,?,?,?,?)";
        return queryRunner.update(sql,null,shopping.getNum(),shopping.getCommodityId(),shopping.getName(),shopping.getPrice(),shopping.getPicture(),shopping.getCommodityType(),shopping.getUname());
    }

    public Shopping findCommodityForShopping(Shopping shopping) throws SQLException {
        String sql = "select * from shopping where uname = ? and commodity_id = ? and commodity_type = ?";

        return queryRunner.query(sql,new BeanHandler<>(Shopping.class,processor),shopping.getUname(),shopping.getCommodityId(),shopping.getCommodityType());
    }

    public int updateShoppingNum(Shopping shopping) throws SQLException {
        String sql = "update shopping set num = ? where uname=? and commodity_id = ? and commodity_type = ?";

        return queryRunner.update(sql,shopping.getNum(),shopping.getUname(),shopping.getCommodityId(),shopping.getCommodityType());
    }
    public int deleteAllShoppingForUser(String uname) throws SQLException {
        String sql = "delete from shopping where uname = ?";
        return queryRunner.update(sql,uname);
    }
    public  List<Shopping> getAllShoppingForUser(String uname) throws SQLException {
        String sql = "select * from shopping where uname = ?";
        return  queryRunner.query(sql,new BeanListHandler<>(Shopping.class,processor) , uname);
    }

    public int deleteOneShopping(String userName, String commodityId, String commodityType) throws SQLException {
        String sql = "delete from shopping where uname = ? and  commodity_id = ? and commodity_type = ?";
        return queryRunner.update(sql,userName,commodityId,commodityType);
    }

    public List<Shopping> findAllShoppingByUserName(String userName) throws SQLException {
        String sql = "select * from shopping where uname = ?";

        return queryRunner.query(sql,new BeanListHandler<>(Shopping.class,processor),userName);
    }

    @Test
    public void testAll() throws SQLException, ClassNotFoundException {
        List<Object> commodity = findAll("costume", "com.brianyi.entity.Costume");
        System.out.println(commodity.size());
    }

}
