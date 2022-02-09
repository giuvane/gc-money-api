package br.edu.utfpr.gcmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;

public interface SrRepository extends JpaRepository<AgroApiKey, Long> {

}
