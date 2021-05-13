package sistema.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sistema.api.rest.model.Usuario;
import sistema.api.rest.repository.UsuarioRepository;

/*
 * SERVICE RESPONSÁVEL POR CHAMAR O REPOSITORY PARA FAZER AS OPERAÇÕES.
 * CONTROLLER É A CAMADA RESPONSAVEL TANTO POR RECEBER REQUISIÇÕES COMO POR ENVIAR A RESPOSTA AO USUÁRIO.
 * */
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	/* SERVIÇO RESTFULL */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id")Long id) {
		
	   Optional<Usuario> usuario = usuarioRepository.findById(id);
	   
	   return new ResponseEntity(usuario.get(), HttpStatus.OK);
		
	}
	/*LISTANDO TODOS OS IDs*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario(){
		
		 List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		 
		 return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	/*SALVANDO REGISTROS PELO POSTMAN USUARIO E TELEFONES*/
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar (@RequestBody Usuario usuario){
		
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		//CRIPTOGRANDO A SENHA DOS USUÁRIOS A SER CADASTRADOS
		String senhaCriptograda = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptograda);
		
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	
	/*---------------ATUALIZANDO--------------------------*/
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar (@RequestBody Usuario usuario){
		
		//ASSOCIANDO TELEFONE AO USUARIO
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
	
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	/*----------------DELETE-----------------*/
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete (@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "ok";
	}
	

}
