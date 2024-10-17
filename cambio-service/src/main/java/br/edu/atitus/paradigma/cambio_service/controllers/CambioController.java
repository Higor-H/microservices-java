package br.edu.atitus.paradigma.cambio_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.edu.atitus.paradigma.cambio_service.clients.CotacaoClient;
import br.edu.atitus.paradigma.cambio_service.clients.CotacaoResponse;
import br.edu.atitus.paradigma.cambio_service.entities.CambioEntity;
import br.edu.atitus.paradigma.cambio_service.repositories.CambioRepository;

@RestController
@RequestMapping("cambio-service")
public class CambioController {

	private final CambioRepository repository;
	private final CotacaoClient cotacaoBCB;

	public CambioController(CambioRepository repository, CotacaoClient cotacaoBCB) {
		super();
		this.repository = repository;
		this.cotacaoBCB = cotacaoBCB;
	}
	
	@Value("${server.port}")
	private int porta;
	
	
	@GetMapping("/{valor}/{origem}/{destino}")
	public ResponseEntity getCambio(@PathVariable double valor, @PathVariable String origem,
			@PathVariable String destino) throws Exception {

		CambioEntity cambio = repository.findByOrigemAndDestino(origem, destino)
				.orElseThrow(() -> new Exception("Câmbio não encontrado para esta origem e destino"));

		
		CotacaoResponse cotacaoOrigem = cotacaoBCB.getCotacaoMoedaDia(origem, "10-10-2024");
		if (destino.equals("BRL")) { 
			
			double fator = cotacaoOrigem.getValue().get(0).getCotacaoVenda();
			cambio.setFator(fator);
		} else {
			
			CotacaoResponse cotacaoDestino = cotacaoBCB.getCotacaoMoedaDia(destino, "10-10-2024");
			double fator = cotacaoOrigem.getValue().get(0).getCotacaoVenda()
								/ cotacaoDestino.getValue().get(0).getCotacaoVenda();
			cambio.setFator(fator);
		}

		cambio.setValorConvertido(valor * cambio.getFator());
		cambio.setAmbiente("Cambio-Service run in port: " + porta);
		
		return ResponseEntity.ok(cambio);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handler(Exception e){
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.badRequest().body(message);
	}
}
