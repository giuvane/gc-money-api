package br.edu.utfpr.gcmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.gcmoney.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
}
