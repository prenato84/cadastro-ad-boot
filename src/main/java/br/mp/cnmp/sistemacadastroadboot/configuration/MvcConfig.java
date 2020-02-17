package br.mp.cnmp.sistemacadastroadboot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
** Classe que configura a Web Controller do Spring MVC expondo as views simples
*/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
	}

}

