package classfilescanner;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.text.JTextComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// End of variables declaration                   

class StateGuard implements DocumentListener, KeyListener {
    private Component guardee;

    public StateGuard(Component c) {
        guardee = c;
    }
    
    public void changedUpdate(DocumentEvent e) {
        setCmdState(e);
    }

    public void insertUpdate(DocumentEvent e) {
        setCmdState(e);
    }

    public void removeUpdate(DocumentEvent e) {
        setCmdState(e);
    }

    public void keyTyped(KeyEvent e) {
        setCmdState(e);
    }

    public void keyPressed(KeyEvent e) {
        setCmdState(e);
    }

    public void keyReleased(KeyEvent e) {
        setCmdState(e);
    }

    private void setCmdState(DocumentEvent e) {
        boolean cond = e.getDocument().getLength() > 0;
        guardee.setEnabled(cond);
    }

    private void setCmdState(KeyEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            String content = ((JTextComponent)src).getText();
            boolean cond = content.length() > 0;
            guardee.setEnabled(cond);
        }
    }
}