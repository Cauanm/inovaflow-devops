package br.com.fiap.inovaflow_backend.repository;

import br.com.fiap.inovaflow_backend.model.AnaliseIa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnaliseIaRepository extends MongoRepository<AnaliseIa, String> {

    List<AnaliseIa> findByIdeiaId(String ideiaId);
}