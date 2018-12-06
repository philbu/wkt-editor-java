package wkteditor.ui;

/**
 * The display options define how the wkt elements are drawn within the editor.
 */
public class DisplayOptions {
    private int pointRadius;
    private float lineWidth;
    private Transform transform;

    public DisplayOptions() {
        pointRadius = 4;
        lineWidth = 2.0f;
        transform = new Transform();
    }

    /**
     * Gets the radius of a point.
     *
     * @return The radius of a point.
     */
    public int getPointRadius() {
        return pointRadius;
    }

    /**
     * Gets the diameter of a point.
     *
     * @return The diameter of a point.
     */
    public int getPointDiameter() {
        return pointRadius * 2;
    }

    /**
     * Sets the radius of a point.
     *
     * @param pointRadius The new radius.
     */
    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
    }

    /**
     * Sets the width of a line.
     *
     * @param lineWidth The new line width.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Gets the width of a line.
     *
     * @return The new line width.
     */
    public float getLineWidth() {
        return lineWidth;
    }

    public Transform getTransform() {
        return transform;
    }
}
