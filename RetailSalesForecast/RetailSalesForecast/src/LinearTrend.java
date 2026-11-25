/** Fits a straight line (least squares) through history and extends it. */
public class LinearTrend implements Forecaster {

    @Override
    public String name() {
        return "Linear Trend";
    }

    @Override
    public double[] forecast(double[] history, int horizon) {
        int n = history.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += history[i];
            sumXY += i * history[i];
            sumXX += (double) i * i;
        }
        double denom = n * sumXX - sumX * sumX;
        double slope = denom == 0 ? 0 : (n * sumXY - sumX * sumY) / denom;
        double intercept = (sumY - slope * sumX) / n;

        double[] result = new double[horizon];
        for (int h = 0; h < horizon; h++) {
            result[h] = Math.max(0, intercept + slope * (n + h));
        }
        return result;
    }
}
