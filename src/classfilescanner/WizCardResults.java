/*
 * WizCardResults.java
 *
 * Created on 13. kesäkuuta 2007, 10:04
 */
package classfilescanner;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.SwingWorker;
import javax.swing.event.*;
import javax.swing.tree.*;

import classfile.ClassFile;
import classfile.Reference;
import java.util.Set;
import java.util.logging.Logger;
import wizard.*;

/**
 *
 * @author  z705692
 */
public class WizCardResults extends javax.swing.JPanel implements WizardCard {
    private static final Logger logger = Logger.getLogger(WizCardResults.class.getPackage().getName());
    private Wizard<Property> wizard;
    private DefaultTreeModel resultTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Results"));
    private SwingWorker backgroundScanner;
    private ScanPropertyListener scanPropListener = new ScanPropertyListener();
    private RootFilePropertyListener rootFilePropListener = new RootFilePropertyListener();

    public WizCardResults(Wizard<Property> wiz) {
        initComponents();
        resultTreeModel.addTreeModelListener(new ResultModelListener());
        TreeSelectionModel selModel = lstFoundReferences.getSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        selModel.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath tp = e.getNewLeadSelectionPath();
                cmdOpen.setEnabled(tp != null && ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject() instanceof classfile.ClassFile);
                getRootPane().setDefaultButton(cmdOpen);
            }
        });
        wizard = wiz;
        wizard.addPropertyChangeListener(Property.ROOT_FILE, rootFilePropListener);
    }

    @Override
    public void init() {
        logger.fine("WizCardResults inited");
    }

    @Override
    public void activate() {
        logger.fine("WizCardResults activated");
        getRootPane().setDefaultButton(null);
        ((DefaultMutableTreeNode) resultTreeModel.getRoot()).removeAllChildren();
        resultTreeModel.reload();
        lstFoundReferences.requestFocusInWindow();

        backgroundScanner = createScanner();
        backgroundScanner.execute();
    }

    @Override
    public void passivate() {
        logger.fine("WizCardResults passivated");
        backgroundScanner.cancel(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblScanCount = new javax.swing.JLabel();
        fldScanCount = new javax.swing.JLabel();
        lblFiles = new javax.swing.JLabel();
        lblRefCount1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstFoundReferences = new javax.swing.JTree();
        cmdPrev = new javax.swing.JButton();
        cmdNewScan = new javax.swing.JButton();
        fldResultsRoot = new javax.swing.JLabel();
        fldRefCount = new javax.swing.JLabel();
        lblRefCount2 = new javax.swing.JLabel();
        cmdOpen = new javax.swing.JButton();
        cmdReport = new javax.swing.JButton();

        setName("RESULTS"); // NOI18N

        jLabel1.setText("<html><font style=\"font-size: 14pt; color:maroon; font-weight: bold\">\n4. Results</font>\n</html>\n");

        lblScanCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblScanCount.setText("Scanned");
        lblScanCount.setFocusable(false);

        fldScanCount.setText("--------");
        fldScanCount.setFocusable(false);
        fldScanCount.setMaximumSize(new java.awt.Dimension(40, 14));
        fldScanCount.setMinimumSize(new java.awt.Dimension(40, 14));
        fldScanCount.setPreferredSize(new java.awt.Dimension(40, 14));

        lblFiles.setText("files from");
        lblFiles.setFocusable(false);

        lblRefCount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRefCount1.setText("Found");
        lblRefCount1.setFocusable(false);
        lblRefCount1.setMaximumSize(new java.awt.Dimension(41, 14));
        lblRefCount1.setMinimumSize(new java.awt.Dimension(41, 14));
        lblRefCount1.setPreferredSize(new java.awt.Dimension(41, 14));

        lstFoundReferences.setModel(resultTreeModel);
        lstFoundReferences.setName("focus"); // NOI18N
        lstFoundReferences.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstFoundReferencesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(lstFoundReferences);

        cmdPrev.setMnemonic('p');
        cmdPrev.setText("<< Previous");
        cmdPrev.setMaximumSize(new java.awt.Dimension(98, 23));
        cmdPrev.setMinimumSize(new java.awt.Dimension(98, 23));
        cmdPrev.setPreferredSize(new java.awt.Dimension(98, 23));
        cmdPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrevActionPerformed(evt);
            }
        });

        cmdNewScan.setMnemonic('s');
        cmdNewScan.setText("New Scan");
        cmdNewScan.setMaximumSize(new java.awt.Dimension(98, 23));
        cmdNewScan.setMinimumSize(new java.awt.Dimension(98, 23));
        cmdNewScan.setPreferredSize(new java.awt.Dimension(98, 23));
        cmdNewScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewScanActionPerformed(evt);
            }
        });

        fldResultsRoot.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        fldResultsRoot.setText("-----");

        fldRefCount.setText("--------");
        fldRefCount.setMaximumSize(new java.awt.Dimension(40, 14));
        fldRefCount.setMinimumSize(new java.awt.Dimension(40, 14));
        fldRefCount.setPreferredSize(new java.awt.Dimension(40, 14));

        lblRefCount2.setText("classes containing references");

        cmdOpen.setText("Open...");
        cmdOpen.setEnabled(false);
        cmdOpen.setMaximumSize(new java.awt.Dimension(98, 23));
        cmdOpen.setMinimumSize(new java.awt.Dimension(98, 23));
        cmdOpen.setName("default"); // NOI18N
        cmdOpen.setPreferredSize(new java.awt.Dimension(98, 23));
        cmdOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOpenActionPerformed(evt);
            }
        });

        cmdReport.setText("Report...");
        cmdReport.setEnabled(false);
        cmdReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 506, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cmdOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdReport)
                        .addGap(18, 18, 18)
                        .addComponent(cmdPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdNewScan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblScanCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRefCount1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fldRefCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fldScanCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFiles)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fldResultsRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                            .addComponent(lblRefCount2))))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblScanCount)
                    .addComponent(fldScanCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFiles)
                    .addComponent(fldResultsRoot))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRefCount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldRefCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRefCount2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdNewScan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdReport))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void cmdOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOpenActionPerformed
    openDump();
}//GEN-LAST:event_cmdOpenActionPerformed

