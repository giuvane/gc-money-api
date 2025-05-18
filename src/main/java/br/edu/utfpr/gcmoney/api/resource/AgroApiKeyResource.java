package br.edu.utfpr.gcmoney.api.resource;

import br.edu.utfpr.gcmoney.api.model.AgroApiKeyResponse;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKeyRequest;
import br.edu.utfpr.gcmoney.api.service.AgroApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agro-api-key")
public class AgroApiKeyResource {

    @Autowired
    private AgroApiKeyService agroApiKeyService;

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
    public List<AgroApiKeyResponse> listar() throws Exception {
        return this.agroApiKeyService.getAllAgroApiKeys();
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<AgroApiKey> criarAgroApiKey(@Valid @RequestBody AgroApiKeyRequest agroapikey, HttpServletResponse response) {
        AgroApiKey agroApiKeySalvo = this.agroApiKeyService.save(agroapikey);
        return ResponseEntity.status(HttpStatus.CREATED).body(agroApiKeySalvo);
    }

    @PutMapping("/{codigo}")
    //@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<AgroApiKey> updateAgroApiKey(@PathVariable Long codigo, @Valid @RequestBody AgroApiKey agroapikey, HttpServletResponse response) {
        AgroApiKey agroApiKeySalvo = this.agroApiKeyService.update(agroapikey);
        return ResponseEntity.status(HttpStatus.CREATED).body(agroApiKeySalvo);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long codigo) {
        this.agroApiKeyService.deleteAgroApiKeyByCodigo(codigo);
    }

    @GetMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<?> getByCode(@PathVariable Long codigo) {
        Optional<AgroApiKey> agroApiKey = this.agroApiKeyService.getAgroApiKeyByCodigo(codigo);
        return agroApiKey.isPresent() ? ResponseEntity.ok(agroApiKey) : ResponseEntity.notFound().build();
    }
}
