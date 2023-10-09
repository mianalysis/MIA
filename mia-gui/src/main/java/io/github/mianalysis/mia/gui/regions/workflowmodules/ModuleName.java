package io.github.mianalysis.mia.gui.regions.workflowmodules;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.system.GUISeparator;
import io.github.mianalysis.mia.object.system.Colours;
import io.github.mianalysis.mia.object.system.Preferences;

public class ModuleName extends JLabel {
    private Module module;
    private JTable table;
    private boolean isSelected;
    private Color defaultColour;

    private static final ImageIcon skipIcon = new ImageIcon(
            ModuleName.class.getResource("/icons/skiparrow_orange_12px.png"), "");
    private static final ImageIcon skipIconDM = new ImageIcon(
            ModuleName.class.getResource("/icons/skiparrow_orangeDM_12px.png"), "");
    private static final ImageIcon warningIcon = new ImageIcon(
            ModuleName.class.getResource("/icons/warning_red_12px.png"), "");
    private static final ImageIcon warningIconDM = new ImageIcon(
            ModuleName.class.getResource("/icons/warning_redDM_12px.png"), "");

    public ModuleName(Module module, JTable table, boolean isSelected) {
        this.module = module;
        this.table = table;
        this.isSelected = isSelected;

        Preferences preferences = MIA.getPreferences();
        boolean darkMode = preferences == null ? false : preferences.darkThemeEnabled();

        setBorder(new EmptyBorder(2, 5, 0, 0));
        setOpaque(true);
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        if (module.isDeprecated()) {
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            font = new Font(attributes);
        }

        defaultColour = getForeground();

        setFont(font);
        setText(module.getNickname());
        updateState();

        if (isSelected)
            setBackground(Colours.getLightBlue(darkMode));
        else
            setBackground(table.getBackground());

    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        Preferences preferences = MIA.getPreferences();
        boolean darkMode = preferences == null ? false : preferences.darkThemeEnabled();

        if (isSelected)
            setBackground(Colours.getLightBlue(darkMode));
        else
            setBackground(table.getBackground());
    }

    public void updateState() {
        setIcon(null);

        Preferences preferences = MIA.getPreferences();
        boolean darkMode = preferences == null ? false : preferences.darkThemeEnabled();

        if (isSelected)
            setBackground(Colours.getLightBlue(darkMode));
        else
            setBackground(table.getBackground());

        String deprecationMessage = "";
        if (module.isDeprecated())
            deprecationMessage = " (deprecated)";

        if (module instanceof GUISeparator) {
            setForeground(Colours.getDarkBlue(darkMode));
            setToolTipText("Module separator");
        } else if (module.isEnabled() && module.isReachable() && module.isRunnable()) {
            setForeground(defaultColour);
            setToolTipText("<html>Module: " + module.getName() + "<br>Nickname: " + module.getNickname()
                    + "<br>Status: OK" + deprecationMessage + "</html>");
        } else if (module.isEnabled() & !module.isReachable()) {
            setForeground(Colours.getOrange(darkMode));
            if (MIA.getPreferences().darkThemeEnabled())
                setIcon(skipIconDM);
            else
                setIcon(skipIcon);
            setToolTipText("<html>Module: " + module.getName() + "<br>Nickname: " + module.getNickname()
                    + "<br>Status: Skipped" + deprecationMessage + "</html>");
        } else if (module.isEnabled() & !module.isRunnable()) {
            setForeground(Colours.getRed(darkMode));
            if (darkMode)
                setIcon(warningIconDM);
            else
                setIcon(warningIcon);
            setToolTipText("<html>Module: " + module.getName() + "<br>Nickname: " + module.getNickname()
                    + "<br>Status: Error" + deprecationMessage + "</html>");
        } else {
            setForeground(Color.GRAY);
            setToolTipText("<html>Module: " + module.getName() + "<br>Nickname: " + module.getNickname()
                    + "<br>Status: Disabled" + deprecationMessage + "</html>");
        }

        if (module.isDeprecated()) {
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            font = new Font(attributes);
        }

        revalidate();
        repaint();

    }
}
