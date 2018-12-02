package wkteditor;

public class DisplayOptions {
    private int pointRadius;
    private float lineWidth;

    public DisplayOptions() {
        pointRadius = 4;
        lineWidth = 2.0f;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public int getPointDiameter() {
        return pointRadius * 2;
    }

    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getLineWidth() {
        return lineWidth;
    }
}
