package wkteditor.ui;

import wkteditor.CursorMode;
import wkteditor.WKTEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * This frame displays the currently edited wkt file and provides UI elements
 * to edit the wkt elements.
 */
public class WKTFrame extends JFrame implements ActionListener, WKTEditor.ElementChangeListener {
    private static final String AC_SAVE = "actionCommand:save";
    public static final String AC_CURSOR_SELECT = "actionCommand:cursorSelect";
    public static final String AC_CURSOR_POINT = "actionCommand:cursorPoint";
    public static final String AC_CURSOR_LINE = "actionCommand:cursorLine";
    public static final String AC_CURSOR_POLYGON = "actionCommand:cursorPolygon";
    private static final String AC_END_ELEMENT = "actionCommand:endElement";
    private static final String AC_END_SUB_ELEMENT = "actionCommand:endSubElement";
    private static final String AC_SET_BG_IMAGE = "actionCommand:setBgImage";
    private static final String AC_REMOVE_BG_IMAGE = "actionCommand:removeBgImage";

    private ResourceBundle strings;
    private WKTEditor editor;

    private WKTPane wktPane;
    private ButtonGroup cursorMenuGroup;
    private ButtonGroup cursorToolbarGroup;
    private Map<CursorMode, ButtonModel> menuButtonMap;
    private Map<CursorMode, ButtonModel> toolbarButtonMap;
    private List<ButtonModel> endElementModels;
    private List<ButtonModel> endSubElementModels;

    public WKTFrame(WKTEditor editor) {
        this.editor = editor;
        strings = ResourceBundle.getBundle("lang/strings");
        endElementModels = new ArrayList<>(2);
        endSubElementModels = new ArrayList<>(2);

        setLayout(new BorderLayout());
        setTitle(strings.getString("name"));

        wktPane = new WKTPane(editor);
        add(wktPane, BorderLayout.CENTER);
        add(buildToolBar(), BorderLayout.NORTH);

        setJMenuBar(buildMenuBar());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(200, 200));
        setLocationRelativeTo(null);

        onModeChanged(editor.getCursorMode());

