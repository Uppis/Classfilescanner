package com.vajasoft.classfilescanner;

import java.awt.event.*;
import javax.swing.*;

public class EscapeAction extends AbstractAction {
    private JDialog dialog;

    public static void setEscAction(JDialog dlg) {
        Action escapeAction = new EscapeAction(dlg);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        dlg.getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e ActionEvent
     * @todo Implement this java.awt.event.ActionListener method
     */
    public void actionPerformed(ActionEvent e) {
        dialog.dispose();
    }

    private EscapeAction(JDialog dlg) {
        dialog = dlg;
    }
}
