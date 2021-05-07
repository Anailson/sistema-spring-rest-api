package sistema.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import sistema.api.rest.model.Usuario;

//ESTABELE O NOSSO GERENCIADOR DE TOKEN
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	//CONFIGURANDO O GERENCIADOR DE AUTENTICAÇÃO
	
	protected JWTLoginFilter(String url,AuthenticationManager authenticationManager) {
		
		//obriga a autenticar a URL
		super(new AntPathRequestMatcher(url));

		//Gerenciador de autenticação
		setAuthenticationManager(authenticationManager);
	}

	//Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		//Esta pegando o Token para validar
		Usuario user = new ObjectMapper().
				readValue(request.getInputStream(), Usuario.class);
		
		//Retorna o usuário Login e Senha e acessos
		return getAuthenticationManager().
				authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}
	
	//Usuário Valido
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		

		 new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
	}

}
