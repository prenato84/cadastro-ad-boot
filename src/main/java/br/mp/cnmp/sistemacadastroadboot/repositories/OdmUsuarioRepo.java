package br.mp.cnmp.sistemacadastroadboot.repositories;

import java.util.List;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;

public interface OdmUsuarioRepo {

    public Usuario create(Usuario usuario);
 
    public Usuario findBySAMAccountName(String sAMAccountName);

    public void update(Usuario usuario);

    public void delete(Usuario usuario);

    public List<Usuario> findAll();
    
    public List<Usuario> findByLastName(String lastName);
}
