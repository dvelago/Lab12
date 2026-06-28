package com.ucv.lab12.repository;

import com.ucv.lab12.model.CompraVideojuego;
import com.ucv.lab12.model.Videojuego;

public interface ICompraVideojuegoRepository {

    CompraVideojuego registrarCompra(Videojuego videojuego, String comprador, String correo,
                                     int cantidad, String metodoPago);
}
