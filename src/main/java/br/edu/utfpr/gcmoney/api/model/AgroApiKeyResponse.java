package br.edu.utfpr.gcmoney.api.model;


public class AgroApiKeyResponse {

    private Long codigo;

    private String name;

    private String apikey;

    private Boolean ativo;

    private String usuario;

    public AgroApiKeyResponse(Long codigo, String name, String apikey, Boolean ativo, String usuario) {
        this.codigo = codigo;
        this.name = name;
        this.apikey = apikey;
        this.ativo = ativo;
        this.usuario = usuario;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
