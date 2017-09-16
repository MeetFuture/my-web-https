package com.tangqiang.web;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * author
 *
 * @author acer-pc
 * @date 2017年3月1日 下午3:14:26
 * 
 * @version 1.0 2017年3月1日 acer-pc create
 *
 */
@SpringBootApplication
public class MyApplication extends SpringBootServletInitializer {
	private static Logger logger = LoggerFactory.getLogger(MyApplication.class);

	public static void main(String[] args) {
		logger.info("My Spring Boot Application Start at:" + new DateTime());
		SpringApplication.run(MyApplication.class, args);
		logger.info("My Spring Boot Application Started !");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MyApplication.class);
	}
}
