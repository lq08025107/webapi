package net.iliuqiang.webapi;

import java.io.IOException;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController {
	private final static String baseUrl = "http://cn.bing.com";
	private final static String bingUrl = "http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1"; 
	private String picUrl= null;
	
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
		return picUrl;
		
	}
}
