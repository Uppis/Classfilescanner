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
    private final DefaultTreeModel model;

    public BackgroundScanner(File root, Collection<Reference> refs, Collection<Reference> excls, DefaultTreeModel mod) {
        rootForScan = root;
        references = refs;
        excludes = excls != null ? excls : new ArrayList<>();
        model = mod;
    }

    @Override
    protected Set<Reference> doInBackground() throws Exception {
        getPropertyChangeSupport().firePropertyChange("nbrofScannedClasses", -1, nbrofScannedClasses); // "reset" to '0'
        try {
            if (rootForScan.isDirectory()) {
                scanDir(rootForScan);
            } else {
                FileInputStream fis = new FileInputStream(rootForScan);
                scanArchive(new ZipInputStream(fis));
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

    public int getNbrofFoundReferences() {
        return foundReferences.size();
    }

    private void scanDir(File dir) throws IOException {
//        File[] files = dir.listFiles(classFileFilter);// filter can be null !
        File[] files = dir.listFiles();
        for (File f : files) {
            if (isCancelled()) {
                break;
            }
            if (f.isDirectory()) {
                scanDir(f);
            } else if (Util.isArchive(f.getName())) {
                scanArchive(new ZipInputStream(new FileInputStream(f)));
            } else if (Util.isClassFile(f.getName())) {
                FileInputStream fis = new FileInputStream(f);
                scanTarget(new FileTarget(f, fis));
                fis.close();
            }
        }
    }

    private void scanArchive(ZipInputStream zis) throws IOException {
        ZipEntry e = zis.getNextEntry();
        while (e != null && !isCancelled()) {
            String name = e.getName();
            if (Util.isClassFile(name)) {
                scanTarget(new ZipInputStreamTarget(zis, e));
            } else if (Util.isArchive(name)) {
                scanArchive(new ZipInputStream(zis));
            }
            zis.closeEntry();
            e = zis.getNextEntry();
        }
    }

    private boolean scanTarget(ScanTarget target) throws IOException {
        try {
            ClassFile cf = new ClassFile(target.getInputStream());
            Collection<Reference> refs = com.vajasoft.filetree.Util.findReferences(references, cf, excludes);
            if (refs.size() > 0) {
                MutableTreeNode classNode = new DefaultMutableTreeNode(cf);//target.getName());
                for (Reference r : refs) {
                    DefaultMutableTreeNode refNode = new DefaultMutableTreeNode(r);
                    refNode.setAllowsChildren(false);
                    classNode.insert(refNode, classNode.getChildCount());
                }
                publish(classNode);
                foundReferences.addAll(refs);
            }
            int oldCount = nbrofScannedClasses;
            getPropertyChangeSupport().firePropertyChange("nbrofScannedClasses", oldCount, ++nbrofScannedClasses);
        } catch (InvalidClassFileException e) {
            System.out.println("Invalid class: " + target.getName());
        }
        return true;
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