private void cmdNewScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewScanActionPerformed
//    saveAsMenuItem.setEnabled(false);
    wizard.reset();//nextPhase();
}//GEN-LAST:event_cmdNewScanActionPerformed

private void cmdPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrevActionPerformed
//    saveAsMenuItem.setEnabled(false);
    wizard.previousPhase();
}//GEN-LAST:event_cmdPrevActionPerformed

private void lstFoundReferencesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstFoundReferencesMouseClicked
    if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
        TreePath tp = lstFoundReferences.getClosestPathForLocation(evt.getX(), evt.getY());
        if (tp != null && ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject() instanceof classfile.ClassFile) {
            evt.consume();
            openDump();
        }
    }
}//GEN-LAST:event_lstFoundReferencesMouseClicked

    private void cmdReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReportActionPerformed
        try {
            // TODO add your handling code here:
            FrmReport report = new FrmReport(null, true, resultTreeModel, (Set<Reference>)backgroundScanner.get());
            report.setLocationRelativeTo(this);
            report.setVisible(true);
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Failed to get scan results", ex);
        } catch (ExecutionException ex) {
            logger.log(Level.SEVERE, "Failed to get scan results", ex);
        }
    }//GEN-LAST:event_cmdReportActionPerformed

    private BackgroundScanner createScanner() {
        BackgroundScanner ret = new BackgroundScanner(
                (File) wizard.getProperty(Property.ROOT_FILE),
                (Collection<Reference>) wizard.getProperty(Property.REFERENCES),
                (Collection<Reference>) wizard.getProperty(Property.EXCLUDES),
                resultTreeModel);
        ret.addPropertyChangeListener(scanPropListener);
        return ret;
    }

    private void openDump() {
        TreePath tp = lstFoundReferences.getSelectionPath();
        if (tp != null) {
            Object o = ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject();
            if (o instanceof ClassFile) {
                FrmDump dump = new FrmDump(null, true, (ClassFile) o);
                dump.setLocationRelativeTo(this);
                dump.setVisible(true);
            }
        }
    }

    private class RootFilePropertyListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            File root = (File) evt.getNewValue();
            String r = root != null ? root.getAbsolutePath() : "";
            fldResultsRoot.setText(r);
        }
    }

    private class ResultModelListener implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            TreeModel model = (TreeModel) e.getSource();
            lstFoundReferences.expandPath(e.getTreePath());
            fldRefCount.setText(String.valueOf(model.getChildCount(model.getRoot())));
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            TreeModel model = (TreeModel) e.getSource();
            fldRefCount.setText(String.valueOf(model.getChildCount(model.getRoot())));
        }
    }

    private class ScanPropertyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("nbrofScannedClasses".equals(evt.getPropertyName())) {
                fldScanCount.setText(String.valueOf(evt.getNewValue()));
            } else if ("state".equals(evt.getPropertyName())) {
                boolean done = SwingWorker.StateValue.DONE.equals(evt.getNewValue());
                fldScanCount.setForeground(done ? Color.BLACK : Color.RED);
                cmdReport.setEnabled(done);
                //                saveAsMenuItem.setEnabled(true);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdNewScan;
    private javax.swing.JButton cmdOpen;
    private javax.swing.JButton cmdPrev;
    private javax.swing.JButton cmdReport;
    private javax.swing.JLabel fldRefCount;
    private javax.swing.JLabel fldResultsRoot;
    private javax.swing.JLabel fldScanCount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblFiles;
    private javax.swing.JLabel lblRefCount1;
    private javax.swing.JLabel lblRefCount2;
    private javax.swing.JLabel lblScanCount;
    private javax.swing.JTree lstFoundReferences;
    // End of variables declaration//GEN-END:variables
}
