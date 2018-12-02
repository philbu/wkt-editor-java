package wkteditor.ui;

import wkteditor.DisplayOptions;
import wkteditor.WKTEditor;
import wkteditor.WKTElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WKTPane extends JComponent implements MouseListener {
    private WKTEditor editor;
    private Image bgImage;

    public WKTPane(WKTEditor editor) {
        this.editor = editor;
        setPreferredSize(new Dimension(200, 200));
        addMouseListener(this);
    }

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
            g2d.drawImage(bgImage, 0, 0, null);
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
            editor.addPoint(event.getX(), event.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        // Ignored
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
    public void mouseExited(MouseEvent e) {
        // Ignored
    }
}
