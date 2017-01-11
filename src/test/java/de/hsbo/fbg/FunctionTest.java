package de.hsbo.fbg;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
import de.hsbo.fbg.systemdynamics.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Seba on 11.01.2017.
 */
public class FunctionTest {

    private Model model;
    Stock population;
    Variable birthRate;
    Flow births;
    Flow deaths;

    @Before
    public void prepareValues() {
        model = new Model(1, 12, 1, new EulerCauchyIntegration());
        try {
            population = (Stock) model.createModelEntity(ModelEntityType.STOCK, "population");
            birthRate = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "birth_rate");
            births = (Flow) model.createModelEntity(ModelEntityType.FLOW, "births");
            deaths = (Flow) model.createModelEntity(ModelEntityType.FLOW, "deaths");
            population.addInputFlows(births);
            population.addOutputFlows(deaths);
            population.setInitialValue(5000);
            population.setChangeRateFunction(
                    () -> births.getCurrentValue()
                            -deaths.getCurrentValue());

            deaths.setInitialValue(100);
            birthRate.setInitialValue(0.1);


        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void algebraicFunctionTest() {
        Converter birthsConverter = model.createConverter(births, birthRate, population);
        birthsConverter.setFunction(new IFunction() {
            @Override
            public double calculateEntityValue() {
                double result=birthRate.getCurrentValue()
                        * population.getCurrentValue();
                return result;
            }
        });
        Simulation simulation = new Simulation(model);
        simulation.run();
    }

    @Test
    public void logicFunctionTest() {
        Converter birthsConverter = model.createConverter(births, birthRate, population);
        birthsConverter.setFunction(() -> {
            if (population.getCurrentValue()>=10000)
                return 0;
            else{
                double result=birthRate.getCurrentValue()
                        * population.getCurrentValue();
                return result;
            }
        });
        Simulation simulation = new Simulation(model);
        simulation.run();
    }

    @Test
    public void lookUpTableTest() {
        Converter birthsConverter = model.createConverter(births, birthRate, population);
        birthsConverter.setFunction(new IFunction() {
            @Override
            public double calculateEntityValue() {
                double result=birthRate.getCurrentValue()
                        * population.getCurrentValue();
                return result;
            }
        });
        Converter birthsRateConverter = model.createConverter(birthRate);
        TreeMap<Integer, Double> lookupTable=new TreeMap<Integer, Double>();
        lookupTable.put(1,0.1);lookupTable.put(2,0.15);
        lookupTable.put(3,0.2);lookupTable.put(4,0.25);
        lookupTable.put(5,0.3);lookupTable.put(6,0.35);
        lookupTable.put(7,0.4);lookupTable.put(8,0.45);
        lookupTable.put(9,0.4);lookupTable.put(10,0.35);
        lookupTable.put(11,0.3);lookupTable.put(12,0.25);

        birthsRateConverter.setFunction(new IFunction() {
            @Override
            public double calculateEntityValue() {
                double currentTime=model.getCurrentTime();
                double floorKey=lookupTable.floorKey((int)currentTime);
                double floor=lookupTable.floorEntry((int)currentTime).getValue();
                double ceilingKey=lookupTable.ceilingKey((int)currentTime);
                double ceiling=lookupTable.ceilingEntry((int)currentTime).getValue();
                if (floorKey==ceilingKey)
                    return floor;
                else{
                    double result=floor+(ceiling-floor)
                            /(ceilingKey-floorKey)*(currentTime-ceilingKey);
                    return result;
                }
            }
        });
        Simulation simulation = new Simulation(model);
        simulation.run();


    }
}
