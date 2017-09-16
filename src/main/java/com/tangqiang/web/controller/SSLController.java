package com.tangqiang.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SSLController {
	private static final String ATTR_CER = "javax.servlet.request.X509Certificate";
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String SCHEME_HTTPS = "https";

	@RequestMapping("ssl")
	public void sslCheck(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType(CONTENT_TYPE);
			response.setCharacterEncoding(DEFAULT_ENCODING);

			String scheme = request.getScheme();
			if (SCHEME_HTTPS.equalsIgnoreCase(scheme)) {
				PrintWriter printOut = response.getWriter();
				X509Certificate[] certs = (X509Certificate[]) request.getAttribute(ATTR_CER);

				if (certs != null) {
					int count = certs.length;
					printOut.println("共检测到[" + count + "]个客户端证书");
					for (int i = 0; i < count; i++) {
						printOut.println("客户端证书 [" + (++i) + "]： ");
						printOut.println("校验结果：" + verifyCertificate(certs[--i]));
						printOut.println("证书详细：\r" + certs[i].toString());
					}
				} else {
					printOut.println("这是一个HTTPS请求，但是没有可用的客户端证书");
				}
				printOut.close();
				
			} else {
				File clientKey = new ClassPathResource("key/client.p12").getFile();
				response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(clientKey.getName(), "UTF-8"));
				InputStream in = new FileInputStream(clientKey);
				OutputStream out = response.getOutputStream();// 输出流
				int len = 0;
				byte buf[] = new byte[1024];
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 校验证书是否过期
	 * </p>
	 * 
	 * @param certificate
	 * @return
	 */
	private boolean verifyCertificate(X509Certificate certificate) {
		boolean valid = true;
		try {
			certificate.checkValidity();
		} catch (Exception e) {
			e.printStackTrace();
			valid = false;
		}
		return valid;
	}

}