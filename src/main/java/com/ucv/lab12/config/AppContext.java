package com.ucv.lab12.config;

import com.ucv.lab12.controller.DistribuidorController;
import com.ucv.lab12.controller.DistribuidorFormController;
import com.ucv.lab12.repository.DistribuidorRepository;
import com.ucv.lab12.repository.IDistribuidorRepository;
import com.ucv.lab12.service.DistribuidorService;
import com.ucv.lab12.service.IDistribuidorService;

/**
 * Contenedor de Inyección de Dependencias (DI).
 * Instancia y conecta todas las capas: config → repository → service → controller.
 */
public class AppContext {

    private static AppContext instance;

    private final DatabaseConfig dbConfig;
    private final IDistribuidorRepository distribuidorRepository;
    private final IDistribuidorService distribuidorService;

    private AppContext() {
        this.dbConfig               = new DatabaseConfig();
        this.distribuidorRepository = new DistribuidorRepository(dbConfig);
        this.distribuidorService    = new DistribuidorService(distribuidorRepository);
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    /**
     * Factory de controladores para FXMLLoader.setControllerFactory().
     * Inyecta el servicio en cada controlador.
     */
    public Object getController(Class<?> type) {
        if (type == DistribuidorController.class) {
            return new DistribuidorController(distribuidorService);
        }
        if (type == DistribuidorFormController.class) {
            return new DistribuidorFormController(distribuidorService);
        }
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

    public IDistribuidorService getDistribuidorService() {
        return distribuidorService;
    }

    public void destroy() {
        dbConfig.close();
    }
}
