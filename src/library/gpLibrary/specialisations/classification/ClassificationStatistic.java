package library.gpLibrary.specialisations.classification;

import library.gpLibrary.helpers.Printer;
import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;

import java.util.HashMap;
import java.util.Map;

public class ClassificationStatistic implements IMemberStatistics<Double> {

    Map<String,Double> measures;
    private String fitnessMeasure;

    public ClassificationStatistic(){
        measures = new HashMap<>();
        measures.put("Accuracy",0d);
        measures.put("Hits",0d);

        fitnessMeasure = "MAE";
    }

    @Override
    public Double getFitness() {
        return measures.get(fitnessMeasure);
    }

    @Override
    public void setFitness(String measure) {
        this.fitnessMeasure = measure;
    }

    @Override
    public Map<String, Double> getMeasures() {
        return measures;
    }

    @Override
    public Map.Entry<String, Double> getMeasure(String key) {

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            if(measure.getKey().equals(key))
                return measure;
        }
        throw new RuntimeException("Measure requested that does not exist");
    }

    @Override
    public void print() {

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            Printer.print(measure.getKey() + " : " + measure.getValue());
        }

    }

    @Override
    public void setMeasure(String key, Double value) {
        measures.put(key,value);
    }
}
