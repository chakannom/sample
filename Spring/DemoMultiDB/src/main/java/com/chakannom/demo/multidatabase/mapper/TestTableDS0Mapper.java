package com.chakannom.demo.multidatabase.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.chakannom.demo.multidatabase.domain.TestTableDTO;
import com.chakannom.demo.multidatabase.mapper.annotation.DataSource0;

@DataSource0
public interface TestTableDS0Mapper {

	@Insert("insert into test_table (test_column) values (#{testColumn})")
	public void insert(TestTableDTO testTableDTO);

	@Select("select * from test_table where test_column = #{testColumn}")
	@Results(value = {
			@Result(property="testColumn", column="test_column")
	})
	public TestTableDTO select(String testColumn);

	@Update("update test_table set test_column = #{testColumn}")
	public void update(TestTableDTO testTableDTO);

	@Delete("delete from test_table where test_column = #{testColumn}")
	public void delete(String testColumn);

}
