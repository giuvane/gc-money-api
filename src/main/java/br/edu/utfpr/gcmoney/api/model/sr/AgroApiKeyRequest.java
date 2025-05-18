package br.edu.utfpr.gcmoney.api.model.sr;

import javax.validation.constraints.NotNull;


public class AgroApiKeyRequest {

    private Long codigo;

    @NotNull
    private String name;

    @NotNull
    private String apikey;

    @NotNull
    private Boolean ativo;

    private Long codUsuario;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getApikey() {
        return apikey;
    }

    public void setApikey(@NotNull String apikey) {
        this.apikey = apikey;
    }

    public @NotNull Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(@NotNull Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Long codUsuario) {
        this.codUsuario = codUsuario;
    }
}
