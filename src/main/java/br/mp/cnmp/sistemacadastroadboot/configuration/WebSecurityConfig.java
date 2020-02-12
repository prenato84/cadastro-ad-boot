package br.mp.cnmp.sistemacadastroadboot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${ldap.url}")
	private String ldapUrl;

	@Value("${ldap.port}")
	private String ldapPort;

	@Value("${ldap.user.search.base}")
	private String ldapUserSearchBase;

	@Value("${ldap.user.search.filter}")
	private String ldapUserSearchFilter;

	@Value("${ldap.group.search.base}")
	private String ldapGroupSearchBase;

	@Value("${ldap.group.search.filter}")
	private String ldapGroupSearchFilter;

	@Value("${ldap.manager.dn}")
	private String ldapManagerDn;

	@Value("${ldap.password.dn}")
	private String ldapPasswordDn;

	@Value("${ldap.enabled}")
	private String ldapEnabled;

	// Configura as autorizações/permissões
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/login").permitAll()
				//.antMatchers("/hello/**").access("hasRole('ADM_DOMINIO') and hasRole('USER')")
				.antMatchers("/hello/**").access("hasRole('ADM_DOMINIO')")
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

	// Configura a autenticação pelo LDAP ou em memória para testes
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		if (Boolean.parseBoolean(ldapEnabled)) {
			auth.ldapAuthentication()
					.userSearchBase(ldapUserSearchBase)
					.userSearchFilter("(" + ldapUserSearchFilter + ")")
					.groupSearchBase(ldapGroupSearchBase)
					.groupSearchFilter(ldapGroupSearchFilter)
					.contextSource()
					.url(ldapUrl)
					.port(Integer.parseInt(ldapPort))
					// .url("ldaps://hurley1.cnmp.ad/DC=cnmp,DC=ad")
					// .port(639)
					.managerDn(ldapManagerDn)
					.managerPassword(ldapPasswordDn);
		} else {
			auth
				.inMemoryAuthentication()
					.withUser("user").password("{noop}user").roles("USER")
					.and()
					.withUser("admin").password("{noop}admin").roles("ADMIN");
		}
	}

	// Exclui os diretórios abaixo da camada de segurança do Spring Security
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}