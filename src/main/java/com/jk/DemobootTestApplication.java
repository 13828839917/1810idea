package com.jk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*@SpringBootConfiguration继承自@Configuration，
二者功能也一致，标注当前类是配置类，
并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到srping容器中，
并且实例名就是方法名。
* */
@SpringBootApplication
/*  扫描当前包及其子包下被@Component，@Controller，@Service，@Repository注解标记的类并纳入到spring容器中进行管理。
 * 是以前的<context:component-scan>
 *     （以前使用在xml中使用的标签，用来扫描包配置的平行支持）。所以本demo中的User为何会被spring容器管理
* */
@MapperScan("com.jk.dao")
/**
 *开启事务控制
 */
@EnableTransactionManagement

public class DemobootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemobootTestApplication.class, args);
    }

}
