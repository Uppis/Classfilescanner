package com.vajasoft.classfilescanner;

import java.io.*;
import com.vajasoft.classfile.Reference;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author z705692
 */
public class FrmReport extends javax.swing.JDialog {

    public FrmReport(java.awt.Frame parent, boolean modal, DefaultTreeModel result, Set<Reference> refs) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(cmdClose);
        EscapeAction.setEscAction(this);
        setClasses(result);
        setReferences(refs);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        fldClasses = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        fldReferences = new javax.swing.JTextArea();
        cmdClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fldClasses.setEditable(false);
        fldClasses.setColumns(20);
        fldClasses.setRows(5);
        jScrollPane1.setViewportView(fldClasses);

        jTabbedPane1.addTab("Classes", jScrollPane1);

        fldReferences.setEditable(false);
        fldReferences.setColumns(20);
        fldReferences.setRows(5);
        jScrollPane2.setViewportView(fldReferences);

        jTabbedPane1.addTab("References", jScrollPane2);

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
                .addContainerGap(546, Short.MAX_VALUE)
                .addComponent(cmdClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(442, Short.MAX_VALUE)
                .addComponent(cmdClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(43, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setClasses(DefaultTreeModel result) {
        setTitle("Results Report");
        CharArrayWriter buf = new CharArrayWriter(8192);
        PrintWriter out = new PrintWriter(buf);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)result.getRoot();
        if (root != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getFirstChild();
            while (node != null) {
                out.println(node);
                node = node.getNextSibling();
            }
            fldClasses.setText(buf.toString());
            fldClasses.setCaretPosition(0);
        }
    }

    private void setReferences(Set<Reference> refs) {
        setTitle("Results Report");
        CharArrayWriter buf = new CharArrayWriter(8192);
        PrintWriter out = new PrintWriter(buf);
        for (Reference ref : refs) {
            out.println(ref);
        }
        fldReferences.setText(buf.toString());
        fldReferences.setCaretPosition(0);
    }

    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        dispose();
    }//GEN-LAST:event_cmdCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdClose;
    private javax.swing.JTextArea fldClasses;
    private javax.swing.JTextArea fldReferences;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}