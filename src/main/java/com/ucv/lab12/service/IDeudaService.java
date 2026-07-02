package com.ucv.lab12.service;

import com.ucv.lab12.model.Deuda;
import java.util.List;

public interface IDeudaService {
    boolean registrarDeuda(Deuda deuda);
    boolean modificarDeuda(Deuda deuda);
    List<Deuda> obtenerTodasLasDeudas();
    List<Deuda> buscarDeudasPorDni(String dni);
}