package library.gpLibrary.models.classification;

import java.util.ArrayList;
import java.util.List;

public class ProblemSet<T> {

    List<Problem<T>> problems;

    public ProblemSet(List<String> data,List<String> dataNames,String answerField){

        problems = new ArrayList<>();

        for (String datum : data) {
            Problem<T> problemEntry = new Problem<T>(answerField);
            String[] dataPoints = datum.split(",");

            for (int i = 0, dataNamesSize = dataNames.size(); i < dataNamesSize; i++) {
                String dataName = dataNames.get(i);
                String dataValue = dataPoints[i];

                problemEntry.addItem(dataName,(T)dataValue);
            }
            problems.add(problemEntry);
        }

        replaceMissingValues();
    }

    private void replaceMissingValues() {
        int total = 0;
        int presentCount = 0;

        for (Problem<T> problem : problems) {

            if(!problem.getValue("COMFORT").equals("?") ){
                total += Integer.parseInt((String) problem.getValue("COMFORT"));
                presentCount++;
            }
        }

        int average = total/presentCount;

        for (Problem<T> problem : problems) {
            if(problem.getValue("COMFORT").equals("?"))
                problem.setValue("COMFORT",(T)Integer.toString(average));
        }
    }

    public List<Problem<T>> getProblems() {
        return problems;
    }
}
