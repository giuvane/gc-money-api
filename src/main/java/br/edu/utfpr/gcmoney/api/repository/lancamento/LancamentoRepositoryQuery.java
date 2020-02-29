package br.edu.utfpr.gcmoney.api.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.utfpr.gcmoney.api.dto.LancamentoEstatisticaCategoria;
import br.edu.utfpr.gcmoney.api.dto.LancamentoEstatisticaDia;
import br.edu.utfpr.gcmoney.api.dto.LancamentoEstatisticaPessoa;
import br.edu.utfpr.gcmoney.api.model.Lancamento;
import br.edu.utfpr.gcmoney.api.repository.filter.LancamentoFilter;
import br.edu.utfpr.gcmoney.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
}
