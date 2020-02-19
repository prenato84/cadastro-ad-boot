package br.mp.cnmp.sistemacadastroadboot.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

/*
** Classe responsável pela configuração, autenticação, autorização e permissão de acesso do Spring Security e do Spring Security LDAP
*/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Associa os valores setados em application.properties
	@Value("${ldap.url}")
	private String ldapUrl;

	@Value("${ldap.port}")
	private String ldapPort;

	@Value("${ldap.manager.dn}")
	private String ldapManagerDn;

	@Value("${ldap.password.dn}")
	private String ldapPasswordDn;

	@Value("${ldap.embedded}")
	private String ldapEmbedded;

	// Configura as autorizações/permissões de segurança do Spring Security
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// essa configuração substitui o último método dessa classe, autorizando o acesso aos recursos estáticos
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				//.antMatchers("/", "/login", "/webjars/**").permitAll()
				//.antMatchers("/hello/**").access("hasRole('ADM_DOMINIO') and hasRole('USER')")
				.antMatchers("/hello/**").access("hasRole('GP_ZEN_ADMIN')")
				.anyRequest()
				.authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/hello")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}

	// Configura a autenticação pelo LDAP (Spring Security LDAP)
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		// se ldapEmbedded = false, a autenticação ocorrerá no servidor AD hurley1.cnmp.ad
		if (!Boolean.parseBoolean(ldapEmbedded)) {
			
			ActiveDirectoryLdapAuthenticationProvider adProvider = 
				new ActiveDirectoryLdapAuthenticationProvider("cnmp.ad","ldap://hurley1.cnmp.ad:389");
			adProvider.setConvertSubErrorCodesToExceptions(true);
			adProvider.setUseAuthenticationRequestCredentials(true);
	
			// set pattern if it exists
			// The following example would authenticate a user if they were a member
			// of the ServiceAccounts group
			// (&(objectClass=user)(userPrincipalName={0})
			//   (memberof=CN=ServiceAccounts,OU=alfresco,DC=mycompany,DC=com))
			adProvider.setSearchFilter("(&(objectClass=user)(userPrincipalName={0})(memberOf=CN=GP_ZEN_ADMIN,OU=STI,OU=eDirectory,DC=cnmp,DC=ad))");
			
			auth.authenticationProvider(adProvider);
			
			// don't erase credentials if you plan to get them later
			// (e.g using them for another web service call)
			auth.eraseCredentials(false);
			
			/* 
			// usuários da OU=STI
			auth.ldapAuthentication()
					.userSearchBase("ou=STI,ou=eDirectory,DC=cnmp,DC=ad")
					.userSearchFilter("(sAMAccountName={0})")
					.groupSearchBase("ou=STI,ou=eDirectory,DC=cnmp,DC=ad")
					.groupSearchFilter("member={0}")
					.contextSource()
					.url(ldapUrl)
					.port(Integer.parseInt(ldapPort))
					.managerDn(ldapManagerDn)
					.managerPassword(ldapPasswordDn);
			// usuários da OU=Terceirizados
			auth.ldapAuthentication()
					.userSearchBase("ou=Terceirizados,ou=eDirectory,DC=cnmp,DC=ad")
					.userSearchFilter("(sAMAccountName={0})")
					// base de busca dos grupos de permissões principais (usuário tem que ser membro de um dos grupos dessa OU, que posteriormente é
					// filtrado na página HTML por meio do Thymeleaf Sec)
					.groupSearchBase("ou=STI,ou=eDirectory,DC=cnmp,DC=ad")
					.groupSearchFilter("member={0}")
					.contextSource()
					.url(ldapUrl)
					.port(Integer.parseInt(ldapPort))
					.managerDn(ldapManagerDn)
					.managerPassword(ldapPasswordDn);

		// ldapEmbedded = true => servidor de teste LDAP UnboundID */
		} else {
			
			// usuários com senha setadas manualmente no CNMP_AD.ldif (https://bcrypt-generator.com/):
			// testesti, [senha], ou=STI, memberOf=não tem GP_ZEN_ADMIN
			// tercservsat, [senha], ou=Terceirizados, memberOf=GP_ZEN_ADMIN
			// ldapUserApp, [senha], ou=STI, memberOf=GP_ZEN_ADMIN
			auth.ldapAuthentication()
				.userSearchFilter("sAMAccountName={0}")
				.userSearchBase("ou=eDirectory,DC=cnmp,DC=ad")
				.groupSearchBase("ou=STI,ou=eDirectory,DC=cnmp,DC=ad")
				.groupSearchFilter("member={0}")
				.contextSource()
					.url("ldap://localhost:389")
					.and()
					.passwordCompare()
						.passwordEncoder(new BCryptPasswordEncoder())
						.passwordAttribute("userPassword");
		}
	}

	/*
	** Autenticação pelo servidor embedded LDAP de TESTE (UnboundID)  
	 */
	/*@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		if (Boolean.parseBoolean(ldapEmbedded)) {
			return  new DefaultSpringSecurityContextSource(Arrays.asList("ldap://localhost:" 
						+ env.getRequiredProperty("spring.ldap.embedded.port") + "/"), env.getRequiredProperty("spring.ldap.embedded.base-dn"));
		} else {
			return  new DefaultSpringSecurityContextSource(Arrays.asList("ldap://hurley1.cnmp.ad:" 
						+ "389" + "/"), "DC=cnmp,DC=ad");
		}
	}*/

	// Exclui os diretórios abaixo da camada de segurança do Spring Security
	/* @Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    } */
}