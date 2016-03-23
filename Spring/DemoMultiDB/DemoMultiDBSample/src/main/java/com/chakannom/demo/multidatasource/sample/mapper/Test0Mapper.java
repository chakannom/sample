package com.chakannom.demo.multidatasource.sample.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.chakanframework.multidatasource.annotation.DataSource0;
import com.chakannom.demo.multidatasource.sample.domain.TestDTO;

@DataSource0
public interface Test0Mapper {

	@Insert("insert into test_table (test_column) values (#{testColumn})")
	public void insert(TestDTO testDTO);

	@Select("select * from test_table where test_column = #{testColumn}")
	@Results(value = {
			@Result(property="testColumn", column="test_column")
	})
	public TestDTO select(String testColumn);

	@Update("update test_table set test_column = #{testColumn}")
	public void update(TestDTO testDTO);

	@Delete("delete from test_table where test_column = #{testColumn}")
	public void delete(String testColumn);

}
