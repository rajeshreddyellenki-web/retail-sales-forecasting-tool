import java.util.Arrays;

/** Simple exponential smoothing: recent sales get more weight (alpha). */
public class ExponentialSmoothing implements Forecaster {
    private final double alpha;

    public ExponentialSmoothing(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public String name() {
        return "Exp. Smoothing (a=" + alpha + ")";
    }

    @Override
    public double[] forecast(double[] history, int horizon) {
        double level = history[0];
        for (int i = 1; i < history.length; i++) {
            level = alpha * history[i] + (1 - alpha) * level;
        }
        double[] result = new double[horizon];
        Arrays.fill(result, level);
        return result;
    }
}
