package br.edu.utfpr.gcmoney.api.resource;

import br.edu.utfpr.gcmoney.api.model.MetaDataUsuarioResponse;
import br.edu.utfpr.gcmoney.api.model.Usuario;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKeyRequest;
import br.edu.utfpr.gcmoney.api.repository.UsuarioRepository;
import br.edu.utfpr.gcmoney.api.service.AgroApiKeyService;
import br.edu.utfpr.gcmoney.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
}
