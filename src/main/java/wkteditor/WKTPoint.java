package wkteditor;

import java.awt.*;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WKTPoint)) {
            return false;
        }
        WKTPoint other = (WKTPoint) o;
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return toWKT();
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
}
