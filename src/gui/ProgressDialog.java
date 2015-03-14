
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;


public class ProgressDialog extends JDialog{
    
    private JButton cancelButton;
    private JProgressBar progressbar;
    
    public ProgressDialog(Window parent){
        super(parent, "Messages Downloading...", ModalityType.APPLICATION_MODAL);
        cancelButton = new JButton("Cancel");
        progressbar = new JProgressBar();
        progressbar.setStringPainted(true);
        
        progressbar.setMaximum(10);
        progressbar.setString("Retrieving messages...");
       // progressbar.setIndeterminate(true);
        
        setLayout(new FlowLayout());
        add(progressbar);
        add(cancelButton);
        
        Dimension size = cancelButton.getPreferredSize();
        size.width=400;
        progressbar.setPreferredSize(size);
        
        pack();
        
        setLocationRelativeTo(parent);
    }
    public void setMaximum(int value){
        progressbar.setMaximum(value);
    }
    public void setValue(int value){
        int progres = 100*value/progressbar.getMaximum();
        progressbar.setString(String.format("%d%% complete", progres));
        progressbar.setValue(value);
    }

    public void setVisible(final boolean visible) {
       
        
        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        
                        if(visible == false){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ProgressDialog.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            progressbar.setValue(0);
                        }
                        ProgressDialog.super.setVisible(visible);
                        
                    }
                });
    }
    
}
