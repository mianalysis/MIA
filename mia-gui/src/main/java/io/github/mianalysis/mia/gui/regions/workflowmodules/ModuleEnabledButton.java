package io.github.mianalysis.mia.gui.regions.workflowmodules;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.gui.GUI;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.module.system.GUISeparator;

/**
 * Created by sc13967 on 07/06/2017.
 */
public class ModuleEnabledButton extends JButton implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 6135822183769524507L;
    private Module module;
    private static final ImageIcon blackIcon = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_black_strike_12px.png"), "");
    private static final ImageIcon blackIconDM = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_blackDM_strike_12px.png"), "");
    private static final ImageIcon redIcon = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_red_12px.png"), "");
    private static final ImageIcon redIconDM = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_redDM_12px.png"), "");
    private static final ImageIcon orangeIcon = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_orange_12px.png"), "");
    private static final ImageIcon orangeIconDM = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_orangeDM_12px.png"), "");
    private static final ImageIcon greenIcon = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_green_12px.png"), "");
    private static final ImageIcon greenIconDM = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_greenDM_12px.png"), "");
    private static final ImageIcon darkBlueIcon = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_darkblue_12px.png"), "");
    private static final ImageIcon darkBlueIconDM = new ImageIcon(
            ModuleEnabledButton.class.getResource("/icons/power_darkblueDM_12px.png"), "");

    public ModuleEnabledButton(Module module) {
        this.module = module;

        setFocusPainted(false);
        setSelected(false);
        setMargin(new Insets(0, 0, 0, 0));
        setName("ModuleEnabled");
        setToolTipText("Enable/disable module");
        updateState();

        addActionListener(this);

    }

    public void updateState() {
        if (module instanceof GUISeparator && module.isEnabled()) {
            if (MIA.getPreferences().darkThemeEnabled())
                setIcon(darkBlueIconDM);
            else
                setIcon(darkBlueIcon);
        } else if (module.isEnabled() && module.isReachable() && module.isRunnable()) {
            if (MIA.getPreferences().darkThemeEnabled())
                setIcon(greenIconDM);
            else
                setIcon(greenIcon);
        } else if (module.isEnabled() & !module.isReachable()) {
            if (MIA.getPreferences().darkThemeEnabled())
                setIcon(orangeIconDM);
            else
                setIcon(orangeIcon);
        } else if (module.isEnabled() & !module.isRunnable()) {
            if (MIA.getPreferences().darkThemeEnabled())
                setIcon(redIconDM);
            else
                setIcon(redIcon);
        } else {
            if (MIA.getPreferences().darkThemeEnabled())
                setIcon(blackIconDM);
            else
                setIcon(blackIcon);
        }
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Invert state
        module.setEnabled(!module.isEnabled());

        updateState();

        int idx = GUI.getModules().indexOf(module);
        if (idx <= GUI.getLastModuleEval())
            GUI.setLastModuleEval(idx - 1);

        // If this is a GUISeparator module, disable all modules after it, until the
        // next separator
        Modules modules = GUI.getModules();
        if (module.getClass().isInstance(new GUISeparator(modules))) {
            for (int i = idx + 1; i < modules.size(); i++) {
                Module currentModule = modules.get(i);
                if (currentModule.getClass().isInstance(new GUISeparator(modules))) {
                    break;
                } else {
                    currentModule.setEnabled(module.isEnabled());
                }
            }
        }

        GUI.updateModules();
        GUI.updateParameters();

    }
}
