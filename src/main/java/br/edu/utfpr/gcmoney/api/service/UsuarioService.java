package br.edu.utfpr.gcmoney.api.service;

import br.edu.utfpr.gcmoney.api.model.MetaDataUsuarioResponse;
import br.edu.utfpr.gcmoney.api.model.Usuario;
import br.edu.utfpr.gcmoney.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<MetaDataUsuarioResponse> getAllAgroApiKeys() {
        List<Usuario> users = usuarioRepository.findAll();

        return users.stream().map(
                usuario -> new MetaDataUsuarioResponse(
                usuario.getCodigo(),
                usuario.getNome()
        )).collect(Collectors.toList());
    }
}
