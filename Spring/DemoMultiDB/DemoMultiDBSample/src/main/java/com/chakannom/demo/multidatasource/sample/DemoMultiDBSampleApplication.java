package com.chakannom.demo.multidatasource.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan({
    "com.chakanframework.multidatasource",
    "com.chakannom.demo.multidatasource.sample"
})

@PropertySources({
	@PropertySource(value = "classpath:demomultidbsample.properties")
})
@MapperScan({"com.chakannom.demo.multidatasource.sample.mapper"})
public class DemoMultiDBSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMultiDBSampleApplication.class, args);
	}
}
