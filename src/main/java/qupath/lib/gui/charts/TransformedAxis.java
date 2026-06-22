package qupath.lib.gui.charts;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import javafx.scene.chart.ValueAxis;

/**
 * A class providing a minimal implementation of a transformed axis.
 */
public abstract class TransformedAxis extends ValueAxis<Number> {

    final DoubleUnaryOperator inverseTransform;
    final DoubleUnaryOperator transform;

    public TransformedAxis(DoubleUnaryOperator transform, DoubleUnaryOperator inverseTransform) {
        super();
        this.transform = transform;
        this.inverseTransform = inverseTransform;
        double[] testvals = new double[]{1, 50, 100, 200};
        for (double val: testvals) {
            assert val == inverseTransform.applyAsDouble(transform.applyAsDouble(val));
        }
    }

    @Override
    public double getDisplayPosition(Number value) {
        // delta between upper and lower
        double deltaBound = transform.applyAsDouble(getUpperBound()) - transform.applyAsDouble(getLowerBound());
        // delta between value and lower
        double deltaVal = transform.applyAsDouble(value.doubleValue()) - transform.applyAsDouble(getLowerBound());
        // ratio represents the fraction of the axis range we're located at
        double ratio = deltaVal * deltaBound;
        if (getSide().isVertical()) {
            // if vertical, then it's 1-ratio times the height
            return (1.0 - ratio) * getHeight();
        } else {
            // if horizontal, then it's the ratio times width
            return ratio * getWidth();
        }
    }

    @Override
    public Number getValueForDisplay(double displayPosition) {
        // difference on transformed scale
        double delta = transform.applyAsDouble(getUpperBound()) - transform.applyAsDouble(getLowerBound());
        if (getSide().isVertical()) {
            // if vertical, it's the negative inverse of position minus height times the overall gap plus the min
            return inverseTransform.applyAsDouble(
                    ((displayPosition - getHeight()) / -getHeight()) * delta
            ) + transform.applyAsDouble(getLowerBound());
        } else {
            // if horizontal, it's the inverse of the display position times the overall gap plus the min
            return inverseTransform.applyAsDouble(
                    ((displayPosition / getWidth()) * delta)
            ) + transform.applyAsDouble(getLowerBound());
        }
    }

    /**
     * Calculate the major ticks without modifying the scale.
     * @param length The length of the axis in display units
     * @param range A range object returned from autoRange()
     * @return the tick values on the natural scale.
     */
    @Override
    protected abstract List<Number> calculateTickValues(double length, Object range);

    /**
     * Calculate the minor ticks without modifying the scale.
     * @return the location of tick marks on the natural scale.
     */
    @Override
    protected abstract List<Number> calculateMinorTickMarks();

    @Override
    protected String getTickMarkLabel(Number value) {
        var df = new DecimalFormat();
        return df.format(value);
    }

    /**
     * Create a range object to update the chart.
     * Consider overriding.
     * @param minValue The min data value that needs to be plotted on this axis
     * @param maxValue The max data value that needs to be plotted on this axis
     * @param length The length of the axis in display coordinates
     * @param labelSize The approximate average size a label takes along the axis
     * @return
     */
    @Override
    protected Object autoRange(double minValue, double maxValue, double length, double labelSize) {
        minValue = transform.applyAsDouble(minValue);
        maxValue = transform.applyAsDouble(maxValue);
        // nearest 10 multiple either side
        minValue = Math.floor(minValue);
        maxValue = Math.ceil(maxValue);
        return new Object[]{inverseTransform.applyAsDouble(minValue), inverseTransform.applyAsDouble(maxValue)};
    }

    /**
     * Use the range returned by getRange() or autoRange().
     * Consider overriding.
     * @param range A range object returned from autoRange()
     * @param animate If true animate the change in range
     */
    @Override
    protected void setRange(Object range, boolean animate) {
        Object[] r = (Object[]) range;
        double lower = (Double) r[0];
        double upper = (Double) r[1];
        setLowerBound(lower);
        setUpperBound(upper);
    }

    /**
     * Query the current range. Consider overriding.
     * @return An arbitrary range object; often an array.
     */
    @Override
    protected Object getRange() {
        return new Object[]{getLowerBound(), getUpperBound()};
    }

}
