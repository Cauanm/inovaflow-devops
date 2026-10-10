package br.com.fiap.inovaflow_backend.repository;

import br.com.fiap.inovaflow_backend.model.Ideia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IdeiaRepository extends MongoRepository<Ideia, String> {

    List<Ideia> findByOperadorId(String operadorId);
}