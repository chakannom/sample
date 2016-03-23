package com.chakanframework.multidatasource.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chakanframework.multidatasource.annotation.DataSource0;
import com.chakanframework.multidatasource.annotation.DataSource1;

public abstract class MyBatisConfig {

	public static final String BASE_PACKAGE = "**.mapper";
	public static final String TYPE_ALIASES_PACKAGE = "**.domain";

	protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
	}
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = DataSource0.class, sqlSessionFactoryRef = "dataSource0SqlSessionFactory")
class DataSource0MyBatisConfig extends MyBatisConfig {

	@Bean
	public SqlSessionFactory dataSource0SqlSessionFactory(@Qualifier("dataSource0") DataSource dataSource0) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		configureSqlSessionFactory(sessionFactoryBean, dataSource0);
		return sessionFactoryBean.getObject();
	}
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = DataSource1.class, sqlSessionFactoryRef = "dataSource1SqlSessionFactory")
class DataSource1MyBatisConfig extends MyBatisConfig {

	@Bean
	public SqlSessionFactory dataSource1SqlSessionFactory(@Qualifier("dataSource1") DataSource dataSource1) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		configureSqlSessionFactory(sessionFactoryBean, dataSource1);
		return sessionFactoryBean.getObject();
	}
}