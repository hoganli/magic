package com.hogan;

import com.hogan.common.base.BaseDaoFactoryBean;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:SpringbootServiceApplication
 * Description:springboot main class
 * User:dada
 * Date:2016/12/14-9:31
 */
@RestController
@SpringBootApplication
@RequestMapping(value = "/magic")
@EnableScheduling
@EnableEncryptableProperties
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
public class Application {

    @Autowired
    private Environment env;

    @Value("${server.port}")
    private String port;


    /**
     * 测试方法
     */
    @RequestMapping(value = "/hello")
    public String hello() {
        String s = "<H1>Hello，欢迎使用金邦达邮寄服务平台！</H1>";
        String s1 = "<H2>通过env对象获取配置信息：" + env.getProperty("server.port") + "</H2>";
        String s2 = "<H2>通过@Value对象获取配置信息：" + port + "</H2>";
        return s + s1 + s2;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
