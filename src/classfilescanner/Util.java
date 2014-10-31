package classfilescanner;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author z705692
 */
public class Util {

    public static void setListSelectionListeners(JTable list, JButton defaultButton) {
        list.addMouseListener(new MouseDblClickListener(defaultButton));
        list.addKeyListener(new EnterKeyListener(defaultButton));
        list.getSelectionModel().addListSelectionListener(new DefaultButtonEnabler(list, defaultButton));
    }

    public static void updateComboList(JComboBox box) {
        Object selected = box.getSelectedItem();
        if (selected != null && selected instanceof String) {
            String val = (String)selected;
            MutableComboBoxModel model = (MutableComboBoxModel)box.getModel();
            int i = model.getSize() - 1;
            while (i >= 0 && val.compareTo(String.valueOf(model.getElementAt(i))) < 0) {
                i--;
            }
            if (i < 0 || !val.equalsIgnoreCase(String.valueOf(model.getElementAt(i)))) {
                model.insertElementAt(new RecentItem(val), i + 1);
            }
        }
    }

    public static RecentItem[] loadRecentItems(Preferences fromNode) throws BackingStoreException {
        String[] keys = fromNode.keys();
        ArrayList<RecentItem> values = new ArrayList<RecentItem>(keys.length);
        for (String key : keys) {
            String val = fromNode.get(key, null);
            if (val != null) {
                values.add(new RecentItem(val));
            }
        }
        Collections.sort(values);
        return values.toArray(new RecentItem[values.size()]);
    }

    public static void saveRecentItems(ComboBoxModel fromModel, Preferences toNode, int maxNbrofItems) throws BackingStoreException {
        RecentItem[] items = new RecentItem[fromModel.getSize()];
        for (int i = 0; i < items.length; i++) {
            items[i] = (RecentItem)fromModel.getElementAt(i);
        }
        saveRecentItems(items, toNode, maxNbrofItems);
    }

    public static void saveRecentItems(RecentItem[] items, Preferences toNode, int maxNbrofItems) throws BackingStoreException {
        toNode.clear();
        Arrays.sort(items, RecentItem.getTimeComparator(true)); // Reverse order
        for (int i = 0; i < items.length && i < maxNbrofItems; i++) {
            toNode.put("item" + i, items[i].asPersistentData());
        }
        toNode.flush();
    }

    public static File getFile(String filename) {
        File ret = null;
        String fn = filename.trim();
        if (fn.startsWith("\"") && fn.endsWith("\"") && fn.length() > 1) {
            fn = fn.substring(1, fn.length() - 1);
        }
        if (fn.length() > 0) {
            File f = new File(fn);
            if (f.exists()) {
                ret = f;
            }
        }
        return ret;
    }

    public static String[] split(CharSequence s, Pattern matchPattern) {
        ArrayList<String> matchList = new ArrayList<String>();
        Matcher m = matchPattern.matcher(s);

        while (m.find()) {
            String match = m.group(1);//s.subSequence(m.start(1), m.end(1)).toString();
            if (match == null) {
                match = m.group();
            }
            matchList.add(match);
        }

        String[] result = new String[matchList.size()];
        return matchList.toArray(result);
    }

    public static boolean isArchive(String name) {
        name = name.toLowerCase();
        return name.endsWith(".zip") || name.endsWith(".jar") || name.endsWith(".war") || name.endsWith(".ear");
    }

    public static boolean isClassFile(String name) {
        name = name.toLowerCase();
        return name.endsWith(".class");
    }

    private Util() {
    }

    private static class MouseDblClickListener extends MouseAdapter {
        JButton defaultButton;

        public MouseDblClickListener(JButton defaultButton) {
            this.defaultButton = defaultButton;
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                if (defaultButton != null) {
                    defaultButton.doClick();
                }
            }
        }
    }

    private static class EnterKeyListener extends KeyAdapter {
        JButton defaultButton;

        public EnterKeyListener(JButton defaultButton) {
            this.defaultButton = defaultButton;
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            // Deliberately swallow ENTER key
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                evt.consume();
            }
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (defaultButton != null) {
                    defaultButton.doClick();
                }
            }
        }
    }

    private static class DefaultButtonEnabler implements ListSelectionListener {
        private final JTable list;
        private final JButton defaultButton;

        public DefaultButtonEnabler(JTable list, JButton defaultButton) {
            this.list = list;
            this.defaultButton = defaultButton;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean cond = list.getSelectedRow() >= 0;
            defaultButton.setEnabled(cond);
        }
    }
}
