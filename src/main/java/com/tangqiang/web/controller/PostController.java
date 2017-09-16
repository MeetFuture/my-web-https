package com.tangqiang.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class PostController {
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	private static final String DEFAULT_ENCODING = "UTF-8";

	@RequestMapping("post")
	public Map<String, Object> post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(DEFAULT_ENCODING);
		PrintWriter out = response.getWriter();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取字符串参数
		Enumeration<String> enumeration = multipartRequest.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String string = (String) enumeration.nextElement();
			String value = (String) multipartRequest.getParameter(string);
			String decode = URLDecoder.decode(value, "UTF-8");
			System.out.println(string + "    " + value + "    decode:" + decode);
		}

		out.close();
		return null;
	}

}