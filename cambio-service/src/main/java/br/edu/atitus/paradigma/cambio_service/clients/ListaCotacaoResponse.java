package br.edu.atitus.paradigma.cambio_service.clients;

import java.util.List;

import br.edu.atitus.paradigma.cambio_service.clients.CotacaoResponse.Cotacao;

public class ListaCotacaoResponse {
    private List<Cotacao> value;
    public List<Cotacao> getValue() {
        return value;
    }
    public void setValue(List<Cotacao> value) {
        this.value = value;
    }
    
}