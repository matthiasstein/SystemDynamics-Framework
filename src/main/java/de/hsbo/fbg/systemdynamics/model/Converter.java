package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.IFunction;

public class Converter {

    private IFunction function;
    private ModelEntity targetEntity;
    ArrayList<ModelEntity> inputs;

    protected Converter(ModelEntity entity, IFunction function, ModelEntity... inputs) {
        this.targetEntity = entity;
        this.function = function;
        this.inputs = new ArrayList<ModelEntity>();
        for (ModelEntity modelEntity : inputs) {
            this.inputs.add(modelEntity);
        }
    }

    public void convert() {
        for (ModelEntity input : this.inputs) {
            if (!input.isCurrentValueCalculated() && input.getConverter() != null) {
                input.getConverter().convert();
            }
        }
        this.targetEntity.setCurrentValue(function.calculateEntityValue());
    }

    public void addInputs(ModelEntity... inputs) throws ModelException {
        for (ModelEntity f : inputs) {
            this.addInput(f);
        }
    }

    private void addInput(ModelEntity input) throws ModelException {
        if (inputAlreadyAdded(input)) {
            throw new ModelException(ModelException.DUPLICATE_VARIABLE_EXCEPTION);
        } else {
            this.inputs.add(input);
        }
    }

    private boolean inputAlreadyAdded(ModelEntity variable) {
        return inputs.contains(variable);
    }

    public ModelEntity getTargetEntity() {
        return targetEntity;
    }

}
