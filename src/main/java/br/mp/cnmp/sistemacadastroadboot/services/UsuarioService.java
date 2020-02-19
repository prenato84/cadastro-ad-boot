package br.mp.cnmp.sistemacadastroadboot.services;

import java.util.List;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;

public interface UsuarioService {

	List<Usuario> buscarTodos();
	
	boolean existeUsuario(String uid);

	Usuario buscarPorUid(String uid);

	void remover(Usuario usuario);

	Usuario salvar(Usuario usuario);

	Usuario atualizar(Usuario usuario);

}