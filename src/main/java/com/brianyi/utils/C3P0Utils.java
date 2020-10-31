package com.brianyi.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class C3P0Utils {

	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

	
	public static DataSource getDataSource(){
		return dataSource;
	}
	

	public static Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	

	public static void close(ResultSet rs,Statement stat,Connection con){
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		if(stat!=null)
			 try{
				 stat.close();
			 }catch(Exception ex){}
			
			if(con!=null)
				try{
					 con.close();
				 }catch(Exception ex){}
	}
}
