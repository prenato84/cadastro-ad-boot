package br.mp.cnmp.sistemacadastroadboot.controllers;

import java.util.List;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;
import br.mp.cnmp.sistemacadastroadboot.services.UsuarioService;

@Controller
//@RequestMapping("usuario")
public class UsuarioController {
	
//	private static final Log logger = LogFactory.getLog(UsuarioController.class);
	
	private UsuarioService usuarioService;
	
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("usuarios")
	public String listarUsuarios(Model model) {

		model.addAttribute("usuarios", usuarioService.buscarTodos());

		System.out.println("Quantidade usuários: " + usuarioService.quantidadeUsuarios());

		List<Usuario> teste = usuarioService.testeBuscarLdapTemplate();

		System.out.println("Quantidade usuários LdapTemplate: " + teste.size());

		//Creates PowerShell session (we can execute several commands in the same session)
		/* try (PowerShell powerShell = PowerShell.openSession()) {
			
			String name = "'testepower'";
			String base = "'OU=STI,OU=eDirectory,DC=cnmp,DC=ad'";
			String senha = "'Mudarsenh@'";
			String departamento = "'STI'";
			String descricao = "'Matrícula: 82000'";
			String primeiroNome = "'Conta Teste'";
			String sobrenome = "'Powershell Script'";
			String telefone = "'9145'";
			String cargo = "'Técnico de Informática'";
			String email = "'testepower@cnmp.mp.br'";
			String nomePrincipal = "'testepower@cnmp.ad'";
			String nomeExibicao = "'Conta Teste Powershell Script'";
			String sala = "'Sala SE-07'";
			String chefe = "'CN=Leonardo da Costa Lopes,OU=STI,OU=eDirectory,DC=cnmp,DC=ad'";

			String comando = "New-ADUser -Name " + name + " -Path " + base +
							 " -AccountPassword(ConvertTo-SecureString -AsPlainText " + senha + " -Force)" +
							 " -ChangePasswordAtLogon $true -Enabled $true" +
							 " -OtherAttributes @{'department'=" + departamento + ";'description'=" + descricao + ";" +
							 "'givenName'=" + primeiroNome + ";'sn'=" + sobrenome + ";'telephoneNumber'=" + telefone + ";" +
							 "'title'=" + cargo +";'mail'=" + email + ";" +
							 "'userPrincipalName'=" + nomePrincipal + ";'displayName'=" + nomeExibicao + ";" +
							 "'physicalDeliveryOfficeName'=" + sala + ";'manager'=" + chefe + "}";

			//Execute a command in PowerShell session
			//PowerShellResponse response = powerShell.executeCommand("Get-ADUser -SearchBase 'OU=STI,OU=eDirectory,DC=cnmp,DC=ad' -filter * -properties PasswordExpired, PasswordLastSet, PasswordNeverExpires | ft Name, PasswordExpired, PasswordLastSet, PasswordNeverExpires");

			//Execute another command in the same PowerShell session
			//PowerShellResponse response = powerShell.executeCommand("New-ADUser -Name " + name + " -Path " + base + " -AccountPassword(ConvertTo-SecureString -AsPlainText " + senha + " -Force) -ChangePasswordAtLogon $true -Enabled $true -OtherAttributes @{'department'='STI';'description'='Matrícula: 82000';'givenName'='Conta Teste';'sn'='Powershell Script';'telephoneNumber'='9145';'title'='Técnico de Informática';'mail'='testepower@cnmp.mp.br';'userPrincipalName'='testepower@cnmp.ad';'displayName'='Conta Teste Powershell Script';'physicalDeliveryOfficeName'='Sala SE-07';'manager'='CN=Leonardo da Costa Lopes,OU=STI,OU=eDirectory,DC=cnmp,DC=ad'}");
	 
			PowerShellResponse response = powerShell.executeCommand(comando);
			//Print results
			//System.out.println("\nUsuário criado:" + response.getCommandOutput());
		
			if (response.getCommandOutput().contains("New-ADUser : A conta especificada já existe")) {
				System.out.println("A conta especificada já existe!");
			} else {
				System.out.println("Usuário " + name + " adicionado com êxito!");
			}
			

		} catch(PowerShellNotAvailableException ex) {
			//Handle error when PowerShell is not available in the system
			//Maybe try in another way?
	
			System.out.println(ex);
		} */

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
