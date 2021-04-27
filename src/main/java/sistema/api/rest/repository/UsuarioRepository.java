package sistema.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sistema.api.rest.model.Usuario;

/*RESPONSÁVEL POR INSERT, DELETE, UPDATE*/
@Repository
public interface UsuarioRepository  extends CrudRepository<Usuario, Long>{

}
