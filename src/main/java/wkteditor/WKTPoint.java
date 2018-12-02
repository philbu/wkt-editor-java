package wkteditor;

import java.awt.*;
import java.util.Objects;

/**
 * A wkt point.
 */
public class WKTPoint extends WKTElement {
    private int x;
    private int y;

    public WKTPoint() {
        super();
    }

    public WKTPoint(int x, int y) {
        super(x, y);
    }

    @Override
    public void add(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean canAdd() {
        return false;
    }

    /**
     * Gets the x-coordinate of this point.
     *
     * @return The x-coordinate of this point.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this point.
     *
     * @return The y-coordinate of this point.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of this point.
     *
     * @param x The new x-coordinate of this point.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of this point.
     *
     * @param y The new y-coordinate of this point.
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toWKT() {
        return "POINT (" + String.valueOf(x) + " " + String.valueOf(y) + ")";
    }

    @Override
    public void paint(Graphics2D g, DisplayOptions opt) {
        g.fillOval(x - opt.getPointRadius(), y - opt.getPointRadius(),
                opt.getPointDiameter(), opt.getPointDiameter());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WKTPoint wktPoint = (WKTPoint) o;
        return x == wktPoint.x && y == wktPoint.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return toWKT();
    }
}
