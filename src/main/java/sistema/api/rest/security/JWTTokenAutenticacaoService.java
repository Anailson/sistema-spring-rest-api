package sistema.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sistema.api.rest.ApplicationContextLoad;
import sistema.api.rest.model.Usuario;
import sistema.api.rest.repository.UsuarioRepository;

@Service
@Component
public class JWTTokenAutenticacaoService {

	// TEMPO DE VALIDADE DO TOKEN 2 DIAS - A CADA 2 DIAS UMA NOVA AUTENTICAÇÃO
	// http://extraconversion.com/pt/tempo/dias/dias-para-milissegundos.html
	private static final long EXPIRATION_TIME = 259200000;

	// UMA SENHA UNICA PARA COMPOR A AUTENTIFCAÇÃO E AJUDA NA SEGURANÇA;
	private static final String SECRET = "senhaExtremamenteSecreta";

	// PREFIXO PADRÃO DE TOKEN
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	// GERANDO TOKEN DE AUTENTICAÇÃO E ADICIONANDO AO CABEÇALHO A RESPOSTA HTTP
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		// MONTAGEM DO TOKEN
		String JWT = Jwts.builder()// chamar o gerador de token
				.setSubject(username)// adicionar o usuario
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();// compactaão e algoritmo de geração de senha

		// JUNtA O TOKNE COM O PREFIXO
		String token = TOKEN_PREFIX + " " + JWT; // bearer - ex: formando o token fafsfsfsdfsdfsd

		// ADICIONA NO CABEÇALHO HTTP
		response.addHeader(HEADER_STRING, token);// authorization: Bearer fsafsdfsdf

		// liberando resposta para as portas diferentes que usam API ou Caso clientes
		// web: exemplo: porta 8080 e porta 4900
		liberacaoCors(response);

		// ESCREVE TOKEN COM RESPOSTA NO CORPO DO HTTP
		response.getWriter().write("{\"Authorization\":\"" + token + "\"}");

	}

	// RETORNA O USUÁRIO VALIDADO COM O TOKEN OU CASO NÃO SEJA VALIDO RETORNA NULL

	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		// pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);

	 try { //capturando exceção do token expirado
		if (token != null) { //ser não entra em nulll entra na codição
			
			//TER UM TOKE LMPO SEM ESPAÇOS EXEMPLO: Bearer eyJhbGciOiJIUzUxMiJ9
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();//trim limpar espaços

			// faz a validação do tokne do usuário na requisição
			String user = Jwts.parser().setSigningKey(SECRET)// Bear asfsfsdfds
					.parseClaimsJws(tokenLimpo)// asfsfsdfds
					.getBody().getSubject();// exemplo : usuario: anailson

	
			if (user != null) {

				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findUserByLogin(user);

				if (usuario != null) {
					
					//SER O TOKEN FOI IGUAL DO BANCO DE DADOS É VALIDO - PASSA A VALIDAÇÃO O ACESSO LIBERADO
					if(tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
						
					// PADRÃO DA DOCUMENTAÇÃO DO SPRING
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(),
							usuario.getAuthorities());
					}

				}
			}
			
		}// fim da codição token != null
	 }catch (io.jsonwebtoken.ExpiredJwtException e) {
		try {
			response.getOutputStream().println("Seu TOKEN está expirado, faça o login ou informe umn novo token para autenticação");
		}catch (IOException el) {}
	}
		//LIBERACAO CORS
		liberacaoCors(response);
		
		return null;// não autorizado
	}

	// liberação do cors metodo criado apos a liberacaoCors
	private void liberacaoCors(HttpServletResponse response) {

		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

	}

}
