package com.ucv.lab12.repository;

import com.ucv.lab12.model.Videojuego;

import java.util.List;
import java.util.Optional;

public interface IVideojuegoRepository {

    List<Videojuego> findAll();

    List<Videojuego> findByFilters(String nombre, String consola, String genero);

    Optional<Videojuego> findById(int id);

    void save(Videojuego videojuego);

    void update(Videojuego videojuego);

    void delete(int id);
}
