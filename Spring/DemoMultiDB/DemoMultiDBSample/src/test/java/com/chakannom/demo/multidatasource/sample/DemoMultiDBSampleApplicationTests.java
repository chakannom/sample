package com.chakannom.demo.multidatasource.sample;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.chakannom.demo.multidatasource.sample.domain.TestDTO;
import com.chakannom.demo.multidatasource.sample.mapper.Test0Mapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoMultiDBSampleApplication.class)
@WebAppConfiguration
public class DemoMultiDBSampleApplicationTests {



	@Autowired
	private Test0Mapper test0Mapper;

	@Test
	public void select() {
		TestDTO testDTO = test0Mapper.select("TEST_DATA_TEST");
		assertThat(testDTO, notNullValue());
		System.out.println(testDTO.getTestColumn());
	}
}
