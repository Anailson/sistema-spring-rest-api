package sistema.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import sistema.api.rest.model.Usuario;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	/* SERVIÇO RESTFULL */
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> init() {
		
		//PASSANDO VALORES PARA A MODEL USUARIO
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("anailson");
		usuario.setLogin("teste@gmail.com");
		usuario.setSenha("ffsfsf");
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(10L);
		usuario2.setNome("teste");
		usuario2.setLogin("javaspring@gmail.com");
		usuario2.setSenha("4510cssd");
		
		//RETORNANDO EM UMA LISTA DE USUARIOS
		List<Usuario> usuarios = new  ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.add(usuario2);
		
		
		return new ResponseEntity(usuarios, HttpStatus.OK);

	}

}
