 package test;

import org.junit.Test;

import main.java.cn.xxq.tools.flyway.FlywayService;


/**
 * @author HEX145
 * @date 2019年9月18日
 */
public class FlywayServiceTest {
    public FlywayService init(){
        String url = "jdbc:mysql://172.16.101.221:3306/test";
        String user = "root";
        String password = "root";
        FlywayService service = new FlywayService(url, user, password);
        return service;
    }

    @Test
    public void testInfo(){
        FlywayService service = init();
        service.getFlywayInfo();
    }

    @Test
    public void testMigrate(){
        FlywayService service = init();
        service.migrateFlyway();
    }
}