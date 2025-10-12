package br.edu.utfpr.gcmoney.api.service;

import br.edu.utfpr.gcmoney.api.model.MetaDataUsuarioResponse;
import br.edu.utfpr.gcmoney.api.model.Usuario;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.repository.UsuarioRepository;
import com.amazonaws.services.glue.model.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AgroApiKeyService agroApiKeyService;

    public List<MetaDataUsuarioResponse> getAllAgroApiKeys() {
        List<Usuario> users = usuarioRepository.findAll();

        return users.stream().map(
                usuario -> new MetaDataUsuarioResponse(
                        usuario.getCodigo(),
                        usuario.getNome()
                )).collect(Collectors.toList());
    }

    public AgroApiKey getApiKeyByUser(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o usuario: " + email));

        return agroApiKeyService.getAgroApiKeyByUserCode(usuario.getCodigo())
                .orElseThrow(() -> new EntityNotFoundException("AgroApiKey não encontrada para o usuário: " + email));
    }
}
