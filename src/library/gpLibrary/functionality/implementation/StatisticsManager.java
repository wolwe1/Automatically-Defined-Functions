package library.gpLibrary.functionality.implementation;

import library.gpLibrary.models.highOrder.implementation.PopulationStatistics;
import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsManager {

    public static PopulationStatistics<Double> calculateAverages(List<IMemberStatistics<Double>> populationStatistics) {

        Map<String,Double> measures = addMeasures(populationStatistics);

        //Now have the total value for each measure, average it and set it in the populationStatistics
        PopulationStatistics<Double> averagePopStats = new PopulationStatistics<>();

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            measure.setValue( measure.getValue()/populationStatistics.size());
            averagePopStats.setMeasure(measure.getKey(),measure.getValue());
        }

        return averagePopStats;

    }

    public static PopulationStatistics<String> calculateChange(PopulationStatistics<Double> start, PopulationStatistics<Double> stop) {

        PopulationStatistics<String> change = new PopulationStatistics<>();

        for (Map.Entry<String, Double> measure : start.getMeasures().entrySet()) {
            double startValue = measure.getValue();
            double stopValue = stop.getMeasure(measure.getKey()).getValue();

            double difference = stopValue - startValue;

            double percentageIncrease = (difference / startValue) * 100;
            percentageIncrease = Math.round( percentageIncrease * 100d) / 100d;

            change.setMeasure(measure.getKey(),"Start - "+startValue + " End - "+ stopValue + " Change - " + percentageIncrease + "%");
        }

        return change;
    }

    public static PopulationStatistics<Double> calculateStandardDeviation(List<IMemberStatistics<Double>> members) {

        PopulationStatistics<Double> mean = calculateAverages(members);
        PopulationStatistics<Double> standardDeviations = new PopulationStatistics<>();


        for (Map.Entry<String,Double> measure : mean.getMeasures().entrySet()) {
            var meanForMeasure = measure.getValue();
            var total = 0d;

            for (IMemberStatistics<Double> member : members) {
                double differance = member.getMeasure(measure.getKey()).getValue() - meanForMeasure;
                total += Math.pow(differance,2);
            }
            total = total/members.size();
            total = Math.sqrt(total);
            standardDeviations.setMeasure(measure.getKey(),total);
        }

        return standardDeviations;
    }

    private static Map<String,Double> addMeasures(List<IMemberStatistics<Double>> members){

        Map<String,Double> measures = new HashMap<>();
        var possibleMeasures = members.get(0).getMeasures();

        for (Map.Entry<String,Double> measure : possibleMeasures.entrySet()){
            //Set a measure of 0
            Map.Entry<String,Double> totalForMeasure = new AbstractMap.SimpleEntry<>(measure.getKey(), 0d);

            for (IMemberStatistics<Double> stat : members) {
                Map.Entry<String,Double> measureForThisMember = stat.getMeasure(totalForMeasure.getKey());

                totalForMeasure.setValue(totalForMeasure.getValue() + measureForThisMember.getValue());
            }
            measures.put(totalForMeasure.getKey(),totalForMeasure.getValue());
        }

        return measures;
    }
}
