package net.iliuqiang.webapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/about")
public class About {
	@RequestMapping()
	public String aboutMe(){
		return "redirect://iliuqiang.net";
	}
}
