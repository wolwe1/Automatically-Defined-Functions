package library.gpLibrary.models.highOrder.implementation;

import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;

import java.util.HashMap;
import java.util.Map;

public class PopulationStatistics<T> implements IMemberStatistics<T> {

    Map<String,T> measures;

    public PopulationStatistics(){
        measures = new HashMap<>();
    }

    @Override
    public Double getFitness() {
        throw new RuntimeException("Population statistics has no fitness");
    }

    @Override
    public Map<String, T> getMeasures() {
        return measures;
    }

    @Override
    public Map.Entry<String, T> getMeasure(String key) {

        for (Map.Entry<String, T> measure : measures.entrySet()) {
            if(measure.getKey().equals(key))
                return measure;
        }
        throw new RuntimeException("Measure requested that does not exist");
    }

    @Override
    public void print() {

        for (Map.Entry<String, T> measure : measures.entrySet()) {
            System.out.printf("%-20.20s  %-30.50s%n",measure.getKey() + ": ",measure.getValue());
        }
    }

    @Override
    public void setMeasure(String key, T value) {
        measures.put(key,value);
    }

}
