package library.gpLibrary.models.classification;

import java.util.HashMap;

public class Problem<T> {

    private T _answer;
    private final HashMap<String,T> items;
    private String answerField;

    public Problem(String answerField) {
        items = new HashMap<>();
        this.answerField = answerField;
    }

    public void addItem(String descriptor, T value){
        items.put(descriptor,value);
    }

    public T getAnswer() {
        return items.get(answerField);
    }

    public T getValue(String matchField) {
        return items.get(matchField);
    }

    public void setValue(String field, T value) {
        items.put(field,value);
    }
}
