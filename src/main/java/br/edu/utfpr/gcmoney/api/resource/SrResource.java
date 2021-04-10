package br.edu.utfpr.gcmoney.api.resource;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.gcmoney.api.model.Pessoa;
import br.edu.utfpr.gcmoney.api.service.SrService;

@RestController
@RequestMapping("/sr")
public class SrResource {
	
	@Autowired
	private SrService srService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public String listar(@RequestParam String link, @RequestParam String nomeLayer) throws Exception {
		
		URL url = new URL(link);
        File file = new File("temp");

        FileUtils.copyURLToFile(url, file);
		
		return this.srService.getJsonFromFile(file, nomeLayer);
		
	}

}
