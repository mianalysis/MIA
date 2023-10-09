package io.github.mianalysis.mia.gui.regions.parameterlist;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.gui.GUI;
import io.github.mianalysis.mia.object.refs.abstrakt.ExportableRef;
import io.github.mianalysis.mia.object.refs.abstrakt.SummaryRef;
import io.github.mianalysis.mia.object.refs.collections.Refs;

public class EnableRefsButton extends JButton implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = -1146487404468628869L;

    private static final ImageIcon icon = new ImageIcon(
            EnableRefsButton.class.getResource("/icons/check-mark_black_12px.png"), "");
    private static final ImageIcon iconDM = new ImageIcon(
            EnableRefsButton.class.getResource("/icons/check-mark_blackDM_12px.png"), "");

    private Refs<SummaryRef> refs;

    // CONSTRUCTOR

    public EnableRefsButton(Refs<SummaryRef> refs) {
        this.refs = refs;

        setMargin(new Insets(0, 0, 0, 0));
        setFocusPainted(false);
        setSelected(false);
        setName("EnableAllMeasurements");
        setToolTipText("Enable all measurements");
        addActionListener(this);
        if (MIA.getPreferences().darkThemeEnabled())
            setIcon(iconDM);
        else
            setIcon(icon);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI.addUndo();

        for (ExportableRef ref : refs.values())
            ref.setExportGlobal(true);

        GUI.updateParameters();

    }
}
