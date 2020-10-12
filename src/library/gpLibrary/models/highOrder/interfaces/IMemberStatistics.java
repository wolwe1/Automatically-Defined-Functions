package library.gpLibrary.models.highOrder.interfaces;


import java.util.Map;

public interface IMemberStatistics<T> {

    /**
     * Returns whatever statistic measure is the fitness
     * @return The fitness value
     */
    Double getFitness();

    Map<String, T> getMeasures();

    Map.Entry<String, T> getMeasure(String key);

    void print();

    void setMeasure(String key, T value);
}
