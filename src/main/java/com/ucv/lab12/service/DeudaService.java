package com.ucv.lab12.service;

import com.ucv.lab12.model.Deuda;
import com.ucv.lab12.repository.IDeudaRepository;
import com.ucv.lab12.repository.DeudaRepository;

import java.util.List;

public class DeudaService implements IDeudaService {

    private final IDeudaRepository deudaRepository;

    // Constructor que inicializa el repositorio
    public DeudaService() {
        this.deudaRepository = new DeudaRepository();
    }

    @Override
    public boolean registrarDeuda(Deuda deuda) {
        // Validación de negocio: El monto no puede ser menor o igual a cero
        if (deuda.getMonto() <= 0) {
            System.out.println("Error: El monto de la deuda debe ser mayor a cero.");
            return false;
        }
        return deudaRepository.registrar(deuda);
    }

    @Override
    public boolean modificarDeuda(Deuda deuda) {
        if (deuda.getMonto() <= 0) {
            System.out.println("Error: El monto a modificar debe ser mayor a cero.");
            return false;
        }
        return deudaRepository.modificar(deuda);
    }

    @Override
    public List<Deuda> obtenerTodasLasDeudas() {
        return deudaRepository.listarTodas();
    }

    @Override
    public List<Deuda> buscarDeudasPorDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return deudaRepository.listarTodas();
        }
        return deudaRepository.buscarPorDniDocente(dni.trim());
    }
}