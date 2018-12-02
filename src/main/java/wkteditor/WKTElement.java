package wkteditor;

import java.awt.*;

public abstract class WKTElement {
    public WKTElement() {

    }

    public WKTElement(int x, int y) {
        this();
        add(x, y);
    }

    public abstract void add(int x, int y);

    public abstract boolean canAdd();

    public abstract String toWKT();

    public abstract void paint(Graphics2D g, DisplayOptions opt);

    public void endSubElement() {

    }
}
