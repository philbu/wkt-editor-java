package wkteditor;

import wkteditor.ui.WKTFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WKTEditor {
    public static void main(String[] args) {
        WKTEditor editor = new WKTEditor();
        WKTFrame frame = new WKTFrame(editor);
        editor.setElementChangeListener(frame);
    }

    private DisplayOptions displayOpt;
    private CursorMode cursorMode;
    private WKTElement curElement;
    private List<WKTElement> elements;
    private ElementChangeListener listener;

    public WKTEditor() {
        displayOpt = new DisplayOptions();
        cursorMode = CursorMode.SELECT;
        elements = new ArrayList<>();
    }

    /**
     * Sets a listener that is notified whenever a wkt element changes.
     *
     * @param listener The listener to set.
     */
    public void setElementChangeListener(ElementChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Gets all elements in the current wkt file.
     *
     * @return A collection of elements in the current wkt file.
     */
    public Collection<WKTElement> getElements() {
        return elements;
    }

    /**
     * Gets the current display options. These options specify how the wkt
     * elements are displayed in the editor.
     *
     * @return The display options.
     */
    public DisplayOptions getDisplayOptions() {
        return displayOpt;
    }

    /**
     * Gets the current cursor mode. This mode specifies the actions take when
     * the user clicks within the editor.
     *
     * @return The cursor mode.
     */
    public CursorMode getCursorMode() {
        return cursorMode;
    }

    /**
     * Updates the cursor mode. This mode specifies the actions takes when the
     * user clicks within the editor.
     *
     * @param cursorMode The new cursor mode.
     */
    public void setCursorMode(CursorMode cursorMode) {
        this.cursorMode = cursorMode;
    }

    /**
     * Gets the wkt element that is currently being edited.
     *
     * @return The wkt element selected for editing.
     */
    public WKTElement getCurrentElement() {
        return curElement;
    }

    /**
     * Ends the current to allow creation / selection of a new element. The
     * element being ended will be added back to the list of all elements.
     *
     * @see #endCurrentSubElement()
     */
    public void endCurrentElement() {
        if (curElement == null) {
            return;
        }

        elements.add(curElement);
        curElement = null;
        onElementChanged();
    }

    /**
     * If the current element supports sub elements, this will end the current
     * sub element and start a new one.
     *
     * @see #endCurrentElement()
     */
    public void endCurrentSubElement() {
        if (!cursorMode.hasSubElements()) {
            return;
        }

        curElement.endSubElement();
        onElementChanged();
    }

    /**
     * Save the edited wkt elements to the specified file.
     *
     * @param file The file to save the wkt elements to.
     */
    public void save(File file) {
        endCurrentElement();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (WKTElement element : elements) {
                String wkt = element.toWKT();
                wkt += "\n";
                fos.write(wkt.getBytes(Charset.forName("UTF-8")));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Adds the specified point to the currently edited element. If no element
     * is being edited, a new one will be created.
     *
     * @param x The x-coordinate of the point to add.
     * @param y The y-coordinate of the point to add.
     */
    public void addPoint(int x, int y) {
        if (!cursorMode.isElement()) {
            // TODO select element
            return;
        }

        // Add point to current element
        if (curElement != null && curElement.getClass() != cursorMode.getWktClass()) {
            System.err.println("Cursor mode changed without ending previous element!");
            endCurrentElement();
        }

        if (curElement != null && !curElement.canAdd()) {
            endCurrentElement();
        }

        if (curElement == null) {
            try {
                curElement = cursorMode.getWktClass().newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                exception.printStackTrace();
                return;
            }
        }

        curElement.add(x, y);
        onElementChanged();
    }

    /**
     * Called when an element has changed. If a listener is set, it will be
     * notified about the change.
     */
    private void onElementChanged() {
        if (listener != null) {
            listener.onElementChanged();
        }
    }

    /**
     * A listener for element changes.
     */
    public interface ElementChangeListener {
        /**
         * Called when an element has changed.
         */
        void onElementChanged();
    }
}
