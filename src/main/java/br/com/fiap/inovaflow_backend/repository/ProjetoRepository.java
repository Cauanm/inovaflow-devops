package br.com.fiap.inovaflow_backend.repository;

import br.com.fiap.inovaflow_backend.model.Projeto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjetoRepository extends MongoRepository<Projeto, String> {

    List<Projeto> findByEstrategiaId(String estrategiaId);

    List<Projeto> findByIdeiaId(String ideiaId);
}