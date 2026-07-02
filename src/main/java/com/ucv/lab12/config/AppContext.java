package com.ucv.lab12.config;

import com.ucv.lab12.repository.DeudaRepository;
import com.ucv.lab12.repository.IDeudaRepository;
import com.ucv.lab12.service.DeudaService;
import com.ucv.lab12.service.IDeudaService;

public class AppContext {

    private final IDeudaRepository deudaRepository;
    private final IDeudaService deudaService;

    public AppContext() {
        // Inicializamos únicamente los componentes del sistema de deudas de la UGEL
        this.deudaRepository = new DeudaRepository();
        this.deudaService = new DeudaService();
    }

    public IDeudaRepository getDeudaRepository() {
        return deudaRepository;
    }

    public IDeudaService getDeudaService() {
        return deudaService;
    }
}