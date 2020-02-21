package br.mp.cnmp.sistemacadastroadboot.repositories;

import javax.naming.Name;

import org.springframework.data.ldap.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;

@Repository
//public interface UsuarioRepo extends CrudRepository<Usuario, Name> 
//public interface UsuarioRepo extends LdapRepository<Usuario>
public interface UsuarioRepo extends CrudRepository<Usuario, Name>{

	@Query("(sAMAccountName={0})")
	Usuario findByLogin(String login);

	/*default <S extends Usuario> S save(S entity)

	default <S extends Usuario> Iterable<S> save(Iterable<S> entities)

	default Usuario findOne(Name id)urn null

	default boolean exists(Name id)

	default Iterable<Usuario> findAll()

	default Iterable<Usuario> findAll(Iterable<Name> ids)

	default long count()

	default void delete(Name id)

	default void delete(Usuario entity)

	default void delete(Iterable<? extends Usuario> entities)

	default void deleteAll()*/
	
	
}
