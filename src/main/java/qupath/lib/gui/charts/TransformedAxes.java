package qupath.lib.gui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * Transformed axes for JavaFX charts.
 */
public class TransformedAxes {

    private static double log(double x, double base) {
        return Math.log(x) / Math.log(base);
    }

    /**
     * Create an axis to display values on a log10(x+1) scale.
     * @return an axis with default ticks
     */
    public static TransformedAxis log1p10() {
        return new LogAxis(10);
    }

    /**
     * Create an axis to display values on a log10(x+1) scale.
     * @param nTicks the number of minor ticks
     * @return an axis with a suitable number of minor ticks
     */
    public static TransformedAxis log1p10(int nTicks) {
        return new LogAxis(10, nTicks);
    }

    /**
     * Create an axis to display values on a log2(x+1) scale.
     * @return an axis with default ticks
     */
    public static TransformedAxis log1p2() {
        return new LogAxis(2, 9);
    }

    /**
     * Create an axis to display values on a log2(x+1) scale.
     * @param nTicks the number of minor ticks
     * @return an axis with n minor ticks
     */
    public static TransformedAxis log1p2(int nTicks) {
        return new LogAxis(2, nTicks);
    }

    static class LogAxis extends TransformedAxis {

        private final double base;
        private final int nTicks;

        LogAxis(double base) {
            this(base, 9);
        }

        LogAxis(double base, int nTicks) {
            super((d) -> log(d + 1, base), (d) -> Math.pow(base,  d - 1));
            this.base = base;
            this.nTicks = nTicks;
        }

        @Override
        protected List<Number> calculateTickValues(double length, Object range) {
            Object[] orange = (Object[]) range;
            double lowerBound = (double)orange[0];
            double upperBound = (double)orange[1];
            List<Number> tickValues = new ArrayList<>();
            double minValue = transform.applyAsDouble(lowerBound);
            double maxValue = transform.applyAsDouble(upperBound);
            minValue = Math.floor(minValue);
            maxValue = Math.ceil(maxValue);
            for (double x = minValue; x <= maxValue; x++) {
                tickValues.add(inverseTransform.applyAsDouble(x));
            }
            return tickValues;
        }

        @Override
        protected List<Number> calculateMinorTickMarks() {
            List<Number> minorTicks = new ArrayList<>();
            // called decade even though may be arbitrary base
            double currentDecade = Math.pow(base, Math.floor(transform.applyAsDouble(getLowerBound())));
            double upperBound = getUpperBound();

            while (currentDecade <= upperBound) {
                // add a tick at each of N ticks between current and next decade
                for (int i = 0; i < nTicks; i++) {
                    minorTicks.add(currentDecade * (1 + (i * (base - 1)) / nTicks));
                }
                currentDecade *= base;
            }
            return minorTicks;
        }
    }

}
