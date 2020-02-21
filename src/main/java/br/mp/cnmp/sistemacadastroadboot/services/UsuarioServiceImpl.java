package br.mp.cnmp.sistemacadastroadboot.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;
import br.mp.cnmp.sistemacadastroadboot.repositories.UsuarioRepo;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	//private static final Log logger = LogFactory.getLog(UsuarioServiceImpl.class);
	
	private LdapTemplate ldapTemplate;
	private UsuarioRepo usuarioRepo;
	
	@Autowired
	public UsuarioServiceImpl(LdapTemplate ldapTemplate, UsuarioRepo usuarioRepo) {
		this.ldapTemplate = ldapTemplate;
		this.usuarioRepo = usuarioRepo;
	}

	@Override
	public List<Usuario> buscarTodos() {

		Iterable<Usuario> usuariosBuscados = usuarioRepo.findAll();
		List<Usuario> usuarios = new ArrayList<>();
		
		List<Usuario> usuariosSemId = new ArrayList<>();
		List<Usuario> usuariosSemGrupo = new ArrayList<>();
		
		for (Usuario usuario : usuariosBuscados) {
			String id = usuario.getId().toString(); //id=cn=OESCommonProxy_iprint,ou=iPrint
			//System.out.println("ID = " + id);
			
			if ( usuario.getId().isEmpty() ) { // usuários com id vazio
				usuariosSemId.add(usuario);
			// Filtrar pelos seguintes Conextos: IMPRESSORAS, COMPUTADORES, Users, COMPUTADORES DESCONHECIDOS, COMPUTADORES-STI, NOTEBOOKS, Domain Controllers, 
			// melhorar pra filtrar por tipoo do objeto
			} else if ( id.contains("OU=") ) {
				// id=CN=PESSOAL,OU=USUARIOS,OU=CNMP,DC=cnmp,DC=ad
				// id=CN=Paulo Renato Alves de Melo Castro,OU=STI,OU=eDirectory,DC=cnmp,DC=ad
				String contexto = id.split(",OU=")[1];
				//System.out.println("CONTEXTO = " + contexto);
				if (!contexto.contentEquals("IMPRESSORAS") && !contexto.contentEquals("COMPUTADORES") && !contexto.contentEquals("Users") 
					&& !contexto.contentEquals("COMPUTADORES DESCONHECIDOS") && !contexto.contentEquals("COMPUTADORES-STI") && !contexto.contentEquals("NOTEBOOKS") 
					&& !contexto.contentEquals("Domain Controllers")) {
					
					configuraAtributosUsuario(usuario);
					usuarios.add(usuario);
				}
			} else { // usuários sem contexto (ou)
				usuariosSemGrupo.add(usuario);
			}
		}
		
		//System.out.println("Usuários sem ID: " + usuariosSemId);
		//System.out.println("Usuários sem grupo: " + usuariosSemGrupo);
		/* for (Usuario usuario : usuarios) {
			System.out.println("Usuário: " + usuario + "\n");
		} */
		
		//Collections.sort((List<Usuario>) usuarios);
		
		//usuariosBuscados.forEach(usuarios::add);
		
		return usuarios;
	}
	
	@Override
	public boolean existeUsuario(String login) {
		
		Usuario usuario = usuarioRepo.findByLogin(login);
		
		if (usuario == null) {
			return false;
		}

		return true;
	}
	
	@Override
	public Usuario buscarPorLogin(String login) {
		Usuario usuario = usuarioRepo.findByLogin(login);
		
		configuraAtributosUsuario(usuario);
		
		return usuario; 
	}
	
	@Override
	public void remover(Usuario usuario) {
		usuarioRepo.delete(usuario);
	}
	
	private void configuraAtributosUsuario(Usuario usuario) {
		
		String id = usuario.getId().toString();
		String chefe = usuario.getChefe();
		
		if ( id.contains("OU=") ) {
			// id=CN=PESSOAL,OU=USUARIOS,OU=CNMP,DC=cnmp,DC=ad
			// id=CN=Paulo Renato Alves de Melo Castro,OU=STI,OU=eDirectory,DC=cnmp,DC=ad
			String contexto = id.split(",")[1];
			
			usuario.setLogin(usuario.getLogin());
			usuario.setUnidadeOrganizacional(contexto.substring(3));
		}

		if (chefe != null && !chefe.isEmpty()) {
			String nomeChefe = chefe.split(",OU=")[0];

			usuario.setChefe(nomeChefe.substring(3));
		}	


	}
	
	@Override
	public Usuario salvar(Usuario usuario) {
		//logger.info("Usuario antes de salvar: " + usuario);
		
		if (usuario.getId().isEmpty()) { //caso esteja criando um novo usuário
			//logger.info("********* usuário com ID vazio - Criando Novo Usuário *********");
		    
			return criar(usuario);
		} else { //caso esteja alterando um usuário existente
			//logger.info("********* Alterando Usuário Existente *********");
			//configuraContextoELogin(usuario);
			
			//Usuario usuarioSalvo = usuarioRepo.save(usuario);
			return atualizar(usuario);
		}
	}

	private Usuario criar(Usuario usuario) {
		
		/*usuario.setLogin(usuario.getUid());
		usuario.setId("cn=" + usuario.getUid() + ",ou=" + usuario.getContexto());*/
		
		Name id = buildId(usuario);
		
		if ( usuarioRepo.existsById(id)) {
			return null;
		} else {
			DirContextAdapter context = new DirContextAdapter(id);
			mapToContext(usuario, context);
			
			ldapTemplate.bind(context);
			
			adicionarAtributosSeguranca(usuario);
			
			return usuario;
		}	
	}
	
	@Override
	public Usuario atualizar(Usuario usuario) {
		/*ldapTemplate.update(usuario);*/
		
		Name dn = usuario.getId();
	    DirContextOperations context = ldapTemplate.lookupContext(dn);
	    
	    mapToContext(usuario, context);
	    ldapTemplate.modifyAttributes(context);
	    
	    return usuario;
	}
	
	//id: cn=TesteSTI,ou=STI,o=cnmp
	private Name buildId(Usuario usuario) {
		/*Name dn = buildDn(usuario.getContexto(), usuario.getLogin());*/
		Name id = buildId(usuario.getUnidadeOrganizacional(), usuario.getLogin());
		return id;
	}

	private Name buildId(String contexto, String login) {
		return LdapNameBuilder.newInstance()
				/*.add("o", DN_BASE)*/
				.add("ou", contexto)
				.add("cn", login)
				.build();
	}
	
	private void adicionarAtributosSeguranca(Usuario usuario) {
		try {
			Calendar utcTime = Calendar.getInstance();
			Date date = utcTime.getTime();
			DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss'Z'");

			ModificationItem[] itens = new ModificationItem[7];

			ModificationItem item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("passworduniquerequired", "TRUE"));
			itens[0] = item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE,
					new BasicAttribute("passwordexpirationinterval", "31536000"));
			itens[1] = item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("logingraceremaining", "6"));
			itens[2] = item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("logingracelimit", "6"));
			itens[3] = item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("passwordminimumlength", "6"));
			itens[4] = item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("passwordrequired", "TRUE"));
			itens[5] = item;
			item = new ModificationItem(DirContext.ADD_ATTRIBUTE,
					new BasicAttribute("passwordexpirationtime", formatter.format(date)));
			itens[6] = item;

			ldapTemplate.modifyAttributes(buildId(usuario), itens);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* objectclass: top
	 * objectclass: person
	 * objectclass: organizationalPerson
	 * objectclass: inetOrgPerson
	 * cn: TesteSTI
	 * uid: TesteSTI
	 * givenName: Teste
	 * sn: STI
	 * userPassword:
	 * fullName: Teste STI
	 * mail: TesteSTI@cnmp.mp.br
	 * title: Técnico de Informática
	 * telephoneNumber: 3366-9100
	 * l: SE-07
	 * description: Matrícula: 82000
	 * ou: SERVSAT
	 */
	
	private void mapToContext (Usuario usuario, DirContextOperations context) {
		context.setAttributeValues("objectclass", new String[] {"top", "person", "organizationalperson", "user"});
		
		context.setAttributeValue("cn", usuario.getLogin());
		context.setAttributeValue("sn", usuario.getSobrenome());
		context.setAttributeValue("title", usuario.getTitulo());
		context.setAttributeValue("description", "Matrícula: " + usuario.getDescricao());
		if (usuario.getTelefone().length() != 0) {
			context.setAttributeValue("telephonenumber", usuario.getTelefone());
		}
		context.setAttributeValue("givenName", usuario.getPrimeiroNome());
		context.setAttributeValue("displayName", usuario.getNomeCompleto());
		context.setAttributeValue("department", usuario.getDepartamento());

		context.setAttributeValue("ou", usuario.getDepartamento());
		context.setAttributeValue("name", usuario.getNomeCompleto());
		context.setAttributeValue("sAMAccountName", usuario.getLogin());
		context.setAttributeValue("userPrincipalName", usuario.getLogin() + "@cnmp.ad");

		context.setAttributeValue("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=cnmp,DC=ad");

		context.setAttributeValue("mail", usuario.getEmail());	
		if (usuario.getChefe().length() != 0) {
			context.setAttributeValue("manager", usuario.getChefe());
		}		
		
	}
	
}
