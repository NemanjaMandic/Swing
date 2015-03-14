
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;


public class ProgressDialog extends JDialog{
    
    private JButton cancelButton;
    private JProgressBar progressbar;
    private ProgresDialogListener listener;
    
    public ProgressDialog(Window parent, String title){
        super(parent, title, ModalityType.APPLICATION_MODAL);
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
        
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
             if(listener != null){
                 listener.progressDialogCancelled();
             }
            }
        });
        pack();
        
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if(listener != null){
                 listener.progressDialogCancelled();
             }
            }
            
});
        setLocationRelativeTo(parent);
    }
    public void setListener(ProgresDialogListener listener){
        this.listener = listener;
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
