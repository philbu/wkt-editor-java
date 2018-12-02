package wkteditor;

import wkteditor.ui.WKTFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

/**
 * The cursor mode specifies how clicks in the editor are interpreted.
 */
public enum CursorMode {
    SELECT(null, "cursor.select", "cursor", false,
            KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_SELECT),
    POINT(WKTPoint.class, "cursor.point", "point", false,
            KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_POINT),
    LINE(WKTLineString.class, "cursor.line", "line", false,
            KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_LINE),
    POLYGON(WKTPolygon.class, "cursor.polygon", "polygon", true,
            KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_DOWN_MASK),
            WKTFrame.AC_CURSOR_POLYGON);

    private final Class<? extends WKTElement> wktElemCls;
    private final String nameRes;
    private final String iconName;
    private final KeyStroke keyStroke;
    private final String actionCommand;
    private final boolean subElements;

    CursorMode(Class<? extends WKTElement> wktElemCls, String nameRes, String iconName,
               boolean subElements, KeyStroke keyStroke, String actionCommand) {
        this.wktElemCls = wktElemCls;
        this.nameRes = nameRes;
        this.iconName = iconName;
        this.subElements = subElements;
        this.keyStroke = keyStroke;
        this.actionCommand = actionCommand;
    }

    /**
     * Gets the name of the icon, that represents this cursor mode, in the
     * buttons resource directory.
     *
     * @return The name of the icon image file.
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * Gets the name resource that can be used with the {@link ResourceBundle}
     * to get the name of this cursor mode.
     *
     * @return The name resource of this cursor mode.
     */
    public String getNameRes() {
        return nameRes;
    }

    /**
     * Gets the class of a wkt element that will be edited / created upon user
     * interaction.
     *
     * @return The class of a wkt element, or <code>null</code> if this cursor
     * mode cannot be used with a specific element.
     */
    public Class<? extends WKTElement> getWktClass() {
        return wktElemCls;
    }

    /**
     * This key stroke causes this cursor mode to become active.
     *
     * @return The key stroke for this cursor mode.
     */
    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    /**
     * This action command will be executed when an UI element representing this
     * cursor mode is selected.
     *
     * @return The action command for this cursor mode.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Whether this cursor mode can be used with a specific wkt element.
     *
     * @return <code>true</code> if this cursor mode can be used with a specific
     * wkt element.
     */
    public boolean isElement() {
        return wktElemCls != null;
    }

    /**
     * Whether the wkt element used with this cursor mode has sub elements. If
     * this cursor mode cannot be used with a specific wkt element, the result
     * of this method has no meaning and should be ignored.
     *
     * @return <code>true</code> if the wkt element represented by this cursor
     * mode supports sub elements.
     */
    public boolean hasSubElements() {
        if (!isElement()) {
            return false;
        }
        return subElements;
    }
}
