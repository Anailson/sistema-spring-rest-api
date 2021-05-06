package sistema.api.rest.security;

import java.util.Date;

import javax.servlet.http.HttpServlet;
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
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {

		// MONTAGEM DO TOKEN
		String JWT = Jwts.builder()// chamar o gerador de token
				.setSubject(username)// adicionar o usuario
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();// compactaão e algoritmo de geração de senha

		// JUNtA O TOKNE COM O PREFIXO
		String token = TOKEN_PREFIX + "" + JWT; // bearer - ex: formando o token fafsfsfsdfsdfsd

		// ADICIONA NO CABEÇALHO HTTP
		response.addHeader(HEADER_STRING, JWT);// authorization: Bearer fsafsdfsdf

		// ESCREVE TOKEN COM RESPOSTA NO CORPO DO HTTP
		response.getWriter().write("{\"Authorization\":\"" + token + "\"}");

	}

	// RETORNA O USUÁRIO VALIDADO COM O TOKEN OU CASO NÃO SEJA VALIDO RETORNA NULL

	public Authentication getAuthentication(HttpServletResponse request) {

		// pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);

		if (token != null) { //ser não entra em nulll entra na codição

			// faz a validação do tokne do usuário na requisição
			String user = Jwts.parser().setSigningKey(SECRET)// Bear asfsfsdfds
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))// asfsfsdfds
					.getBody().getSubject();// exemplo : usuario: anailson

			if (user != null) {

				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findUserByLogin(user);

				if (user != null) {

					// PADRÃO DA DOCUMENTAÇÃO DO SPRING
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
							usuario.getAuthorities());

				}
			}

		}

		return null;// não autorizado
	}

}
