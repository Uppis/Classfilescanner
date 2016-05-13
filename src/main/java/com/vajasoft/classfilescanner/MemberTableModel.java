package com.vajasoft.classfilescanner;

import com.vajasoft.classfile.Member;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author z705692
 */
public class MemberTableModel extends AbstractTableModel {
    private final String[] headers = {"Flags", "Name", "Type"};
    private final List<Member> data = new ArrayList<>();
    
    /** Creates a new instance of NameAndTypeTableModel */
    public MemberTableModel() {
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
        Member nt = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                ret = com.vajasoft.classfile.Util.formatAccessFlags(nt.getAccessFlags());
                break;
            case 1:
                ret = nt.getName();
                break;
            case 2:
                ret = nt.getDescriptor();
                break;
        }
        return ret;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public void clearData() {
        data.clear();
        fireTableDataChanged();
    }

    public void addData(Member[] d) {
        data.addAll(Arrays.asList(d));
        fireTableDataChanged();
    }
    
    public Member getData(int ix) {
        return data.get(ix);
    }
}
