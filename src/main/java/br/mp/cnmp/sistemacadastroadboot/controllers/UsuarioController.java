package br.mp.cnmp.sistemacadastroadboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("usuario")
public class UsuarioController {
	
	@GetMapping("usuarios")
	public String listarUsuarios(Model model) {

		model.addAttribute("usuarios", usuarioService.buscarTodos());

	    return "hello";
	}

	/* @RequestMapping(value = "/default")
	public String defaultAfterLogin(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:admin/cadastrar_usuario";
        } else if (request.isUserInRole("ROLE_USER")) {
            return "redirect:user/hello";
        }
        return "redirect:index";
	} */
	
}
