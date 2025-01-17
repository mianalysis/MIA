package io.github.mianalysis.mia.gui.regions.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import ij.Prefs;
import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.gui.GUI;
import io.github.mianalysis.mia.gui.GUIAnalysisHandler;
import io.github.mianalysis.mia.gui.UndoRedoStore;
import io.github.mianalysis.mia.process.logging.LogRenderer;
import io.github.mianalysis.mia.process.logging.LogRenderer.Level;

public class CustomMenuBar extends JMenuBar implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 779793751255990466L;

    private static JMenu fileMenu = new JMenu("File");
    private static JMenu editMenu = new JMenu("Edit");
    private static JMenu analysisMenu = new JMenu("Analysis");
    private static JMenu viewMenu = new JMenu("View");
    private static JMenu helpMenu = new JMenu("Help");
    private static JMenu blankMenu = new JMenu("");
    private static JMenu logMenu = new JMenu("Logging");

    private static MenuItem newWorkflow = new MenuItem(MenuItem.NEW_WORKFLOW);
    private static MenuItem loadWorkflow = new MenuItem(MenuItem.LOAD_WORKFLOW);
    private static MenuItem saveWorkflow = new MenuItem(MenuItem.SAVE_WORKFLOW);
    private static MenuItem saveWorkflowAs = new MenuItem(MenuItem.SAVE_WORKFLOW_AS);

    private static MenuItem resetAnalysis = new MenuItem(MenuItem.RESET_ANALYSIS);
    private static MenuItem enableAllModules = new MenuItem(MenuItem.ENABLE_ALL);
    private static MenuItem disableAllModules = new MenuItem(MenuItem.DISABLE_ALL);
    private static MenuItem outputAllModules = new MenuItem(MenuItem.OUTPUT_ALL);
    private static MenuItem silenceAllModules = new MenuItem(MenuItem.SILENCE_ALL);

    private static SidebarMenuCheckbox sidebarMenuCheckbox = new SidebarMenuCheckbox();

    private static MenuItem undo = new MenuItem(MenuItem.UNDO);
    private static MenuItem redo = new MenuItem(MenuItem.REDO);

    public CustomMenuBar() {
        // Creating the file menu
        fileMenu.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        fileMenu.setFont(GUI.getDefaultFont().deriveFont(14f));
        add(fileMenu);
        fileMenu.add(newWorkflow);
        fileMenu.add(loadWorkflow);
        fileMenu.add(saveWorkflow);
        fileMenu.add(saveWorkflowAs);

        // Creating the edit menu
        editMenu.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        editMenu.setFont(GUI.getDefaultFont().deriveFont(14f));
        add(editMenu);
        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.addSeparator();
        editMenu.add(new MenuItem(MenuItem.COPY));
        editMenu.add(new MenuItem(MenuItem.PASTE));
        editMenu.addSeparator();
        editMenu.add(new MenuItem(MenuItem.PREFERENCES));

        // Creating the analysis menu
        analysisMenu.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        analysisMenu.setFont(GUI.getDefaultFont().deriveFont(14f));
        add(analysisMenu);
        analysisMenu.add(new MenuItem(MenuItem.RUN_ANALYSIS));
        analysisMenu.add(new MenuItem(MenuItem.STOP_ANALYSIS));
        analysisMenu.add(resetAnalysis);
        analysisMenu.addSeparator();
        analysisMenu.add(enableAllModules);
        analysisMenu.add(disableAllModules);
        analysisMenu.add(outputAllModules);
        analysisMenu.add(silenceAllModules);

        // Creating the new menu
        viewMenu.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        viewMenu.setFont(GUI.getDefaultFont().deriveFont(14f));
        add(viewMenu);
        if (MIA.isDebug())
            viewMenu.add(new MenuItem(MenuItem.PROCESSING_VIEW));
        else
            viewMenu.add(new MenuItem(MenuItem.EDITING_VIEW));
        viewMenu.add(sidebarMenuCheckbox);

        // Creating the help menu
        add(helpMenu);
        helpMenu.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        helpMenu.setFont(GUI.getDefaultFont().deriveFont(14f));
        helpMenu.add(new MenuItem(MenuItem.SHOW_ABOUT));
        helpMenu.add(new MenuItem(MenuItem.SHOW_GETTING_STARTED));
        helpMenu.add(new MenuItem(MenuItem.SHOW_UNAVAILABLE_MODULES));

        helpMenu.add(logMenu);
        logMenu.setFont(GUI.getDefaultFont().deriveFont(14f));

        LogRenderer renderer = MIA.getMainRenderer();

        Level level = Level.DEBUG;
        renderer.setWriteEnabled(level, Prefs.get("MIA.Log.Debug", false));
        MenuLogCheckbox menuLogCheckbox = new MenuLogCheckbox(level, renderer.isWriteEnabled(level));
        logMenu.add(menuLogCheckbox);

        level = Level.MEMORY;
        renderer.setWriteEnabled(level, Prefs.get("MIA.Log.Memory", false));
        menuLogCheckbox = new MenuLogCheckbox(level, renderer.isWriteEnabled(level));
        logMenu.add(menuLogCheckbox);

        level = Level.MESSAGE;
        renderer.setWriteEnabled(level, Prefs.get("MIA.Log.Message", true));
        menuLogCheckbox = new MenuLogCheckbox(level, renderer.isWriteEnabled(level));
        logMenu.add(menuLogCheckbox);

        level = Level.STATUS;
        renderer.setWriteEnabled(level, Prefs.get("MIA.Log.Status", false));
        menuLogCheckbox = new MenuLogCheckbox(level, renderer.isWriteEnabled(level));
        logMenu.add(menuLogCheckbox);

        level = Level.WARNING;
        renderer.setWriteEnabled(level, Prefs.get("MIA.Log.Warning", true));
        menuLogCheckbox = new MenuLogCheckbox(level, renderer.isWriteEnabled(level));
        logMenu.add(menuLogCheckbox);

        add(Box.createHorizontalGlue());
        
        add(blankMenu);
        blankMenu.add(new MenuItem(MenuItem.SHOW_PONY));

        KeyStroke saveModules = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        registerKeyboardAction(this, "Save", saveModules, JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke newAnalysis = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        registerKeyboardAction(this, "New", newAnalysis, JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke undoAction = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        registerKeyboardAction(this, "Undo", undoAction, JComponent.WHEN_IN_FOCUSED_WINDOW);

        KeyStroke redoAction = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        registerKeyboardAction(this, "Redo", redoAction, JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    public void update() {
        newWorkflow.setVisible(!GUI.isProcessingGUI());

        editMenu.setVisible(!GUI.isProcessingGUI());
        blankMenu.setVisible(!GUI.isProcessingGUI());

        resetAnalysis.setVisible(!GUI.isProcessingGUI());
        enableAllModules.setVisible(!GUI.isProcessingGUI());
        disableAllModules.setVisible(!GUI.isProcessingGUI());
        outputAllModules.setVisible(!GUI.isProcessingGUI());
        silenceAllModules.setVisible(!GUI.isProcessingGUI());

    }

    public void setUndoRedoStatus(UndoRedoStore undoRedoStatus) {
        undo.setEnabled(undoRedoStatus.getUndoSize() != 0);
        redo.setEnabled(undoRedoStatus.getRedoSize() != 0);
    }

    public void setShowSidebar(boolean showSidebar) {
        sidebarMenuCheckbox.setSelected(showSidebar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "New":
                GUIAnalysisHandler.newAnalysis();
                break;
            case "Save":
                GUIAnalysisHandler.saveModules();
                break;
            case "Undo":
                GUI.undo();
                break;
            case "Redo":
                GUI.redo();
                break;
        }
    }
}
