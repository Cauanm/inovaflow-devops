package br.com.fiap.inovaflow_backend.repository;

import br.com.fiap.inovaflow_backend.model.Estrategia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstrategiaRepository extends MongoRepository<Estrategia, String> {
}