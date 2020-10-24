package library.gpLibrary.helpers;

public class MathHelp {

    public static double easyRound(int decimalPoints,double value){
        double scale = Math.pow(10, 4);
        return Math.round(value * scale) / scale;
    }
}
