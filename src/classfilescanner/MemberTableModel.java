/*
 * NameAndTypeTableModel.java
 *
 * Created on 9. toukokuuta 2007, 12:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package classfilescanner;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import classfile.*;

/**
 *
 * @author z705692
 */
public class MemberTableModel extends AbstractTableModel {
    private String[] headers = {"Flags", "Name", "Type"};
    private List<Member> data = new ArrayList<Member>();
    
    /** Creates a new instance of NameAndTypeTableModel */
    public MemberTableModel() {
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;
        Member nt = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                ret = classfile.Util.formatAccessFlags(nt.getAccessFlags());
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
