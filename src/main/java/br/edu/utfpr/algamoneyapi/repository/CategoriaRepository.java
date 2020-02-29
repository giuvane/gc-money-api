package br.edu.utfpr.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.algamoneyapi.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
}
