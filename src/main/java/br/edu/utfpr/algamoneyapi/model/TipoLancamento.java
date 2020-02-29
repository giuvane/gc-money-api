package br.edu.utfpr.algamoneyapi.model;

public enum TipoLancamento {
	RECEITA("Receita"),
	DESPESA("Despesa");
	
	private final String descricao;
	
	private TipoLancamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
