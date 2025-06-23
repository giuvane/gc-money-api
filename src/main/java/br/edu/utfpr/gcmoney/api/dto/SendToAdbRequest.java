package br.edu.utfpr.gcmoney.api.dto;

public class SendToAdbRequest {
    private String link;
    private String nomeLayer;
    private String adbToken;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNomeLayer() {
        return nomeLayer;
    }

    public void setNomeLayer(String nomeLayer) {
        this.nomeLayer = nomeLayer;
    }

    public String getAdbToken() {
        return adbToken;
    }

    public void setAdbToken(String adbToken) {
        this.adbToken = adbToken;
    }
}