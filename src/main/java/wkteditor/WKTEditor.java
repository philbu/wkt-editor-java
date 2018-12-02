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

    public void setElementChangeListener(ElementChangeListener listener) {
        this.listener = listener;
    }

    public Collection<WKTElement> getElements() {
        return elements;
    }

    public DisplayOptions getDisplayOptions() {
        return displayOpt;
    }

    public CursorMode getCursorMode() {
        return cursorMode;
    }

    public void setCursorMode(CursorMode cursorMode) {
        this.cursorMode = cursorMode;
    }

    public WKTElement getCurrentElement() {
        return curElement;
    }

    public void endCurrentElement() {
        if (curElement == null) {
            return;
        }

        elements.add(curElement);
        curElement = null;
        onElementChanged();
    }

    public void endCurrentSubElement() {
        if (!cursorMode.hasSubElements()) {
            return;
        }

        curElement.endSubElement();
        onElementChanged();
    }

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

    private void onElementChanged() {
        if (listener != null) {
            listener.onElementChanged();
        }
    }

    public interface ElementChangeListener {
        void onElementChanged();
    }
}
