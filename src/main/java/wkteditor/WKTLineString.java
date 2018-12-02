package wkteditor;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class WKTLineString extends WKTElement {
    private LinkedList<WKTPoint> points;

    public WKTLineString() {
        super();
        points = new LinkedList<>();
    }

    public WKTLineString(int x, int y) {
        super(x, y);
    }

    public void add(int x, int y) {
        points.add(new WKTPoint(x, y));
    }

    @Override
    public boolean canAdd() {
        return true;
    }

    @Override
    public String toString() {
        return toWKT();
    }

    @Override
    public String toWKT() {
        return "LINESTRING ("
                + points.stream()
                .map(p -> p.getX() + " " + p.getY())
                .collect(Collectors.joining(", "))
                + ")";
    }

    @Override
    public void paint(Graphics2D g, DisplayOptions opt) {
        Iterator<WKTPoint> iterator = points.iterator();
        WKTPoint prev = null;

        g.setStroke(new BasicStroke(opt.getLineWidth()));
        while (iterator.hasNext()) {
            WKTPoint cur = iterator.next();
            if (prev != null) {
                g.drawLine(prev.getX(), prev.getY(), cur.getX(), cur.getY());
            }
            cur.paint(g, opt);

            prev = cur;
        }
    }
}
