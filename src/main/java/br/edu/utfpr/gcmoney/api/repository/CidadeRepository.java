package br.edu.utfpr.gcmoney.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.gcmoney.api.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	
	List<Cidade> findByEstadoCodigo(Long estadoCodigo);

}
