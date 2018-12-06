package wkteditor.ui;

import wkteditor.WKTEditor;
import wkteditor.WKTElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This pane displays the wkt elements, that are being edited.
 */
public class WKTPane extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {
    private WKTEditor editor;
    private Image bgImage;

    private int dragX;
    private int dragY;
    private double zoom;

    public WKTPane(WKTEditor editor) {
        this.editor = editor;
        zoom = editor.getDisplayOptions().getTransform().getZoom();

        setPreferredSize(new Dimension(200, 200));
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    /**
     * Sets an image that is displayed in the background of the wkt elements.
     *
     * @param image The new image to display, or <code>null</code> to remove the
     *              image.
     */
    public void setBackgroundImage(Image image) {
        bgImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        DisplayOptions opt = editor.getDisplayOptions();

        // Background
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (bgImage != null) {
            Transform transform = editor.getDisplayOptions().getTransform();
            g2d.drawImage(bgImage, transform.transformX(0), transform.transformY(0),
                    transform.zoom(bgImage.getWidth(null)), transform.zoom(bgImage.getHeight(null)), null);
        }

        // Foreground
        g2d.setColor(getForeground());
        for (WKTElement element : editor.getElements()) {
            element.paint(g2d, opt);
        }

        WKTElement curElement = editor.getCurrentElement();
        if (curElement != null) {
            g2d.setColor(new Color(255, 95, 74));
            curElement.paint(g2d, opt);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            Transform transform = editor.getDisplayOptions().getTransform();
            editor.addPoint(transform.reverseTransformX(event.getX()),
                    transform.reverseTransformY(event.getY()));
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON3) {
            dragX = event.getX();
            dragY = event.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseExited(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (event.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
            editor.getDisplayOptions().getTransform()
                    .setTranslationRelative(event.getX() - dragX, event.getY() - dragY);
            dragX = event.getX();
            dragY = event.getY();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        zoom -= event.getPreciseWheelRotation() * 0.2;
        Transform transform = editor.getDisplayOptions().getTransform();
        transform.setZoom(zoom);
        repaint();
    }
}
