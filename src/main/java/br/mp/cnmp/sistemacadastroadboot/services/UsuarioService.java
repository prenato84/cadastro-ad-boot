package br.mp.cnmp.sistemacadastroadboot.services;

import java.util.List;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;

public interface UsuarioService {

	List<Usuario> buscarTodos();
	
	boolean existeUsuario(String login);

	Usuario buscarPorLogin(String login);

	void remover(Usuario usuario);

	Usuario salvar(Usuario usuario);

	Usuario atualizar(Usuario usuario);

	long quantidadeUsuarios();

	public List<Usuario> testeBuscar();

}