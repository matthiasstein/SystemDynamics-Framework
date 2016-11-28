package de.hsbo.fbg.systemdynamics.model;

import java.util.HashMap;

import de.hsbo.fbg.systemdynamics.exceptions.DuplicateModelEntityException;
import java.util.ArrayList;

public class Model {

    private HashMap<String, ModelEntity> modelEntities;
    private ArrayList<Converter> converterList;

    public Model() {
        modelEntities = new HashMap<String, ModelEntity>();
        converterList = new ArrayList<Converter>();
    }

    public ModelEntity createModelEntity(ModelEntityType entityType, String name) throws DuplicateModelEntityException {
        ModelEntity modelEntity;
        switch (entityType) {
            case STOCK:
                modelEntity = new Stock(name);
                break;
            case FLOW:
                modelEntity = new Flow(name);
                break;
            case VARIABLE:
                modelEntity = new Variable(name);
                break;
            default:
                return null;
        }
        this.addModelEntity(modelEntity);
        return modelEntity;
    }

    public void addModelEntity(ModelEntity modelEntity) throws DuplicateModelEntityException {
        if (!existsModelEntity(modelEntity)) {
            this.modelEntities.put(modelEntity.getName(), modelEntity);
        } else {
            throw new DuplicateModelEntityException("Model Entity name already exsists");
        }
    }

    private boolean existsModelEntity(ModelEntity modelEntity) {
        return this.modelEntities.containsKey(modelEntity.getName());
    }

    public void addConverter(Converter converter) {
        converterList.add(converter);
    }

    public void addConverters(Converter... converters) {
        for (Converter c : converters) {
            this.addConverter(c);
        }
    }

    public void simulate() {
        // simulate model
        converterList.forEach((converter) -> {
            converter.convert();
        });
    }

    public HashMap<String, ModelEntity> getModelEntities() {
        return modelEntities;
    }
}
