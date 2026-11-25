/** Common interface for every forecasting method. */
public interface Forecaster {

    /** Name shown in the report. */
    String name();

    /**
     * Predict the next {@code horizon} values using only the given history.
     *
     * @param history past sales, oldest first
     * @param horizon how many future periods to predict
     */
    double[] forecast(double[] history, int horizon);
}
