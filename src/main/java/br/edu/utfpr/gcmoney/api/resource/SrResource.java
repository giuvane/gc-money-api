package br.edu.utfpr.gcmoney.api.resource;

import java.io.File;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.gcmoney.api.event.RecursoCriadoEvent;
import br.edu.utfpr.gcmoney.api.model.Pessoa;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.service.SrService;

@RestController
@RequestMapping("/sr")
public class SrResource {
	
	@Autowired
	private SrService srService;
	
	@Autowired
	private ApplicationEventPublisher publisher; // Atributo criado para chamar o Evento criado
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public String listar(@RequestParam String link, @RequestParam String nomeLayer) throws Exception {
		
		URL url = new URL(link);
        File file = new File("temp");

        FileUtils.copyURLToFile(url, file);
		
		return this.srService.getJsonFromFile(file, nomeLayer);
		
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<AgroApiKey> criarAgroApiKey(@Valid @RequestBody AgroApiKey agroapikey, HttpServletResponse response) {
		//Pessoa pessoaSalva = this.pessoaRepository.save(pessoa); // Comentado ao ser adicionado Contato em Pessoa
		AgroApiKey agroApiKeySalvo = this.srService.salvar(agroapikey);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, agroApiKeySalvo.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(agroApiKeySalvo);
	}

}
