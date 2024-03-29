package br.edu.utfpr.gcmoney.api.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import br.edu.utfpr.gcmoney.api.AlgamoneyApiApplication;
import br.edu.utfpr.gcmoney.api.model.Lancamento;
import br.edu.utfpr.gcmoney.api.storage.S3;

public class LancamentoAnexoListener {

	@PostLoad
	public void postLoad(Lancamento lancamento) {
		if (StringUtils.hasText(lancamento.getAnexo())) {
			S3 s3 = AlgamoneyApiApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
		}
	}
}
