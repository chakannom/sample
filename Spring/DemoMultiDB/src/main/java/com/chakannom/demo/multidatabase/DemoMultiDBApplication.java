package com.chakannom.demo.multidatabase;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
	@PropertySource(value = "classpath:demomultidb.properties")
})
@MapperScan({"com.chakannom.chakanoffice.multidatabase.mapper"})
public class DemoMultiDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoMultiDBApplication.class, args);
    }
}
