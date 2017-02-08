package net.iliuqiang.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

//INSERT INTO PostForm (id, title, content, tags) VALUES (4, '我的第四条博客', '这是我的第四条博客', '测试');
public class CreateSQL {
	public static void main(String[] args) throws Exception{
		StringBuilder sb = new StringBuilder();
		String part1 = "insert into city (id, cityEn, cityZh, countryCode, countryEn, countryZh, provinceEn, provinceZh, leaderEn, leaderZh, lat,lon)";
		String part2 = "values";
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

		// The path to the resource from the root of the JAR file
		Resource file = resourceResolver.getResource("classpath:/china-city-list.txt");
		
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String tempString = null;
		while((tempString = reader.readLine()) != null){
			String[] temps = tempString.split("\t");
			String temppart3 = "(";
			for(int i = 0; i<temps.length; i++){
				if(i != temps.length-1 && i != temps.length-2)
					temppart3 = temppart3 + "'" + temps[i] + "'" + ",";
				else if(i == temps.length-2)
					temppart3 = temppart3 + temps[i] + ",";
				else {
					temppart3 = temppart3 + temps[i];
				}
			}
			temppart3 += ")";
			sb.append(part1).append(part2).append(temppart3).append(";");
			System.out.println(sb);
			sb.delete(0, sb.length());
		}
		reader.close();
		
	}
}
