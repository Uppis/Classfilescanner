package com.vajasoft.classfilescanner;

import com.vajasoft.classfile.ClassFile;
import com.vajasoft.classfile.InvalidClassFileException;
import com.vajasoft.classfile.Reference;
import com.vajasoft.filetree.UsageException;
import com.vajasoft.filetree.FileTarget;
import com.vajasoft.filetree.ZipInputStreamTarget;
import com.vajasoft.filetree.ScanTarget;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 *
 * @author z705692
 */
public class BackgroundScanner extends SwingWorker<Set<Reference>, MutableTreeNode> {
    private final File rootForScan;
    private final Collection<Reference> references;
    private final Collection<Reference> excludes;
    Set<Reference> foundReferences = new TreeSet<>(); // Sorted according to the natural order of elements (see String compareTo)
    private int nbrofScannedClasses;
    private int nbrofClassesWithRefs;
    private final DefaultTreeModel model;
    private MutableTreeNode subTree;

    public BackgroundScanner(File root, Collection<Reference> refs, Collection<Reference> excls, DefaultTreeModel mod) {
        rootForScan = root;
        references = refs;
        excludes = excls != null ? excls : new ArrayList<>();
        model = mod;
    }

    @Override
    protected Set<Reference> doInBackground() throws Exception {
        getPropertyChangeSupport().firePropertyChange("nbrofScannedClasses", -1, nbrofScannedClasses); // "reset" to '0'
        getPropertyChangeSupport().firePropertyChange("nbrofClassesWithRefs", -1, nbrofClassesWithRefs); // "reset" to '0'
        try {
            if (rootForScan.isDirectory()) {
                scanDir(null, rootForScan);
            } else {
                FileInputStream fis = new FileInputStream(rootForScan);
                scanArchive(null, new ZipInputStream(fis));
                fis.close();
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        return foundReferences;
    }

    public void checkUsage() throws UsageException {
    }

    public void setRoot(File f) {
    }

    public File getRootForScan() {
        return rootForScan;
    }

    public int getNbrofScannedClasses() {
        return nbrofScannedClasses;
    }

    public int getNbrofClassesWithRefs() {
        return nbrofClassesWithRefs;
    }

    public int getNbrofFoundReferences() {
        return foundReferences.size();
    }

    private boolean scanDir(MutableTreeNode dirNode, File dir) throws IOException {
        boolean foundRefs = false;
//        File[] files = dir.listFiles(classFileFilter);// filter can be null !
        File[] files = dir.listFiles();
        for (File f : files) {
            if (isCancelled()) {
                break;
            }
            boolean foundFromFile = false;
            MutableTreeNode fileNode = new DefaultMutableTreeNode(f.getName());
            if (f.isDirectory()) {
                foundFromFile = scanDir(fileNode, f);
            } else if (Util.isArchive(f.getName())) {
                foundFromFile = scanArchive(fileNode, new ZipInputStream(new FileInputStream(f)));
            } else if (Util.isClassFile(f.getName())) {
                FileInputStream fis = new FileInputStream(f);
                foundFromFile = scanTarget(fileNode, new FileTarget(f, fis));
                fis.close();
            }
            if (foundFromFile) {
                handleResult(dirNode, fileNode);
                foundRefs = true;
            }
        }
        return foundRefs;
    }

    private boolean scanArchive(MutableTreeNode archiveNode, ZipInputStream zis) throws IOException {
        boolean foundRefs = false;
        ZipEntry e = zis.getNextEntry();
        while (e != null && !isCancelled()) {
            boolean foundFromEntry = false;
            String name = e.getName();
            MutableTreeNode entryNode = new DefaultMutableTreeNode(name);
            if (Util.isClassFile(name)) {
                foundFromEntry = scanTarget(entryNode, new ZipInputStreamTarget(zis, e));
            } else if (Util.isArchive(name)) {
                foundFromEntry = scanArchive(entryNode, new ZipInputStream(zis));
            }
            zis.closeEntry();
            if (foundFromEntry) {
                handleResult(archiveNode, entryNode);
                foundRefs = true;
            }
            e = zis.getNextEntry();
        }
        return foundRefs;
    }

    private boolean scanTarget(MutableTreeNode classNode, ScanTarget target) throws IOException {
        boolean foundRefs = false;
        try {
            ClassFile cf = new ClassFile(target.getInputStream());
            Collection<Reference> refs = com.vajasoft.filetree.Util.findReferences(references, cf, excludes);
            if (refs.size() > 0) {
//                MutableTreeNode classNode = new DefaultMutableTreeNode(cf);//target.getName());
                for (Reference r : refs) {
                    DefaultMutableTreeNode refNode = new DefaultMutableTreeNode(r);
                    refNode.setAllowsChildren(false);
                    handleResult(classNode, refNode);
                }
//                publish(classNode);
                foundReferences.addAll(refs);
                getPropertyChangeSupport().firePropertyChange("nbrofClassesWithRefs", nbrofClassesWithRefs, ++nbrofClassesWithRefs);
                foundRefs = true;
            }
            getPropertyChangeSupport().firePropertyChange("nbrofScannedClasses", nbrofScannedClasses, ++nbrofScannedClasses);
        } catch (InvalidClassFileException e) {
            System.out.println("Invalid class: " + target.getName());
        }
        return foundRefs;
    }

    private void handleResult(MutableTreeNode parent, MutableTreeNode child) {
        if (parent != null) {
            parent.insert(child, parent.getChildCount());    // Append new child
        } else {
            publish(child);  // archive is the root, so can't modify the model directly in a background thread
        }
    }

    @Override
    protected void process(List<MutableTreeNode> nodes) {
        if (model != null) {
            MutableTreeNode rootNode = (MutableTreeNode)model.getRoot();
            for (MutableTreeNode n : nodes) {
                model.insertNodeInto(n, rootNode, rootNode.getChildCount());
            }
        }
    }
}
