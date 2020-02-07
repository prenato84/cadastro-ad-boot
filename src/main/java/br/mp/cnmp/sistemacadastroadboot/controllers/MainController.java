package br.mp.cnmp.sistemacadastroadboot.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@Autowired
    public MainController() {
		super();
	}
	
/*	@RequestMapping("/403")
	public String accessDenied() {
	    return "errors/403";
	}*/

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/default")
	public String defaultAfterLogin(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:admin/cadastrar_usuario";
        } else if (request.isUserInRole("ROLE_USER")) {
            return "redirect:user/hello";
        }
        return "redirect:index";
	}
	
}
