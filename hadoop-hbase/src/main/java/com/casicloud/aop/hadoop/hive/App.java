package com.casicloud.aop.hadoop.hive;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.util.ajax.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class App {
	@Autowired
	private JdbcTemplate hiveTemplate;
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";  
    private static String url = "jdbc:hive2://centos1:10000";  
    private static String user = "root";  
    private static String password = ""; 
	@Test
	public void testQuery() throws Exception {
		hiveTemplate.execute("insert into tb (id,name) values(1,'测试数据')");
		List<Map<String, Object>> list=hiveTemplate.queryForList("select  * from tb");
		System.out.println(JSON.toString(list));
		HttpServletRequest request=null;
		//String json = request.getParameter("sysOrgInfo");
		
		/*Class.forName(driverName);  
		Connection conn = DriverManager.getConnection(url, user, password); 
        PreparedStatement ps=conn.prepareStatement("show databases");
		
        ResultSet rs= ps.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString("database_name"));
			
		}*/
	}
}
