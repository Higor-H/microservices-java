package br.edu.atitus.paradigma.cambio_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.atitus.paradigma.cambio_service.entities.CambioEntity;


public interface CambioRepository extends JpaRepository<CambioEntity, Integer> {

	Optional<CambioEntity> findByOrigemAndDestino(String origem, String destino);
	
}
