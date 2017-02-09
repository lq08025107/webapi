package net.iliuqiang.webapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.iliuqiang.entity.County;
import net.iliuqiang.entity.ProvinceAndCity;

@Controller
@RequestMapping("/api")
public class APIController {
	private final static String baseUrl = "http://cn.bing.com";
	private final static String bingUrl = "http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1"; 
	private String picUrl= null;
	public static String xmlContent;
	static{
		xmlContent = getFromFile();
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
		System.out.println("-----------bing pic called by someone-------");
		return picUrl;
		
	}
	@RequestMapping(value = "/china", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public String getProvince(){
		String jsonObject;
		try {
			List<ProvinceAndCity> lists = new ArrayList<>();
			Document document = DocumentHelper.parseText(xmlContent);
			Element root = document.getRootElement();
			for(Iterator iterator = root.elementIterator("province"); iterator.hasNext(); ){
				ProvinceAndCity provinceAndCity = new ProvinceAndCity();
				Element province = (Element)iterator.next();
				int provinceId = Integer.valueOf(province.attributeValue("id"));
				String provinceName = province.attributeValue("name");
				provinceAndCity.setId(provinceId);
				provinceAndCity.setName(provinceName);
				lists.add(provinceAndCity);
			}
			Gson gson = new Gson();
			jsonObject = gson.toJson(lists);
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject = null;
		}
		System.out.println("----------Province called by someone----------");
		return jsonObject;
		
	}
	@RequestMapping(value = "/china/{provinceId}", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public String getCity(@PathVariable int provinceId){
		String jsonObject;
		try {
			List<ProvinceAndCity> lists = new ArrayList<>();
			Document document = DocumentHelper.parseText(xmlContent);
			Element root = document.getRootElement();
			
			for(Iterator iterator = root.elementIterator("province"); iterator.hasNext(); ){
				
				Element province = (Element)iterator.next();
				if(Integer.valueOf(province.attributeValue("id")) == provinceId){
					for(Iterator iterator2 = province.elementIterator("city"); iterator2.hasNext(); ){
						ProvinceAndCity provinceAndCity = new ProvinceAndCity();
						Element city = (Element)iterator2.next();
						int cityId = Integer.valueOf(city.attributeValue("id"));
						String cityName = city.attributeValue("name");
						provinceAndCity.setId(cityId);
						provinceAndCity.setName(cityName);
						lists.add(provinceAndCity);
					}
				}
			}
			Gson gson = new Gson();
			jsonObject = gson.toJson(lists);
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject = null;
		}
		System.out.println("----------City called by someone----------");
		return jsonObject;
		
	}
	@RequestMapping(value = "/china/{provinceId}/{cityId}", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public String getCounty(@PathVariable int provinceId, @PathVariable int cityId){
		String jsonObject;
		try {
			List<County> lists = new ArrayList<>();
			Document document = DocumentHelper.parseText(xmlContent);
			Element root = document.getRootElement();
			
			for(Iterator iterator = root.elementIterator("province"); iterator.hasNext(); ){
				
				Element province = (Element)iterator.next();
				if(Integer.valueOf(province.attributeValue("id")) == provinceId){
					for(Iterator iterator2 = province.elementIterator("city"); iterator2.hasNext(); ){
						
						Element city = (Element)iterator2.next();
						if(cityId == Integer.valueOf(city.attributeValue("id"))){
							for(Iterator iterator3 = city.elementIterator("county");iterator3.hasNext(); ){
								County countyObject = new County();
								Element county = (Element)iterator3.next();
								int countyId = Integer.valueOf(county.attributeValue("id"));
								String countyName = county.attributeValue("name");
								String weather_id = "CN"+county.attributeValue("weatherCode");
								countyObject.setId(countyId);
								countyObject.setName(countyName);
								countyObject.setWeather_id(weather_id);
								lists.add(countyObject);
							}
						}
						
						
					}
				}
			}
			Gson gson = new Gson();
			jsonObject = gson.toJson(lists);
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject = null;
		}
		System.out.println("----------County called by someone----------");
		return jsonObject;
	}
	@RequestMapping(value = "/weather", produces = "application/json ; charset=utf-8")
	@ResponseBody
	public String getWeather(@RequestParam("cityid") String cityId, @RequestParam("key") String key){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String jsonObject = null;
		String url = "https://free-api.heweather.com/v5/weather?"+"city="+cityId+"&key="+"39f17e6eeeb74f7e9b1e8456e6b29c96";
		HttpGet httpGet = new HttpGet(url);
		
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				jsonObject = EntityUtils.toString(entity, "UTF-8");
				
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jsonObject;
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
