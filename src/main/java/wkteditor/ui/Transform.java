package wkteditor.ui;

/**
 * Stores and calculates the translation and zoom.
 */
public class Transform {
    private double translateX;
    private double translateY;
    private double zoom;

    Transform() {
        translateX = 0;
        translateY = 0;
        zoom = 1.0f;
    }

    /**
     * Sets the zoom factor.
     *
     * @param zoom The new zoom factor.
     */
    void setZoom(double zoom) {
        this.zoom = zoom;
    }

    double getZoom() {
        return zoom;
    }

    /**
     * Sets the translation.
     *
     * @param x The new translation in x direction.
     * @param y The new translation in y direction.
     */
    void setTranslation(double x, double y) {
        translateX = x;
        translateY = y;
    }

    /**
     * Adds the given translation to the current translation.
     *
     * @param x The difference in translation in x direction.
     * @param y The difference in translation in y direction.
     */
    void setTranslationRelative(double x, double y) {
        translateX += x;
        translateY += y;
    }

    /**
     * Transforms the given x-coordinate according to the current settings.
     *
     * @param x The x-coordinate to transform.
     * @return The transformed coordinate.
     */
    public int transformX(int x) {
        return (int) ((x + translateX) * zoom);
    }

    /**
     * Transforms the given y-coordinate according to the current settings.
     *
     * @param y The y-coordinate to transform.
     * @return The transformed coordinate.
     */
    public int transformY(int y) {
        return (int) ((y + translateY) * zoom);
    }

    /**
     * Zooms the given value.
     *
     * @param i The value to zoom.
     * @return The zoomed value.
     */
    public int zoom(int i) {
        return (int) (i * zoom);
    }

    /**
     * Reverts any transformations done to x.
     *
     * @param x The x-coordinate to reverse transform.
     * @return The normal x-coordinate.
     */
    int reverseTransformX(int x) {
        return (int) ((((double) x) / zoom) - translateX);
    }

    /**
     * Reverts any transformations done to y.
     *
     * @param y The y-coordinate to reverse transform.
     * @return The normal y-coordinate.
     */
    int reverseTransformY(int y) {
        return (int) ((((double) y) / zoom) - translateY);
    }
}
