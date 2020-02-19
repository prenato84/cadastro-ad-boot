package br.mp.cnmp.sistemacadastroadboot.domain;

import java.util.Comparator;
import java.util.Set;

import javax.naming.Name;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapUtils;

//dn: cn=TesteSTI,ou=STI,o=cnmp
																					// remover os códigos comentados para usar UsuarioServiceImpl (usa odm)
@Entry(objectClasses = { "inetorgperson", "organizationalperson", "person", "top" }/*, base="o=cnmp"*/)
public class Usuario implements Comparable<Usuario>{

	@Id
	private Name id;
	
	@Transient
	@DnAttribute(value = "ou", index = 0)
    private String contexto;
	
	@Transient
    @DnAttribute(value = "cn", index = 0)
    private String login;
    
    @Attribute(name = "uid")
    private String uid;

    @Attribute(name = "givenName")
    private String primeiroNome;
    
    @Attribute(name = "mail")
    private String email;
    
    @Attribute(name = "telephoneNumber")
    private String telefone;
    
    @Attribute(name = "l")
    private String localizacao;
    
    @Attribute(name = "description")
    private String descricao;
    
    @Attribute(name = "ou")
    private String departamento;
    
    @Attribute(name = "sn")
    private String sobrenome;

    @Attribute(name = "fullName")
    private String nomeCompleto;

    @Attribute(name = "title")
    private String titulo;
    
    @Attribute(name = "groupMembership")
	private Set<Name> permissoes;
    
    @Attribute(name = "securityEquals")
	private Set<Name> permissoesSeguranca;
    
    @Attribute(name = "iPrintiCMPrinterList")
	private Set<Name> impressoras;
    
    @Transient
    private String[] grupos;

    @Transient
	private String[] listas;
	
    /*@Transient
	private String[] impressoras;*/
	
    @Transient
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
	private String[] copiasEmail;
    
    /*@Transient
    private List<String> permissoes; //groupMembership */
	
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

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPrimeiroNome() {
		return primeiroNome;
	}

	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
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

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
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
	
	public Set<Name> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Name> permissoes) {
		this.permissoes = permissoes;
	}
	
	public Set<Name> getPermissoesSeguranca() {
		return permissoesSeguranca;
	}

	public void setPermissoesSeguranca(Set<Name> permissoesSeguranca) {
		this.permissoesSeguranca = permissoesSeguranca;
	}

	public Set<Name> getImpressoras() {
		return impressoras;
	}

	public void setImpressoras(Set<Name> impressoras) {
		this.impressoras = impressoras;
	}

	public String[] getGrupos() {
		return grupos;
	}

	public void setGrupos(String[] grupos) {
		this.grupos = grupos;
	}

	public String[] getListas() {
		return listas;
	}

	public void setListas(String[] listas) {
		this.listas = listas;
	}

	/*public String[] getImpressoras() {
		return impressoras;
	}

	public void setImpressoras(String[] impressoras) {
		this.impressoras = impressoras;
	}*/

	public String[] getListasEmail() {
		return listasEmail;
	}

	public void setListasEmail(String[] listasEmail) {
		this.listasEmail = listasEmail;
	}

	public boolean isNaoEnviarEmail() {
		return naoEnviarEmail;
	}

	public void setNaoEnviarEmail(boolean naoEnviarEmail) {
		this.naoEnviarEmail = naoEnviarEmail;
	}

	public String getListaEmailUsuario() {
		return listaEmailUsuario;
	}

	public void setListaEmailUsuario(String listaEmailUsuario) {
		this.listaEmailUsuario = listaEmailUsuario;
	}

	public String getSenhaInicial() {
		return senhaInicial;
	}

	public void setSenhaInicial(String senhaInicial) {
		this.senhaInicial = senhaInicial;
	}

	public String getTextoEmail() {
		return textoEmail;
	}

	public void setTextoEmail(String textoEmail) {
		this.textoEmail = textoEmail;
	}

	public String getDestinatarioEmail() {
		return destinatarioEmail;
	}

	public void setDestinatarioEmail(String destinatarioEmail) {
		this.destinatarioEmail = destinatarioEmail;
	}

	public String[] getCopiasEmail() {
		return copiasEmail;
	}

	public void setCopiasEmail(String[] copiasEmail) {
		this.copiasEmail = copiasEmail;
	}

	/****************************** Getters and Setters End ******************************/
	
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
	 * Métodos para ordenação de objetos "Usuario" por meio do atributo "uid"
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Usuario usuario) {
		
		return this.uid.compareToIgnoreCase(usuario.getUid());
	}
	
	public static Comparator<Usuario> UsuarioNameComparator = new Comparator<Usuario>() {
	
		public int compare(Usuario u1, Usuario u2) {
		
		String u1Login = u1.getUid().toUpperCase();
		String u2Login = u2.getUid().toUpperCase();
		
		//ascending order
		return u1Login.compareTo(u2Login);
		}
	
	};

}
