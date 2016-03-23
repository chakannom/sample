package com.chakanframework.multidatasource.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataSource1DatabaseProperties.PREFIX)
public class DataSource1DatabaseProperties implements DatabaseProperties {

	public static final String PREFIX = "datasource[1]"; 

	private String driverClassName;
	
	private String url;
	
	private String userName;
	
	private String password;
	
	private int initialSize;
	
	private int maxTotal;
	
	private int maxIdle;
	
	private int minIdle;
	
	private long maxWaitMillis;
	
	private String validationQuery;

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	
	public String getValidationQuery() {
		return validationQuery;
	}
	
	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
	
	@Override
	public String toString() {
		return "DataSource1DatabaseProperties[" + this.driverClassName + "]";
	}
}
