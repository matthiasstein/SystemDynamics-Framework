# System Dynamics Java-Framework

### Introduction

This Framework allows you to create your own System Dynamics model of complex systems and to simulate its dynamic behavior.

### Usage

Best practise sample

#### 1. Create the model
```
Model model = new Model(0, 10, 0.1, new EulerCauchyIntegration());
```
#### 2. Create all system elements
```
// create a stock
Stock populationPrey = (Stock) model.createModelEntity(ModelEntityType.STOCK, POPULATION_PREY_KEY);
// set initial value
populationPrey.setInitialValue(100);

// create a variable
Variable expansionRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, BIRTH_RATE_PREY_KEY);
// set initial value
expansionRatePrey.setInitialValue(0.1);

// create another variable
Variable meetings = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, MEETINGS_KEY);
```
#### 3. Create the flows
```
// create some flows
Flow birthsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, BIRTHS_PREY_KEY);
Flow deathsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, DEATHS_PREY_KEY);
  
// add them to a stock
populationPrey.addInputFlows(birthsPrey);
populationPrey.addOutputFlows(deathsPrey);

// set calculation function
populationPrey.setChangeRateFunction(
	() -> birthsPrey.getCurrentValue() – deathsPrey.getCurrentValue()
);
```
#### 4. Define the converters
```
// define a converter
Converter deathsPreyConverter = model.createConverter(deathsPrey, meetings, lossRatePrey);

// set function
deathsPreyConverter.setFunction(
	() -> meetings.getCurrentValue() * lossRatePrey.getCurrentValue()
);
```
#### 5. Create the simulation
```
Simulation simulation = new Simulation(model);
```
#### 6. Add result handler
```
// add a CSVExport
simulation.addSimulationEventListener(
	new CSVExporter("output.csv", ";")
);

// Add a ChartViewer
simulation.addSimulationEventListener(
	new ChartViewer()
);
```
#### 7. Run the simulation
```
simulation.run();
```
