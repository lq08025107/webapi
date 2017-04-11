package net.iliuqiang.webapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.sql.SQLNonTransientConnectionException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {
	private final static int oneDay = 24 * 60 * 60 * 1000;
	private final static String url = "http://localhost:8080/api/Bing_Pic";
	private CloseableHttpClient httpClient = null;
	private String remoteFileUrl = null;
	
	@Scheduled(fixedRate = oneDay)
	public void DownLoadPerDayTask() throws IOException{
		String localFilePath = "/work/images";
		httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/api/bing_pic");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		remoteFileUrl = EntityUtils.toString(entity, "UTF-8");
		String[] parts = remoteFileUrl.split("/");
		localFilePath += parts[parts.length-1];
		System.out.println(localFilePath);
		executeDownloadFile(httpClient, remoteFileUrl, localFilePath);
		localFilePath = "/work/images";
	}
	
	public static boolean executeDownloadFile(CloseableHttpClient httpClient, 
			String remoteFileUrl, 
			String localFilePath 
			) throws IOException{
		CloseableHttpResponse response = null;
		InputStream in = null;
		FileOutputStream fout = null;
		
		HttpGet httpGet = new HttpGet(remoteFileUrl);
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return false;
			}
			in = entity.getContent();
			File file = new File(localFilePath);
			fout = new FileOutputStream(file);
			int l = -1;
			byte[] tmp = new byte[2014];
			while ((l=in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
			}
			fout.flush();
			EntityUtils.consume(entity);
			return true;
		} finally{
			if (fout != null) {
				fout.close();
			}
			if(response != null){
				response.close();
			}
			if(httpClient != null){
				httpClient.close();
			}
		}
	}

}
