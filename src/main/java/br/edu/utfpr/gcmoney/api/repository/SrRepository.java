package br.edu.utfpr.gcmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;

import java.util.Optional;

public interface SrRepository extends JpaRepository<AgroApiKey, Long> {
    void deleteByCodigo(Long codigo);
    Optional<AgroApiKey> findByUsuarioCodigo(Long codigoUsuario);
}
