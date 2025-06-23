package br.edu.utfpr.gcmoney.api.resource;

import java.io.File;
import java.net.URL;

import br.edu.utfpr.gcmoney.api.dto.SendToAdbRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.gcmoney.api.service.SrService;

@RestController
@RequestMapping("/sr")
public class SrResource {
	
	@Autowired
	private SrService srService;
	
	@Autowired
	private ApplicationEventPublisher publisher; // Atributo criado para chamar o Evento criado
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public String sendToAdb(@RequestBody SendToAdbRequest request) throws Exception {
		
		URL url = new URL(request.getLink());
        File file = new File("temp");

        FileUtils.copyURLToFile(url, file);
		
		return this.srService.createJsonAndSendToAdb(file, request.getNomeLayer(), request.getAdbToken());
		
	}
}
