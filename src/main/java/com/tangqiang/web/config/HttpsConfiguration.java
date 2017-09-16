package com.tangqiang.web.config;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.tangqiang.web.MyApplication;

//@Configuration
public class HttpsConfiguration {
	private Logger logger = LoggerFactory.getLogger(MyApplication.class);

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(80);
		connector.setScheme("http");
		connector.setSecure(false);
		connector.setRedirectPort(443);
		return connector;
	}

	private Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		try {
			File keystore = new ClassPathResource("key/server.keystore").getFile();
			File truststore = new ClassPathResource("key/client.truststore").getFile();
			logger.info("keystore:" + keystore.getAbsolutePath() + "	truststore:" + truststore.getAbsolutePath());
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(443);
			protocol.setSSLEnabled(true);
			protocol.setKeystoreFile(keystore.getAbsolutePath());
			protocol.setKeystorePass("123456");
			protocol.setTruststoreFile(truststore.getAbsolutePath());
			protocol.setTruststorePass("123456");
			protocol.setKeyAlias("server");

			protocol.setClientAuth("true");
			// protocol.setClientAuth("want");
			return connector;
		} catch (Exception ex) {
			throw new IllegalStateException("can't access keystore: [" + "keystore" + "] or truststore: [" + "keystore" + "]", ex);
		}
	}
}