package com.chakannom.demo.multidatabase;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.chakannom.demo.multidatabase.DemoMultiDBApplication;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoMultiDBApplication.class)
abstract public class AbstractTestableContext {
	
}
