package com.vajasoft.classfilescanner;

import com.vajasoft.classfile.Reference;
import com.vajasoft.wizard.WizardCard;
import com.vajasoft.wizard.WizardPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author z705692
 */
public class FrmClassFileScanner extends JFrame {
    private static String PREF_NODE_RECENT_ROOTS = "recentroots";
    private static String PREF_NODE_RECENT_CLASSES = "recentclasses";
    private static final Logger logger = Logger.getLogger(FrmClassFileScanner.class.getPackage().getName());
    private MutableComboBoxModel recentRoots;
    private MutableComboBoxModel recentClasses;
    private Preferences userPrefs;
    private int maxNbrofRecentItems = 20;

    /**
     * Creates new form FrmClassFileScanner
     */
    public FrmClassFileScanner() {
        initComponents();
        initLAFMenu();
        RecentItem[] rcntRoots = new RecentItem[0];
        RecentItem[] rcntClasses = new RecentItem[0];
        try {
            userPrefs = Preferences.userNodeForPackage(FrmClassFileScanner.class);
            rcntRoots = Util.loadRecentItems(userPrefs.node(PREF_NODE_RECENT_ROOTS));
            rcntClasses = Util.loadRecentItems(userPrefs.node(PREF_NODE_RECENT_CLASSES));
        } catch (BackingStoreException ex) {
            logger.log(Level.SEVERE, "Unable to load recent items !", ex);
        }
        recentRoots = new DefaultComboBoxModel(rcntRoots);
        recentClasses = new DefaultComboBoxModel(rcntClasses);
        WizardPanel<Property> wizard = new WizardPanel<Property>();
        WizardCard wcRootFile = new WizCardRootFile(wizard, recentRoots);
        wizard.add(wcRootFile);
        WizardCard wcClassFile = new WizCardClassFile(wizard, recentClasses);
        wizard.add(wcClassFile);
        WizardCard wcArchive = new WizCardArchive(wizard);
        wizard.add(wcArchive);
        WizardCard wcMembers = new WizCardMembers(wizard);
        wizard.add(wcMembers);
        WizardCard wcSummary = new WizCardSummary(wizard);
        wizard.add(wcSummary);
        WizardCard wcResults = new WizCardResults(wizard);
        wizard.add(wcResults);

        wizard.addRoute(wcClassFile, Property.REFERENCES, wcSummary);
        wizard.addRoute(wcClassFile, Property.CLASS_FILE, wcMembers);

        getContentPane().add(wizard, java.awt.BorderLayout.CENTER);
        pack();

        wizard.nextPhase(); // => first card in the container
        logger.fine("Application initialized.");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content
     * of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        resultFileChooser = new javax.swing.JFileChooser();
        fldStatus = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        lafMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        resultFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Class File Scanner");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        fldStatus.setText(" ");
        fldStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fldStatus.setFocusable(false);
        fldStatus.setMinimumSize(new java.awt.Dimension(0, 19));
        getContentPane().add(fldStatus, java.awt.BorderLayout.SOUTH);

        menuBar.setFocusable(false);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setEnabled(false);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        copyMenuItem.setText("Copy");
        copyMenuItem.setEnabled(false);
        editMenu.add(copyMenuItem);

        menuBar.add(editMenu);

        viewMenu.setMnemonic('v');
        viewMenu.setText("View");

        lafMenu.setMnemonic('l');
        lafMenu.setText("L&F");
        viewMenu.add(lafMenu);

        menuBar.add(viewMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    try {
        Util.saveRecentItems(recentRoots, userPrefs.node(PREF_NODE_RECENT_ROOTS), maxNbrofRecentItems);
        Util.saveRecentItems(recentClasses, userPrefs.node(PREF_NODE_RECENT_CLASSES), maxNbrofRecentItems);
    } catch (BackingStoreException ex) {
        logger.log(Level.SEVERE, "Unable to save recent items", ex);
    }
}//GEN-LAST:event_formWindowClosed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    public static void main(String args[]) throws InterruptedException {
        logger.fine("Application started.");
        if (args.length == 0) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    JFrame frm = new FrmClassFileScanner();
                    frm.setLocationRelativeTo(null);
                    frm.setVisible(true);
                }
            });
        } else if (args.length > 1) {
            File target = new File(args[0]);
            Collection<Reference> refs = new ArrayList<Reference>();
            com.vajasoft.filetree.Util.loadReferences2(args[1], refs);
            Collection<Reference> excls = new ArrayList<Reference>();
            if (args.length > 2) {
                com.vajasoft.filetree.Util.loadReferences2(args[2], excls);
            }
            BackgroundScanner scanner = new BackgroundScanner(target, refs, excls, null);
            scanner.addPropertyChangeListener(new ScanPropertyListener());
            scanner.execute();
            while (!scanner.isDone()) {
                Thread.sleep(100L);
            }
        } else {
            System.out.println("Usage: ");
            System.out.println("\tjava -jar classfilescanner.jar <dir|archive> @<refs file> [@<excls file>]");
        }
    }

    private void initLAFMenu() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo lafi : lafs) {
            JMenuItem mi = new JRadioButtonMenuItem(new LafAction(lafi, this));
            if (laf != null && laf.getName().startsWith(lafi.getName())) {
                mi.setSelected(true);
            }
            lafGroup.add(mi);
            lafMenu.add(mi);
        }
    }

    private static class ScanPropertyListener implements PropertyChangeListener {

        public ScanPropertyListener() {
            System.out.println();
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("nbrofScannedClasses".equals(evt.getPropertyName())) {
                System.out.print("\r" + evt.getNewValue());
            } else if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                try {
                    BackgroundScanner scanner = (BackgroundScanner)evt.getSource();
                    Set<Reference> results = scanner.get();
                    System.out.println("\nReferences from " + scanner.getRootForScan().getAbsolutePath() + ":");
                    for (Reference ref : results) {
                        System.out.println(ref);
                    }
                    System.out.println("\nScanned " + scanner.getNbrofScannedClasses() + " classes.");
                } catch (InterruptedException ex) {
                } catch (ExecutionException ex) {
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel fldStatus;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu lafMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JFileChooser resultFileChooser;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
    private javax.swing.ButtonGroup lafGroup = new ButtonGroup();
}
