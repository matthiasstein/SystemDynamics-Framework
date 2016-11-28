package de.hsbo.fbg.systemdynamics.model;

import de.hsbo.fbg.systemdynamics.functions.IFunction;

public class Converter {

    private IFunction function;
    private ModelEntity targetEntity;

    protected Converter(ModelEntity entity, IFunction function) {
        this.targetEntity = entity;
        this.function = function;
    }

    public void convert() {
        this.targetEntity.setValue(function.calculateEntityValue());
    }
}
