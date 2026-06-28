package com.ucv.lab12.service;

import com.ucv.lab12.model.CompraVideojuego;
import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.repository.ICompraVideojuegoRepository;
import com.ucv.lab12.repository.IVideojuegoRepository;

import java.util.List;

public class VideojuegoService implements IVideojuegoService {

    private final IVideojuegoRepository videojuegoRepository;
    private final ICompraVideojuegoRepository compraRepository;

    public VideojuegoService(IVideojuegoRepository videojuegoRepository,
                             ICompraVideojuegoRepository compraRepository) {
        this.videojuegoRepository = videojuegoRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    public List<Videojuego> listar() {
        return videojuegoRepository.findAll();
    }

    @Override
    public List<Videojuego> buscar(String nombre, String consola, String genero) {
        return videojuegoRepository.findByFilters(nombre, consola, genero);
    }

    @Override
    public void crear(Videojuego videojuego) {
        validar(videojuego);
        videojuegoRepository.save(videojuego);
    }

    @Override
    public void actualizar(Videojuego videojuego) {
        validar(videojuego);
        if (videojuego.getIdVideojuego() <= 0) {
            throw new IllegalArgumentException("El videojuego no tiene un ID valido.");
        }
        videojuegoRepository.update(videojuego);
    }

    @Override
    public void eliminar(int id) {
        videojuegoRepository.delete(id);
    }

    @Override
    public void validar(Videojuego videojuego) {
        if (videojuego.getNombre() == null || videojuego.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (videojuego.getNombre().trim().length() > 45) {
            throw new IllegalArgumentException("El nombre no puede superar 45 caracteres.");
        }
        if (videojuego.getConsola() == null || videojuego.getConsola().trim().isEmpty()) {
            throw new IllegalArgumentException("La consola es obligatoria.");
        }
        if (videojuego.getConsola().trim().length() > 45) {
            throw new IllegalArgumentException("La consola no puede superar 45 caracteres.");
        }
        if (videojuego.getGenero() == null || videojuego.getGenero().trim().isEmpty()) {
            throw new IllegalArgumentException("El genero es obligatorio.");
        }
        if (videojuego.getGenero().trim().length() > 45) {
            throw new IllegalArgumentException("El genero no puede superar 45 caracteres.");
        }
        if (videojuego.getClasificacion() == null || videojuego.getClasificacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La clasificacion es obligatoria.");
        }
        if (videojuego.getClasificacion().trim().length() > 45) {
            throw new IllegalArgumentException("La clasificacion no puede superar 45 caracteres.");
        }
        if (videojuego.getDescripcion() == null || videojuego.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (videojuego.getDescripcion().trim().length() > 255) {
            throw new IllegalArgumentException("La descripcion no puede superar 255 caracteres.");
        }
        if (videojuego.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (videojuego.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (videojuego.getIdDistribuidor() <= 0) {
            throw new IllegalArgumentException("El ID del distribuidor debe ser mayor que cero.");
        }
        if (videojuego.getIdDesarrollador() <= 0) {
            throw new IllegalArgumentException("El ID del desarrollador debe ser mayor que cero.");
        }
    }

    @Override
    public CompraVideojuego comprar(int idVideojuego, String comprador, String correo,
                                    int cantidad, String metodoPago) {
        if (idVideojuego <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un videojuego valido.");
        }
        if (comprador == null || comprador.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del comprador es obligatorio.");
        }
        if (comprador.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre del comprador no puede superar 100 caracteres.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El metodo de pago es obligatorio.");
        }
        if (metodoPago.trim().length() > 30) {
            throw new IllegalArgumentException("El metodo de pago no puede superar 30 caracteres.");
        }
        if (correo != null && !correo.trim().isEmpty()
                && !correo.trim().matches("^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        }
        if (correo != null && correo.trim().length() > 120) {
            throw new IllegalArgumentException("El correo no puede superar 120 caracteres.");
        }

        Videojuego videojuego = videojuegoRepository.findById(idVideojuego)
                .orElseThrow(() -> new IllegalArgumentException("El videojuego no existe."));
        return compraRepository.registrarCompra(videojuego, comprador.trim(),
                correo == null ? "" : correo.trim(), cantidad, metodoPago.trim());
    }
}
