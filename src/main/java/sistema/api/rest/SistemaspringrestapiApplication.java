package sistema.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

 /*
  * Autor: Anailson
  */
@SpringBootApplication
@EntityScan(basePackages = {"sistema.api.rest.model"}) /*Configura os pacotes base usados pela configuração automáticaao procurar classes de entidade.*/
@ComponentScan(basePackages = {"sistema.*"})	/*Configura diretivas de varredura de componentes para uso com @ Configurationclasses*/
@EnableJpaRepositories(basePackages = {"sistema.api.rest.repository"})	/*Anotação para ativar repositórios JPA*/
@EnableTransactionManagement	/*Permite o recurso de gerenciamento de transações orientado a anotações do Spring*/
@EnableWebMvc	/* A adição desta anotação a uma @Configurationclasse importa a configuração do Spring MVC*/
@RestController	/*Os tipos que carregam essa anotação são tratados como controladores,*/
@EnableAutoConfiguration	/* Ative a configuração automática do Contexto do Aplicativo Spring, tentando adivinhar e configurar os beans que você provavelmente precisará.*/
public class SistemaspringrestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaspringrestapiApplication.class, args);
	}

}
