package br.mp.cnmp.sistemacadastroadboot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

	@Value("${ldap.enabled}")
	private String ldapEnabled;

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

	// Configura a autenticação pelo LDAP (Spring Security LDAP) ou em memória para testes
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		if (Boolean.parseBoolean(ldapEnabled)) {
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
					/* base de busca dos grupos de permissões principais (usuário tem que ser membro de um dos grupos dessa OU, que posteriormente é
					filtrado na página HTML por meio do Thymeleaf Sec) */
					.groupSearchBase("ou=STI,ou=eDirectory,DC=cnmp,DC=ad")
					.groupSearchFilter("member={0}")
					.contextSource()
					.url(ldapUrl)
					.port(Integer.parseInt(ldapPort))
					.managerDn(ldapManagerDn)
					.managerPassword(ldapPasswordDn);
		} else {
			// usuários em memória (alterar a propriedade "ldap.enabled= false")
			auth.inMemoryAuthentication()
					.withUser("user").password("{noop}user").roles("USER")
					.and()
					.withUser("admin").password("{noop}admin").roles("GP_ZEN_ADMIN");
		}
	}

	// Exclui os diretórios abaixo da camada de segurança do Spring Security
	/* @Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    } */
}