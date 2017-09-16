package com.tangqiang.web;

import java.io.File;
import java.io.FileInputStream;
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

public class MyWebTest extends TestCase {
	private Logger logger = LoggerFactory.getLogger(MyWebTest.class);
	private SSLConnectionSocketFactory socketFactory = null;

	public MyWebTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(MyWebTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		File clientStore = new ClassPathResource("key/client.p12").getFile();
		File serverStore = new ClassPathResource("key/server.truststore").getFile();

		KeyStore ksClient = KeyStore.getInstance("PKCS12");
		ksClient.load(new FileInputStream(clientStore), "123456".toCharArray());

		KeyStore ksServer = KeyStore.getInstance("JKS");
		ksServer.load(new FileInputStream(serverStore), "123456".toCharArray());

		logger.info("clientStore:" + clientStore.getAbsolutePath() + " ---------------------------");
		logger.info("serverStore:" + serverStore.getAbsolutePath() + " ---------------------------");
		SSLContext context = SSLContexts.custom()
//				.loadKeyMaterial(ksClient, "123456".toCharArray())
				.loadTrustMaterial(ksServer, null).build();
		socketFactory = new SSLConnectionSocketFactory(context);
	}

	private void Get() {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet get = new HttpGet("https://tom/");

			CloseableHttpResponse response = httpclient.execute(get);
			logger.info("StatusLine:" + response.getStatusLine());
			HttpEntity entity = response.getEntity();
			logger.info("HttpEntity:" + entity);

		} catch (Exception e) {
			logger.error("MyWebTest error! ", e);
		}
	}

	private void HttpsGet() {
		try {
			String url = "https://tom/";
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
			HttpGet method = new HttpGet(url);
			HttpResponse response = client.execute(method);
			HttpEntity entity = response.getEntity();
			logger.info(EntityUtils.toString(entity, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testHttpsPost() {
		try {
			String url = "https://tom/FileUpload";
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
			HttpPost method = new HttpPost(url);

			String tokenInfo = "tokenInfo";
			File upload = new File("D:\\datafile\\Feelvision_license.jar");
			String isZip = "false";

			HttpEntity entity = MultipartEntityBuilder.create().addPart("tokenInfo", new StringBody(tokenInfo, ContentType.TEXT_PLAIN)).addTextBody("isZip", isZip)
			// .addPart("file", new FileBody(upload))
					.addBinaryBody("file", upload).build();
			method.setEntity(entity);
			HttpResponse response = client.execute(method);

			HttpEntity resEntity = response.getEntity();
			logger.info(EntityUtils.toString(resEntity, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
