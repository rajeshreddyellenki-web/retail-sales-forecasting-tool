/** Forecast accuracy measures. Lower is better for all three. */
public final class Metrics {
    private Metrics() {}

    /** Mean Absolute Error: average miss in units. */
    public static double mae(double[] actual, double[] predicted) {
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.abs(actual[i] - predicted[i]);
        }
        return sum / actual.length;
    }

    /** Root Mean Squared Error: penalises big misses more. */
    public static double rmse(double[] actual, double[] predicted) {
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            double d = actual[i] - predicted[i];
            sum += d * d;
        }
        return Math.sqrt(sum / actual.length);
    }

    /** Mean Absolute Percentage Error: average miss in percent. */
    public static double mape(double[] actual, double[] predicted) {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] != 0) {
                sum += Math.abs((actual[i] - predicted[i]) / actual[i]);
                count++;
            }
        }
        return count == 0 ? 0 : 100.0 * sum / count;
    }
}
