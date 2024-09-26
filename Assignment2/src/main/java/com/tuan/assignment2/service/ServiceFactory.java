package com.tuan.assignment2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceFactory {
    private final Map <String, IService<?>> serviceRegistry;

    @Autowired
    public ServiceFactory(List<IService<?>> services) {
        this.serviceRegistry = new HashMap<>();
        services.forEach(service -> {
            if (service instanceof UserService) {
                serviceRegistry.put("User", service);
            } else if (service instanceof CatService) {
                serviceRegistry.put("Cat", service);
            }
        });
    }

    public IService<?> getService(String entityType) {
        return  serviceRegistry.get(entityType);
    }
}
