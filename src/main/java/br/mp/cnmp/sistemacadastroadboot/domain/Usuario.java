package br.mp.cnmp.sistemacadastroadboot.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.naming.Name;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapUtils;

//dn: cn=TesteSTI,ou=STI,o=cnmp
/* dn: CN=STI_TERC_MONTREAL,OU=STI,OU=DEPARTAMENTOS,OU=CNMP,DC=cnmp,DC=ad
objectClass: top
objectClass: group */

/* dn: CN=STI-ELDER,OU=COMPUTADORES-STI,OU=CNMP,DC=cnmp,DC=ad
objectClass: top
objectClass: person
objectClass: organizationalPerson
objectClass: user
objectClass: computer */

/* dn: CN=STI-S-1,OU=OBJETOS,OU=IMPRESSORAS,OU=CNMP,DC=cnmp,DC=ad
objectClass: top
objectClass: person
objectClass: organizationalPerson
objectClass: user */

/* dn: CN=HURLEY2,OU=Domain Controllers,DC=cnmp,DC=ad
objectClass: top
objectClass: person
objectClass: organizationalPerson
objectClass: user
objectClass: computer */
																					// remover os códigos comentados para usar UsuarioServiceImpl (usa odm)
@Entry(objectClasses = { "top", "person", "organizationalperson", "user" }, base="OU=eDirectory,DC=cnmp,DC=ad")
public class Usuario implements Comparable<Usuario>{

	// Distinguished Name
	@Id
	private Name id;
	
	@Transient
	@DnAttribute(value = "ou", index = 0)
    private String unidadeOrganizacional;
    
    @Attribute(name = "sAMAccountName")
    private String login;
    
    @Attribute(name = "mail")
    private String email;
    
    @Attribute(name = "telephoneNumber")
    private String telefone;
    
    @Attribute(name = "department")
    private String departamento;
    
    @Attribute(name = "description")
    private String descricao;

	@Attribute(name = "givenName")
	private String primeiroNome;
	
	@Attribute(name = "sn")
    private String sobrenome;

    @Attribute(name = "name")
    private String nomeCompleto;

    @Attribute(name = "title")
    private String titulo;
    
    @Attribute(name = "memberOf")
	private Set<Name> gruposSeguranca;

	@Attribute(name = "objectClass")
	private List<String> classesObjeto;

	@Attribute(name = "manager")
	private String chefe;
    
    @Transient
    private List<String> grupos;

    @Transient
	private String[] listas;
	
    /*@Transient
	private String[] impressoras;*/
	
    /* @Transient
	private String[] listasEmail;

    @Transient
	private boolean naoEnviarEmail;
	
    @Transient
	private String listaEmailUsuario;

    @Transient
    private String senhaInicial = "mudarsenh@";

    @Transient
	private String textoEmail;

    @Transient
	private String destinatarioEmail;

    @Transient
	private String[] copiasEmail; */
	
	/****************************** Getters and Setters ******************************/

	public Name getId() {
		return id;
	}
	
	public void setId(Name dn) {
		this.id = dn;
	}
	
	public void setId(String id) {
		this.id = LdapUtils.newLdapName(id);
	}

	public String getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(String unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPrimeiroNome() {
		return primeiroNome;
	}

	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Set<Name> getGruposSeguranca() {
		return gruposSeguranca;
	}

	public void setGruposSeguranca(Set<Name> gruposSeguranca) {
		this.gruposSeguranca = gruposSeguranca;
	}

	public List<String> getClassesObjeto() {
		return classesObjeto;
	}

	public void setClassesObjeto(List<String> classesObjeto) {
		this.classesObjeto = classesObjeto;
	}

	public String getChefe() {
		return chefe;
	}

	public void setChefe(String chefe) {
		this.chefe = chefe;
	}

	public List<String> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<String> grupos) {
		this.grupos = grupos;
	}

	public String[] getListas() {
		return listas;
	}

	public void setListas(String[] listas) {
		this.listas = listas;
	}

	/****************************** Fim Getters and Setters ******************************/
	
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	/*
	 * Métodos para ordenação de objetos "Usuario" por meio do atributo "login"
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Usuario usuario) {
		
		return this.login.compareToIgnoreCase(usuario.getLogin());
	}
	
	public static Comparator<Usuario> UsuarioNameComparator = new Comparator<Usuario>() {
	
		public int compare(Usuario u1, Usuario u2) {
		
		String u1Login = u1.getLogin().toUpperCase();
		String u2Login = u2.getLogin().toUpperCase();
		
		//ascending order
		return u1Login.compareTo(u2Login);
		}
	
	};

}
