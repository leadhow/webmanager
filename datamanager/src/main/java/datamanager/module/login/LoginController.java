package datamanager.module.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import datamanager.springmvc.RestResponseBean;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	public RestResponseBean login(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("start login");
		return new RestResponseBean("0", "", "login success");
	}

}
