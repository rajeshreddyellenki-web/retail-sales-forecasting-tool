import java.util.Arrays;

/** Forecasts the average of the last N periods (flat forecast). */
public class MovingAverage implements Forecaster {
    private final int window;

    public MovingAverage(int window) {
        this.window = window;
    }

    @Override
    public String name() {
        return "Moving Average (" + window + ")";
    }

    @Override
    public double[] forecast(double[] history, int horizon) {
        int n = Math.min(window, history.length);
        double sum = 0;
        for (int i = history.length - n; i < history.length; i++) {
            sum += history[i];
        }
        double[] result = new double[horizon];
        Arrays.fill(result, sum / n);
        return result;
    }
}
