package classfilescanner;

import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author z705692
 */
public class LafAction extends AbstractAction {
    private LookAndFeelInfo lafInfo;
    private Window windowToUpdate;
    
    LafAction(LookAndFeelInfo lafInfo, Window componentToUpdate) {
        super(lafInfo.getName());
        this.lafInfo = lafInfo;
        this.windowToUpdate = componentToUpdate;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(lafInfo.getClassName());
            SwingUtilities.updateComponentTreeUI(windowToUpdate);
            windowToUpdate.pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
