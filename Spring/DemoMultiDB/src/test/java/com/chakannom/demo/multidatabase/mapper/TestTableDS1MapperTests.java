package com.chakannom.demo.multidatabase.mapper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chakannom.demo.multidatabase.AbstractTestableContext;
import com.chakannom.demo.multidatabase.domain.TestTableDTO;

public class TestTableDS1MapperTests extends AbstractTestableContext {

	@Autowired
	private TestTableDS1Mapper testTableReadOnlyMapper;

	@Test
	public void select() {
		TestTableDTO testTableDTO = testTableReadOnlyMapper.select("TEST_DATA_TEST");
		assertThat(testTableDTO, notNullValue());
		System.out.println(testTableDTO.getTestColumn());
	}
}
