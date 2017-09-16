package com.tangqiang.web;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	private Logger logger = LoggerFactory.getLogger(AppTest.class);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	public void testApp() throws Exception {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpGet get = new HttpGet("https://www.baidu.com/");

			CloseableHttpResponse response = httpclient.execute(get);
			logger.info("StatusLine:" + response.getStatusLine());
			HttpEntity entity = response.getEntity();
			logger.info("HttpEntity:" + entity);
		} catch (Exception e) {
			logger.error("testApp error! ", e);
		}
	}
}
