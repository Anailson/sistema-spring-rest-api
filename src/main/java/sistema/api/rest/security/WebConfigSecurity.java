package sistema.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import sistema.api.rest.service.ImplementacaoUserDetailsService;

//MAPEIA URL, ENDEREÇOS, AUTORIZA OU BLOQUEIA ACESSO A URL
@Configuration
@EnableWebSecurity
public class WebConfigSecurity  extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService; //chamando a classe
	
	
	//CONFIGURA AS SOLICITAÇÕES DE ACESSO POR HTTPS
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//ATIVANDO A PROTAÇÃO CONTRA USUARIO QUE NÃO ESTÃO VALIDADOS POR TOKEN
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//verificar ser esta no codigo
		
		//ATIVANDO A PERMISSÃO DE ACESSO PAGINA INICIAL EX: SISTEMA.COM.BR/INDEX
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()//PERMITE TODOS ACESSO
		
		//permite varios tipos de uso no HTTP com portas e servidores diferentes
		.antMatchers(HttpMethod.OPTIONS,  "/**").permitAll()
		
			
		//URL DE LOGOUT - REDIRECIONAR APOS O USER DESLOGAR DO SISTEMA
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		//MAPEIA A URL DE LOGOUT E INVALIDA O USUARIO
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		
		// Filtra requisições de login para autenticação
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)
		
				
		// Filtra demais requisições para verificar a presença do TOKEN JWT no HEADER HTTP
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
	}
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		//SERVICE QUE IRA CONSULTA  O USUARIO NO BANCO DE DADOS
		auth.userDetailsService(implementacaoUserDetailsService)
		
		//PADRÃO DE CODIFICAÇÃO DE SENHA
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	

}
