package net.iliuqiang.webapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLNonTransientConnectionException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController {
	private final static String baseUrl = "http://cn.bing.com";
	private final static String bingUrl = "http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1"; 
	private String picUrl= null;
	public static String xmlContent;
	static{
		xmlContent = getFromFile();
		System.out.println(xmlContent);
	}
	@RequestMapping(value = "/bing_pic", produces = "text/plain ; charset=utf-8")
	@ResponseBody
	public String getBingPic(){
		String content = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpGet httpGet = new HttpGet(bingUrl);
		
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				content = EntityUtils.toString(entity, "UTF-8");
				System.out.println(content);
			}
			try {
				Document document = DocumentHelper.parseText(content);
				Element root = document.getRootElement();
				Element image = root.element("image");
				Element url = image.element("url");
				picUrl = baseUrl + url.getText();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return picUrl;
		
	}
	@RequestMapping(value = "/china", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public void getProvince(){
		try {
			Document document = DocumentHelper.parseText(xmlContent);
			Element root = document.getRootElement();
			Element province = root.element("province");
			System.out.println(province.getText());
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("--------------province call-----------");
	}
	@RequestMapping(value = "/china/{province}", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public void getCity(){
		System.out.println("--------------city call-----------");
	}
	@RequestMapping(value = "/china/{province}/{city}", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public void getCounty(){
		System.out.println("--------------county call-----------");
	}
	
	private static String  getFromFile(){
		
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		// The path to the resource from the root of the JAR file
		Resource file = resourceResolver.getResource("classpath:/china_city_list.xml");
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String temp;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String xmlContent = sb.toString();
		return xmlContent;
	}

}
