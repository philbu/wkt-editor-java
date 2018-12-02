package wkteditor;

import wkteditor.ui.WKTFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;

public enum CursorMode {
    SELECT(null, "cursor.select", "cursor", false, false,
            KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_SELECT),
    POINT(WKTPoint.class, "cursor.point", "point", true, false,
            KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_POINT),
    LINE(WKTLineString.class, "cursor.line", "line", true, false,
            KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_LINE),
    POLYGON(WKTPolygon.class, "cursor.polygon", "polygon", true, true,
            KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_POLYGON);

    private final Class<? extends WKTElement> wktElemCls;
    private final String nameRes;
    private final String iconName;
    private final KeyStroke keyStroke;
    private final String actionCommand;
    private final boolean isElement;
    private final boolean subElements;

    CursorMode(Class<? extends WKTElement> wktElemCls, String nameRes, String iconName,
               boolean isElement, boolean subElements, KeyStroke keyStroke, String actionCommand) {
        this.wktElemCls = wktElemCls;
        this.nameRes = nameRes;
        this.iconName = iconName;
        this.isElement = isElement;
        this.subElements = subElements;
        this.keyStroke = keyStroke;
        this.actionCommand = actionCommand;
    }

    public String getIconName() {
        return iconName;
    }

    public String getNameRes() {
        return nameRes;
    }

    public Class<? extends WKTElement> getWktClass() {
        return wktElemCls;
    }

    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public boolean isElement() {
        return isElement;
    }

    public boolean hasSubElements() {
        return subElements;
    }
}
