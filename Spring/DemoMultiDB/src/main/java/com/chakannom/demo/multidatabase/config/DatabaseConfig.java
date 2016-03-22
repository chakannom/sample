package com.chakannom.demo.multidatabase.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.chakannom.demo.multidatabase.properties.DataSource0DatabaseProperties;
import com.chakannom.demo.multidatabase.properties.DataSource1DatabaseProperties;
import com.chakannom.demo.multidatabase.properties.DatabaseProperties;

public abstract class DatabaseConfig {

	@Bean
	public abstract DataSource dataSource();

	protected void configureDataSource(org.apache.tomcat.jdbc.pool.DataSource dataSource, DatabaseProperties databaseProperties) {
		dataSource.setDriverClassName(databaseProperties.getDriverClassName());
		dataSource.setUrl(databaseProperties.getUrl());
		dataSource.setUsername(databaseProperties.getUserName());
		dataSource.setPassword(databaseProperties.getPassword());
		dataSource.setMaxActive(databaseProperties.getMaxActive());
		dataSource.setMaxIdle(databaseProperties.getMaxIdle());
		dataSource.setMinIdle(databaseProperties.getMinIdle());
		dataSource.setMaxWait(databaseProperties.getMaxWait());
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
	}
}

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(DataSource0DatabaseProperties.class)
class DataSource0DatabaseConfig extends DatabaseConfig {

	@Autowired
	private DataSource0DatabaseProperties dataSource0DatabaseProperties;

	@Primary
	@Bean(name = "dataSource0", destroyMethod = "close")
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource0 = new org.apache.tomcat.jdbc.pool.DataSource();
		configureDataSource(dataSource0, dataSource0DatabaseProperties);
		return dataSource0;
	}

	@Bean
	public PlatformTransactionManager transactionManager(@Qualifier("dataSource0") DataSource dataSource0) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource0);
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}
}

@Configuration
@EnableConfigurationProperties(DataSource1DatabaseProperties.class)
class DataSource1DatabaseConfig extends DatabaseConfig {

	@Autowired
	private DataSource1DatabaseProperties dataSource1DatabaseProperties;

	@Bean(name = "dataSource1", destroyMethod = "close")
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = new org.apache.tomcat.jdbc.pool.DataSource();
		configureDataSource(dataSource1, dataSource1DatabaseProperties);
		return dataSource1;
	}
}