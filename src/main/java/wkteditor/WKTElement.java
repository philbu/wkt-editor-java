package wkteditor;

import java.awt.*;

/**
 * An element of the wkt file.
 */
public abstract class WKTElement {
    public WKTElement() {

    }

    public WKTElement(int x, int y) {
        this();
        add(x, y);
    }

    /**
     * Add the specified point to this element.
     *
     * @param x The x-coordinate of the point to add.
     * @param y The y-coordinate fo the point to add.
     */
    public abstract void add(int x, int y);

    /**
     * Checks if this element can accept new points.
     *
     * @return Whether this element can accept new points.
     */
    public abstract boolean canAdd();

    /**
     * Converts the element to a wkt string.
     *
     * @return The wkt string representing this element.
     */
    public abstract String toWKT();

    /**
     * Paints this element in the editor.
     *
     * @param g   The graphics to paint with.
     * @param opt The display options defining how the wkt elements should look.
     */
    public abstract void paint(Graphics2D g, DisplayOptions opt);

    /**
     * Ends the current sub element and starts a new one.
     */
    public void endSubElement() {

    }
}
