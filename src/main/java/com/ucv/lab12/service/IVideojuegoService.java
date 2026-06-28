package com.ucv.lab12.service;

import com.ucv.lab12.model.CompraVideojuego;
import com.ucv.lab12.model.Videojuego;

import java.util.List;

public interface IVideojuegoService {

    List<Videojuego> listar();

    List<Videojuego> buscar(String nombre, String consola, String genero);

    void crear(Videojuego videojuego);

    void actualizar(Videojuego videojuego);

    void eliminar(int id);

    void validar(Videojuego videojuego);

    CompraVideojuego comprar(int idVideojuego, String comprador, String correo, int cantidad, String metodoPago);
}
