import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Retail Sales Forecasting Tool.
 *
 * Usage: java Main [csvFile] [forecastMonths] [testMonths]
 * Defaults: data/sales.csv 3 3
 *
 * For each product the tool:
 *  1. holds back the last N months as a test set,
 *  2. runs every forecasting method on the earlier data,
 *  3. scores each method against the held-back months,
 *  4. picks the most accurate one (lowest MAPE) and forecasts the future with it.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Path file = Path.of(args.length > 0 ? args[0] : "data/sales.csv");
        int horizon = args.length > 1 ? Integer.parseInt(args[1]) : 3;
        int testSize = args.length > 2 ? Integer.parseInt(args[2]) : 3;

        List<Forecaster> methods = List.of(
                new MovingAverage(3),
                new ExponentialSmoothing(0.5),
                new LinearTrend());

        Map<String, double[]> sales = SalesDataLoader.load(file);
        if (sales.isEmpty()) {
            System.out.println("No sales data found in " + file);
            return;
        }

        System.out.println("=== Retail Sales Forecasting Tool ===");
        System.out.printf("Data: %s | Test months: %d | Forecast months: %d%n", file, testSize, horizon);

        for (Map.Entry<String, double[]> entry : sales.entrySet()) {
            String product = entry.getKey();
            double[] series = entry.getValue();

            System.out.println("\n--- " + product + " (" + series.length + " months of history) ---");
            if (series.length <= testSize + 2) {
                System.out.println("Not enough data, skipping.");
                continue;
            }

            double[] train = Arrays.copyOfRange(series, 0, series.length - testSize);
            double[] test = Arrays.copyOfRange(series, series.length - testSize, series.length);

            System.out.printf("%-26s %8s %8s %8s%n", "Method", "MAE", "RMSE", "MAPE %");
            Forecaster best = null;
            double bestMape = Double.MAX_VALUE;

            for (Forecaster f : methods) {
                double[] predicted = f.forecast(train, testSize);
                double mape = Metrics.mape(test, predicted);
                System.out.printf("%-26s %8.1f %8.1f %8.2f%n",
                        f.name(), Metrics.mae(test, predicted), Metrics.rmse(test, predicted), mape);
                if (mape < bestMape) {
                    bestMape = mape;
                    best = f;
                }
            }

            double[] future = best.forecast(series, horizon);
            System.out.println("Best method: " + best.name());
            System.out.print("Next " + horizon + " months forecast:");
            for (double v : future) {
                System.out.printf(" %.0f", v);
            }
            System.out.println();
        }
    }
}
