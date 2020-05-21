package com.pruebas.apuestas.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.apuestas.entity.Ruleta;
@Repository
public interface RuletaRepository extends CrudRepository<Ruleta,Long> {

}
