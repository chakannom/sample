package com.chakanframework.multidatasource.properties;

public interface DatabaseProperties {
	
	public String getDriverClassName();
	
	public String getUrl();
	
	public String getUserName();
	
	public String getPassword();
	
	public int getInitialSize();
	
	public int getMaxTotal();
	
	public int getMaxIdle();
	
	public int getMinIdle();
	
	public long getMaxWaitMillis();
	
	public String getValidationQuery();
}
