package com.vajasoft.classfilescanner;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author Z705692
 */
public class DelegatingTransferHandler extends TransferHandler {
        private TransferHandler delegate;

        public DelegatingTransferHandler(TransferHandler delegate) {
            if (delegate == null)
                throw new IllegalArgumentException("Delegate can't be null");
            this.delegate = delegate;
        }

    public boolean importData(JComponent comp, Transferable t) {
        return delegate.importData(comp, t);
    }

    public boolean importData(TransferSupport support) {
        return delegate.importData(support);
    }

    public Icon getVisualRepresentation(Transferable t) {
        return delegate.getVisualRepresentation(t);
    }

    public int getSourceActions(JComponent c) {
        return delegate.getSourceActions(c);
    }

    public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
        delegate.exportToClipboard(comp, clip, action);
    }

    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        delegate.exportAsDrag(comp, e, action);
    }

    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        return delegate.canImport(comp, transferFlavors);
    }

    public boolean canImport(TransferSupport support) {
        return delegate.canImport(support);
    }
}
