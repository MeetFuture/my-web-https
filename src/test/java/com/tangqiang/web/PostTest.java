package com.tangqiang.web;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;

public class PostTest extends TestCase {
	private Logger logger = LoggerFactory.getLogger(PostTest.class);

	public PostTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(PostTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}


	public void testHttpPost() {
		try {
			String url = "http://tom/post";
			CloseableHttpClient client = HttpClients.custom().build();
			HttpPost method = new HttpPost(url);

			String tokenInfo = "a;sdsasldi01[-12213cmassssj125@#$fsfe";
			String isZip = "false";

			HttpEntity entity = MultipartEntityBuilder.create().addPart("a", new StringBody(URLEncoder.encode(tokenInfo,"UTF-8"), ContentType.TEXT_PLAIN)).addTextBody("isZip", isZip)
					.build();
			method.setEntity(entity);
			HttpResponse response = client.execute(method);

			HttpEntity resEntity = response.getEntity();
			logger.info(EntityUtils.toString(resEntity, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
