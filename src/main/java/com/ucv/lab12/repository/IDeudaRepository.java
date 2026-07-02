package com.ucv.lab12.repository;

import com.ucv.lab12.model.Deuda;
import java.util.List;

public interface IDeudaRepository {
    boolean registrar(Deuda deuda);
    boolean modificar(Deuda deuda);
    List<Deuda> listarTodas();
    List<Deuda> buscarPorDniDocente(String dni);
}