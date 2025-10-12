package br.edu.utfpr.gcmoney.api.service;

import br.edu.utfpr.gcmoney.api.model.AgroApiKeyResponse;
import br.edu.utfpr.gcmoney.api.model.Usuario;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKeyRequest;
import br.edu.utfpr.gcmoney.api.repository.SrRepository;
import br.edu.utfpr.gcmoney.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgroApiKeyService {

    @Autowired
    private SrRepository srRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AgroApiKey save(AgroApiKeyRequest request) {

        // 1. Verifica se o usuário já tem uma chave
        srRepository.findByUsuarioCodigo(request.getCodUsuario()).ifPresent(existing -> {
            throw new IllegalStateException("Usuário já possui uma API Key.");
        });

        // 2. Busca o usuário
        Usuario usuario = usuarioRepository.findById(request.getCodUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 3. Cria o objeto AgroApiKey
        AgroApiKey agroApiKey = new AgroApiKey();
        agroApiKey.setApikey(request.getApikey());
        agroApiKey.setAtivo(request.getAtivo());
        agroApiKey.setName(request.getName());
        agroApiKey.setUsuario(usuario);

        return srRepository.save(agroApiKey);
    }

    public AgroApiKey update(AgroApiKey request) {
        return srRepository.save(request);
    }


    public List<AgroApiKeyResponse> getAllAgroApiKeys() {
        List<AgroApiKey> list = srRepository.findAll();

        List<AgroApiKeyResponse> responseList = list.stream()
                .map(agroApiKey -> {
                    Optional<Usuario> user = usuarioRepository.findById(agroApiKey.getUsuario().getCodigo());
                    return user.map(usuario -> new AgroApiKeyResponse(
                            agroApiKey.getCodigo(),
                            agroApiKey.getName(),
                            agroApiKey.getApikey(),
                            agroApiKey.getAtivo(),
                            usuario.getNome()
                    )).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return responseList;
    }

    @Transactional
    public void deleteAgroApiKeyByCodigo(Long codigo) {
        srRepository.deleteByCodigo(codigo);
    }

    @Transactional
    public Optional<AgroApiKey> getAgroApiKeyByCodigo(Long codigo) {
        return this.srRepository.findById(codigo);
    }

    @Transactional
    public Optional<AgroApiKey> getAgroApiKeyByUserCode(Long codigo) {
        return this.srRepository.findByUsuarioCodigo(codigo);
    }
}
