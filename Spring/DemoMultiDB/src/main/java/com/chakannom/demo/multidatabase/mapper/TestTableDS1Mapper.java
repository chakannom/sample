package com.chakannom.demo.multidatabase.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.chakannom.demo.multidatabase.domain.TestTableDTO;
import com.chakannom.demo.multidatabase.mapper.annotation.DataSource1;

@DataSource1
public interface TestTableDS1Mapper {

	@Select("select * from test_table where test_column = #{testColumn}")
	@Results(value = {
			@Result(property="testColumn", column="test_column")
	})
	public TestTableDTO select(String testColumn);

}
