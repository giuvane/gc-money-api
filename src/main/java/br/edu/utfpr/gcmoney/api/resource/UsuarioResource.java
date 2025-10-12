package br.edu.utfpr.gcmoney.api.resource;

import br.edu.utfpr.gcmoney.api.model.MetaDataUsuarioResponse;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
    public List<MetaDataUsuarioResponse> listar() throws Exception {
        return usuarioService.getAllAgroApiKeys();
    }

    @GetMapping("/agroapikey")
    public AgroApiKey getAgroApiKeyDoUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        return usuarioService.getApiKeyByUser(login);
    }
}
