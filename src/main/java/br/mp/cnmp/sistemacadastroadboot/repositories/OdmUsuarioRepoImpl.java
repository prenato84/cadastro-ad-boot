package br.mp.cnmp.sistemacadastroadboot.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import br.mp.cnmp.sistemacadastroadboot.domain.Usuario;

@Service
public class OdmUsuarioRepoImpl implements OdmUsuarioRepo {
    
    @Autowired
    private LdapTemplate ldapTemplate;
 
    @Override
    public Usuario create(Usuario usuario) {
       ldapTemplate.create(usuario);
       return usuario;
    }
 
    @Override
    public Usuario findBySAMAccountName(String sAMAccountName) {
       return ldapTemplate.findOne(query().where("sAMAccountName").is(sAMAccountName), Usuario.class);
    }
 
    @Override
    public void update(Usuario usuario) {
       ldapTemplate.update(usuario);
    }
 
    @Override
    public void delete(Usuario usuario) {
       ldapTemplate.delete(usuario);
    }
 
    @Override
    public List<Usuario> findAll() {
       return ldapTemplate.findAll(Usuario.class);
    }
 
    @Override
    public List<Usuario> findByLastName(String lastName) {
       return ldapTemplate.find(query().where("sn").is(lastName), Usuario.class);
    }
 }