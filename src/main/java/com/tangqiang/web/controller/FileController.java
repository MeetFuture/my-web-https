package com.tangqiang.web.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.MultipartConfigElement;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 文件上传处理
 *
 * @author Tom
 * @date 2017年8月30日 下午9:07:14
 *
 * @version 1.0 2017年8月30日 Tom create
 * 
 * @copyright Copyright © 2017-???? 广电运通 All rights reserved.
 */
@RestController
public class FileController {
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	private String directory = "D:\\datafile\\";
	private String maxFileSize = "1024MB";

	@RequestMapping("file")
	public ModelAndView show() {
		return new ModelAndView("/screen/file/index.html");
	}

	@RequestMapping("FileUpload")
	public Map<String, Object> fileUpload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("result", false);
			String scheme = request.getScheme();
			boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
			logger.info("[" + scheme + "] file upload  isMultipartContent:" + isMultipartContent);
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

			// 获取字符串参数
			Enumeration<String> enumeration = multipartRequest.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				String value = multipartRequest.getParameter(name);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("fieldName", name);
				data.put("value", value);
				logger.info("Parameter name:" + name + "	value:" + value);
				result.put(name, data);
			}

			// 获取文件参数
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				Map<String, Object> data = new HashMap<String, Object>();
				String name = iterator.next();
				MultipartFile multipartFile = multipartRequest.getFile(name);
				String fileName = multipartFile.getOriginalFilename();
				int index = fileName.lastIndexOf(".");
				String fileType = fileName.substring(index);
				String preName = fileName.substring(0, index);
				String storeName = preName + UUID.randomUUID().toString().replace("-", "") + fileType;
				File storeFile = new File(directory + storeName);
				logger.info("Store File:" + fileName + "	Size:" + multipartFile.getSize() + "	Path:" + storeFile.getAbsolutePath());
				multipartFile.transferTo(storeFile);
				logger.info("Store File:" + fileName + " success !");
				data.put("fieldName", name);
				data.put("fileName", fileName);
				data.put("storeName", storeName);
				result.put(name, data);
			}
			result.put("result", true);
		} catch (Exception e) {
			logger.error("FileController fileUpload error ! ", e);
			result.put("error", e.getMessage());
		}
		return result;
	}
	
	@Bean  
    public MultipartConfigElement multipartConfigElement() {  
		MultipartConfigFactory factory = new MultipartConfigFactory();  
        factory.setMaxFileSize(maxFileSize);  
        factory.setMaxRequestSize(maxFileSize);  
        return factory.createMultipartConfig();  
    }  

}