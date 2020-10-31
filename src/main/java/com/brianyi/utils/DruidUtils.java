package com.brianyi.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * TODO
 *
 * @author ahao 2020-10-20
 */
public class DruidUtils {
     private static DataSource dataSource;
     static {
          InputStream resourceAsStream = DruidUtils.class.getClassLoader().getResourceAsStream("druid_config.properties");
          Properties properties = new Properties();
          try {
               properties.load(resourceAsStream);
          } catch (IOException e) {
               e.printStackTrace();
          }
          //init
          try {
               dataSource = DruidDataSourceFactory.createDataSource(properties);
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     public static DataSource getDataSource() {
          return dataSource;
     }


     public static void close(ResultSet rs, Statement stat, Connection con){
          if(rs!=null) {
               try {
                    rs.close();
               } catch (SQLException e) {
                    e.printStackTrace();
               }
          }

          if(stat!=null) {
               try{
                    stat.close();
               }catch(Exception ex){}
          }

          if(con!=null) {
               try{
                    con.close();
               }catch(Exception ex){}
          }
     }

     public static void main(String[] args) {
          getDataSource();
     }
}
