package com.vajasoft.classfilescanner;

import com.vajasoft.classfile.Reference;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author z705692
 */
public class ReferenceTableModel extends AbstractTableModel {
    private final String[] headers = {"Class/package", "Name", "Type"};
    private final List<Reference> data = new ArrayList<>();
    
    public ReferenceTableModel() {
    }
    
    public ReferenceTableModel(Reference[] refs) {
        data.addAll(Arrays.asList(refs));
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;
        Reference r = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                ret = r.getClassName();
                break;
            case 1:
                ret = r.getMemberName();
                break;
            case 2:
                ret = r.getMemberType();
                break;
        }
        return ret;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }
    
    public void clearData() {
        data.clear();
        fireTableDataChanged();
    }

    public void addData(Reference r) {
        data.add(r);
        fireTableDataChanged();
    }

    public void addData(Reference[] refs) {
        addData(Arrays.asList(refs));
    }

    public void addData(Collection<Reference> refs) {
        data.addAll(refs);
        fireTableDataChanged();
    }
    
    public Reference getData(int ix) {
        return data.get(ix);
    }

    public void setData(Reference[] refs) {
        data.clear();
        if (refs != null)
            data.addAll(Arrays.asList(refs));
        fireTableDataChanged();
    }
    
    public Collection<Reference> getReferences() {
        return Collections.unmodifiableList(data);
    }
}
