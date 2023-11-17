import java.io.File;

public class SignalPlotter {


    public static final double FIRST_LIMIT = -10.0;
    public static final double SECOND_LIMIT = 10.0;
    public static final int NUMBER_OF_POINTS = 1000;
    public static final int SAMPLINGRATE = 250;


    public static double[] createSamplingPoints(double firstLimit, double secondLimit, int numberOfPoints){

        if(firstLimit == secondLimit){
            numberOfPoints = 1;
        }

        double[] points = new double[numberOfPoints];


        if(numberOfPoints == 1){
            points[0] = secondLimit;
        } else {

            //Berechnung zum Abstand der Punkte,
            //da wir aber schon den ersten Punkt gegeben haben muessen wir bei numberOfPoints eines abziehen
            double distanceOfPoints = (secondLimit - firstLimit) / (numberOfPoints - 1);

            for(int i = 0; i < numberOfPoints; i++){
                points[i] = firstLimit + (i * distanceOfPoints);
            }

        }

        return points;
    }

    public static double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }

    public static double[] applySigmoidToArray(double[] xs){

        double[] siggayray = new double[xs.length];

        for(int i = 0; i < xs.length; i++){
            siggayray[i] = sigmoid(xs[i]);
        }

        return siggayray;
    }

    public static void plotSigmoid(){

        double[] samplingPoints = createSamplingPoints(FIRST_LIMIT, SECOND_LIMIT, NUMBER_OF_POINTS);

        double[] functionAtSamplePoint = applySigmoidToArray(samplingPoints);

        PlotHelper.plot2D(samplingPoints, functionAtSamplePoint);

    }

    public static void plotEcg(){

        double[] ecgSignal = PlotHelper.readEcg("ecg.txt");

        double firstLimit = 0;
        double secondLimit = (double) ecgSignal.length / SAMPLINGRATE;

        double[] ecgTime = createSamplingPoints(firstLimit, secondLimit, ecgSignal.length);

        double[] functionAtSamplePoint = applySigmoidToArray(ecgSignal);

        //PlotHelper.plotEcg(ecgTime, functionAtSamplePoint);


        //R-Zacken Aufgabe
        int[] idxRPeaks = PlotHelper.readPeaks("rpeaks.txt");

        double[] rPeaks = new double[idxRPeaks.length];
        double[] timeRPeaks = new double[idxRPeaks.length];

        for(int i = 0; i < idxRPeaks.length; i++){
            rPeaks[i] = functionAtSamplePoint[idxRPeaks[i]];
        }
        for(int i = 0; i < idxRPeaks.length; i++){
            timeRPeaks[i] = ecgTime[idxRPeaks[i]];
        }

        computeHeartRate(timeRPeaks);

        PlotHelper.plotEcg(ecgTime, functionAtSamplePoint, timeRPeaks, rPeaks);


    }

    public static void computeHeartRate(double[] timeRPeaks){
        System.out.println("Heart Rate:");
        for(int i = 0; i < timeRPeaks.length - 1; i++){
            double dif = timeRPeaks[i + 1] - timeRPeaks[i];
            double bpm = 60 / dif;
            System.out.println(String.format("%.2f", bpm) + " bpm");
        }
    }

    public static void main(String[] args) {

        plotSigmoid();

        plotEcg();

    }
}
