/*
 * FrmDump.java
 *
 * Created on 29. toukokuuta 2007, 9:57
 */

package classfilescanner;

import java.io.*;
import classfile.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

/**
 *
 * @author  z705692
 */
public class FrmDump extends javax.swing.JDialog {
    
    public FrmDump(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(cmdClose);
        EscapeAction.setEscAction(this);
    }
    
    public FrmDump(java.awt.Frame parent, boolean modal, ClassFile cf) {
        this(parent, modal);
        setContents(cf);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        fldClassDump = new javax.swing.JTextArea();
        cmdClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fldClassDump.setColumns(20);
        fldClassDump.setEditable(false);
        fldClassDump.setRows(5);
        jScrollPane1.setViewportView(fldClassDump);

        cmdClose.setText("Close");
        cmdClose.setMaximumSize(new java.awt.Dimension(98, 23));
        cmdClose.setMinimumSize(new java.awt.Dimension(98, 23));
        cmdClose.setPreferredSize(new java.awt.Dimension(98, 23));
        cmdClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addComponent(cmdClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public final void setContents(ClassFile cf) {
        CharArrayWriter buf = new CharArrayWriter(8192);
        PrintWriter out = new PrintWriter(buf);
        cf.dump(out);
        out.println("\nReferences:");
        for (Reference ref : cf.getReferences2()) {
            out.println(ref);
        }
        fldClassDump.setText(buf.toString());
        fldClassDump.setCaretPosition(0);
        setTitle(cf.getClassFileName());
    }

    public void supportDnD() {
        setTransferHandler(new DialogTransferHandler());
        fldClassDump.setTransferHandler(new ComponentTransferHandler(fldClassDump.getTransferHandler()));
    }

    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        dispose();
    }//GEN-LAST:event_cmdCloseActionPerformed
    
    public static void main(String args[]) throws IOException, InvalidClassFileException {
        if (args.length == 0) {
            System.out.println("Usage: java classfilescanner.FrmDump <classfile>");
        }
        final FrmDump frm = args.length == 0 ? new FrmDump(null, true) : new FrmDump(null, true, new ClassFile(new java.io.FileInputStream(args[0])));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frm.supportDnD();
                frm.setVisible(true);
            }
        });
    }

    private class DialogTransferHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            boolean ret = false;
            if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                ret = (COPY & support.getSourceDropActions()) == COPY;  // copySupported
                if (ret) {
                    support.setDropAction(COPY);
                }
            }
            return ret;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean ret = false;
            if (canImport(support)) {
                Transferable t = support.getTransferable();
                try {
                    java.util.List<File> l = (java.util.List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File f : l) {
                        setContents(new ClassFile(new java.io.FileInputStream(f)));
                        break;  // Only 1 file supported so far
                    }
                    ret = true;
                } catch (UnsupportedFlavorException e) {
                    ret = false;
                } catch (IOException e) {
                    ret = false;
                } catch (InvalidClassFileException ex) {
                    ret = false;
                }
            }
            return ret;
        }
    };

    private class ComponentTransferHandler extends DelegatingTransferHandler {

        public ComponentTransferHandler(TransferHandler delegate) {
            super(delegate);
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            boolean ret = meCanImport(support);
            if (ret == false) {
                return super.canImport(support);
            }
            return ret;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean ret;
            if (meCanImport(support)) {
                Transferable t = support.getTransferable();
                try {
                    java.util.List<File> l = (java.util.List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File f : l) {
                        setContents(new ClassFile(new java.io.FileInputStream(f)));
                        break;  // Only 1 file supported so far
                    }
                    ret = true;
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                } catch (InvalidClassFileException ex) {
                    return false;
                }
            } else {
                ret = super.importData(support);
            }
            return ret;
        }

        private  boolean meCanImport(TransferHandler.TransferSupport support) {
            boolean ret = false;
            if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;
                if (copySupported) {
                    support.setDropAction(COPY);
                    ret = true;
                }
            }
            return ret;
        }
    };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdClose;
    private javax.swing.JTextArea fldClassDump;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}
