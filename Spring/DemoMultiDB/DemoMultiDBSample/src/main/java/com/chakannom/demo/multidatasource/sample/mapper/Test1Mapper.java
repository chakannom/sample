package com.chakannom.demo.multidatasource.sample.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.chakanframework.multidatasource.annotation.DataSource1;
import com.chakannom.demo.multidatasource.sample.domain.TestDTO;

@DataSource1
public interface Test1Mapper {

	@Select("select * from test_table where test_column = #{testColumn}")
	@Results(value = {
			@Result(property="testColumn", column="test_column")
	})
	public TestDTO select(String testColumn);

}