        setVisible(true);
    }

    /**
     * Builds the complete menu.
     *
     * @return The created menu.
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu(strings.getString("menu.file"));
        menuBar.add(menuFile);

        JMenuItem menuFileSave = new JMenuItem(strings.getString("menu.file.save"));
        menuFileSave.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menuFileSave.setActionCommand(AC_SAVE);
        menuFileSave.addActionListener(this);
        menuFile.add(menuFileSave);

        JMenu menuEdit = new JMenu(strings.getString("menu.edit"));
        menuBar.add(menuEdit);

        cursorMenuGroup = new ButtonGroup();
        menuButtonMap = new EnumMap<>(CursorMode.class);


        for (CursorMode mode : CursorMode.values()) {
            JRadioButtonMenuItem menuEditCursor = new JRadioButtonMenuItem(
                    strings.getString(mode.getNameRes()));
            menuEditCursor.setAccelerator(mode.getKeyStroke());
            menuEditCursor.setActionCommand(mode.getActionCommand());
            menuEditCursor.addActionListener(this);
            cursorMenuGroup.add(menuEditCursor);
            menuButtonMap.put(mode, menuEditCursor.getModel());
            menuEdit.add(menuEditCursor);
        }

        menuEdit.addSeparator();

        JMenuItem menuEditEndElement = new JMenuItem(strings.getString("menu.edit.endElement"));
        menuEditEndElement.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        menuEditEndElement.setActionCommand(AC_END_ELEMENT);
        menuEditEndElement.addActionListener(this);
        endElementModels.add(menuEditEndElement.getModel());
        menuEdit.add(menuEditEndElement);

        JMenuItem menuEditEndSubElement = new JMenuItem(strings.getString("menu.edit.endElement.sub"));
        menuEditEndSubElement.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK));
        menuEditEndSubElement.setActionCommand(AC_END_SUB_ELEMENT);
        menuEditEndSubElement.addActionListener(this);
        endSubElementModels.add(menuEditEndSubElement.getModel());
        menuEdit.add(menuEditEndSubElement);

        JMenu menuView = new JMenu(strings.getString("menu.view"));
        menuBar.add(menuView);

        JMenuItem menuViewImage = new JMenuItem(
                strings.getString("menu.view.bgImage"));
        menuViewImage.setActionCommand(AC_SET_BG_IMAGE);
        menuViewImage.addActionListener(this);
        menuView.add(menuViewImage);

        JMenuItem menuViewImageRemove = new JMenuItem(
                strings.getString("menu.view.bgImage.remove"));
        menuViewImageRemove.setActionCommand(AC_REMOVE_BG_IMAGE);
        menuViewImageRemove.addActionListener(this);
        menuView.add(menuViewImageRemove);

        return menuBar;
    }

    /**
     * Builds the complete toolbar.
     *
     * @return The created toolbar.
     */
    private JToolBar buildToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);

        JButton buttonSave = buildToolbarButton("save",
                "toolbar.save", AC_SAVE);
        toolBar.add(buttonSave);

        toolBar.addSeparator();

        cursorToolbarGroup = new ButtonGroup();
        toolbarButtonMap = new EnumMap<>(CursorMode.class);

        for (CursorMode mode : CursorMode.values()) {
            JButton buttonCursor = buildToolbarButton(mode.getIconName(),
                    mode.getNameRes(), mode.getActionCommand());
            cursorToolbarGroup.add(buttonCursor);
            toolbarButtonMap.put(mode, buttonCursor.getModel());
            toolBar.add(buttonCursor);
        }

        toolBar.addSeparator();

        JButton buttonEndElement = buildToolbarButton("end_shape",
                "toolbar.endElement", AC_END_ELEMENT);
        endElementModels.add(buttonEndElement.getModel());
        toolBar.add(buttonEndElement);

        JButton buttonEndSubElement = buildToolbarButton("end_sub_shape",
                "toolbar.endElement.sub", AC_END_SUB_ELEMENT);
        endSubElementModels.add(buttonEndSubElement.getModel());
        toolBar.add(buttonEndSubElement);

        return toolBar;
    }

    /**
     * Builds a single button for the toolbar.
     *
     * @param iconName      The name of the button icon in the buttons resources
     *                      folder.
     * @param nameRes       The id of the name resource in the resource bundle.
     * @param actionCommand The action command that is executed when the button
     *                      is clicked.
     * @return A new button with the above specified settings.
     */
    private JButton buildToolbarButton(String iconName, String nameRes,
                                       String actionCommand) {
        String name = strings.getString(nameRes);
        URL iconUrl = WKTFrame.class.getResource("/buttons/" + iconName + ".png");

        JButton button = new JButton();
        button.setToolTipText(name);
        button.setOpaque(false);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);

        if (iconUrl != null) {
            button.setIcon(new ImageIcon(iconUrl, name));
        } else {
            button.setText(name);
        }

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String ac = event.getActionCommand();
        if (ac == null) {
            return;
        }

        switch (ac) {
            case AC_SET_BG_IMAGE:
                setBgImage();
                break;
            case AC_REMOVE_BG_IMAGE:
                wktPane.setBackgroundImage(null);
                break;
            case AC_SAVE:
                saveWkt();
                break;
            case AC_CURSOR_SELECT:
                editor.endCurrentElement();
                editor.setCursorMode(CursorMode.SELECT);
                onModeChanged(CursorMode.SELECT);
                break;
            case AC_CURSOR_POINT:
                editor.endCurrentElement();
                editor.setCursorMode(CursorMode.POINT);
                onModeChanged(CursorMode.POINT);
                break;
            case AC_CURSOR_LINE:
                editor.endCurrentElement();
                editor.setCursorMode(CursorMode.LINE);
                onModeChanged(CursorMode.LINE);
                break;
            case AC_CURSOR_POLYGON:
                editor.endCurrentElement();
                editor.setCursorMode(CursorMode.POLYGON);
                onModeChanged(CursorMode.POLYGON);
                break;
            case AC_END_ELEMENT:
                editor.endCurrentElement();
                break;
            case AC_END_SUB_ELEMENT:
                editor.endCurrentSubElement();
                break;
        }
    }

    /**
     * Shows a file dialog to let the user select the destination file. If a
     * file was selected, forwards the save operation to the {@link WKTEditor}.
     */
    private void saveWkt() {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (!file.isFile()) {
                    return true;
                }

                String[] parts = file.getName().split("\\.");
                if (parts.length < 2) {
                    return false;
                }
                String ext = parts[parts.length - 1].toLowerCase();
                return "wkt".equals(ext);
            }

            @Override
            public String getDescription() {
                return strings.getString("fileFilter.wkt");
            }
        });
        final int result = fc.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            editor.save(fc.getSelectedFile());
        }
    }

    /**
     * Shows a file dialog to let the user select a background image. If an
     * image was selected, updates the {@link WKTPane} accordingly.
     */
    private void setBgImage() {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (!file.isFile()) {
                    return true;
                }

                String[] parts = file.getName().split("\\.");
                if (parts.length < 2) {
                    return false;
                }
                String ext = parts[parts.length - 1].toLowerCase();
                return "png".equals(ext) || "jpg".equals(ext) || "jpeg".equals(ext);
            }

            @Override
            public String getDescription() {
                return strings.getString("fileFilter.images");
            }
        });
        final int result = fc.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                wktPane.setBackgroundImage(ImageIO.read(fc.getSelectedFile()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Called when the cursor mode changed. Updates the related UI elements to
     * represent the new cursor mode.
     *
     * @param mode The new cursor mode.
     */
    private void onModeChanged(CursorMode mode) {
        cursorMenuGroup.setSelected(menuButtonMap.get(mode), true);
        cursorToolbarGroup.setSelected(toolbarButtonMap.get(mode), true);

        for (ButtonModel model : endElementModels) {
            model.setEnabled(mode.isElement());
        }
        for (ButtonModel model : endSubElementModels) {
            model.setEnabled(mode.isElement() && mode.hasSubElements());
        }
    }

    @Override
    public void onElementChanged() {
        wktPane.repaint();
    }
}
