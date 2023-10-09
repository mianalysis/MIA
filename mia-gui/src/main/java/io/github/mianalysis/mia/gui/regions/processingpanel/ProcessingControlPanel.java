package io.github.mianalysis.mia.gui.regions.processingpanel;

import io.github.mianalysis.mia.gui.ComponentFactory;
import io.github.mianalysis.mia.gui.GUI;
import io.github.mianalysis.mia.gui.regions.workflowmodules.ModuleEnabledButton;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.module.core.InputControl;
import io.github.mianalysis.mia.module.core.OutputControl;
import io.github.mianalysis.mia.module.system.GUISeparator;
import io.github.mianalysis.mia.object.parameters.BooleanP;
import io.github.mianalysis.mia.process.analysishandling.Analysis;
import io.github.mianalysis.mia.process.analysishandling.AnalysisTester;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class ProcessingControlPanel extends JScrollPane {
    /**
     *
     */
    private static final long serialVersionUID = 7620575044877199413L;
    private static GUISeparator loadSeparator;
    private JPanel panel;

    private static final int minimumWidth = 400;
    private static final int preferredWidth = 400;

    public ProcessingControlPanel() {
        panel = new JPanel();
        setViewportView(panel);

        loadSeparator = new GUISeparator(GUI.getModules());

        // Initialising the scroll panel
        setMinimumSize(new Dimension(minimumWidth, 1));
        setPreferredSize(new Dimension(preferredWidth, 1));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        setViewportBorder(BorderFactory.createEmptyBorder());
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(10);

        // Initialising the panel for module buttons
        panel.setLayout(new GridBagLayout());
        panel.validate();
        panel.repaint();

        validate();
        repaint();

        loadSeparator.setNickname("File selection");

    }

    public void updatePanel() {
        InputControl inputControl = GUI.getModules().getInputControl();
        OutputControl outputControl = GUI.getModules().getOutputControl();

        AnalysisTester.testModule(inputControl, GUI.getModules());
        AnalysisTester.testModule(outputControl, GUI.getModules());
        AnalysisTester.testModules(GUI.getModules(),GUI.getTestWorkspace());

        Analysis analysis = GUI.getAnalysis();
        ComponentFactory componentFactory = GUI.getComponentFactory();

        panel.removeAll();

        // Check if there are no controls to be displayed
        if (!analysis.hasVisibleParameters()) {
            showUsageMessage();
            return;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Adding a separator between the input and main modules
        c.insets = new Insets(5, 0, 0, 5);
        panel.add(componentFactory.createProcessingSeparator(loadSeparator), c);
        c.insets = new Insets(0, 0, 0, 5);

        // Only modules below an expanded GUISeparator should be displayed
        BooleanP expanded = (loadSeparator.getParameter(GUISeparator.EXPANDED_PROCESSING));

        // Adding input control options
        if (expanded.isSelected()) {
            c.gridy++;
            JPanel inputPanel = componentFactory.createProcessingModuleControl(inputControl);
            if (inputPanel != null)
                panel.add(inputPanel, c);
        }

        // Adding module buttons
        GUISeparator separator = loadSeparator;
        Modules modules = analysis.getModules();
        for (Module module : modules) {
            // If the module is the special-case GUISeparator, create this module, then
            // return
            JPanel modulePanel = null;
            if (module instanceof GUISeparator) {
                separator = (GUISeparator) module;

                // If not runnable, don't show this separator (e.g. if WorkflowHandling is
                // skipping this separator)
                if (!separator.isRunnable())
                    continue;

                // Not all GUI separators are shown on the processing view panel
                BooleanP showProcessing = module.getParameter(GUISeparator.SHOW_PROCESSING);
                if (!showProcessing.isSelected())
                    continue;

                // If this separator doesn't control any visible modules, skip it
                if (((GUISeparator) module).getProcessingViewModules().size() == 0 & !module.canBeDisabled())
                    continue;

                expanded = module.getParameter(GUISeparator.EXPANDED_PROCESSING);
                modulePanel = componentFactory.createProcessingSeparator(module);

            } else {
                if (separator.isEnabled() && module.isRunnable() || module.invalidParameterIsVisible()) {
                    modulePanel = componentFactory.createProcessingModuleControl(module);
                }
            }

            if (modulePanel != null && (expanded.isSelected())) {
                c.gridy++;
                panel.add(modulePanel, c);
            }

            if (module instanceof GUISeparator) {
                c.gridy++;
                panel.add(modulePanel, c);
            }
        }

        JPanel outputPanel = componentFactory.createProcessingModuleControl(outputControl);
        if (outputPanel != null && expanded.isSelected()) {
            c.gridy++;
            panel.add(outputPanel, c);
        }

        c.gridy++;
        c.weighty = 1;
        c.insets = new Insets(20, 0, 0, 0);
        c.fill = GridBagConstraints.VERTICAL;
        JSeparator jSeparator = new JSeparator();
        jSeparator.setPreferredSize(new Dimension(-1, 1));
        panel.add(jSeparator, c);

        panel.revalidate();
        panel.repaint();

        revalidate();
        repaint();

    }

    public void showUsageMessage() {
        panel.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;

        // Adding title to help window
        JTextPane usageMessage = new JTextPane();
        usageMessage.setContentType("text/html");
        usageMessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        usageMessage.setText("<html><center><font face=\"sans-serif\" size=\"3\">"
                + "To load an existing workflow,<br>click \"Load\" and select a .mia file." + "<br><br>"
                + "To start creating a new workflow,<br>go to View > Switch to editing view."
                + "</font></center></html>");
        usageMessage.setEditable(false);
        usageMessage.setBackground(null);
        usageMessage.setOpaque(false);
        panel.add(usageMessage);

        panel.revalidate();
        panel.repaint();

    }

    public static int getMinimumWidth() {
        return minimumWidth;
    }

    public static int getPreferredWidth() {
        return preferredWidth;
    }

    public void updateButtonStates() {
        for (Component component : panel.getComponents()) {
            if (component.getClass() == ModuleEnabledButton.class)
                ((ModuleEnabledButton) component).updateState();
        }
    }
}
