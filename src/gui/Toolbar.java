
package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;


public class Toolbar extends JToolBar implements ActionListener{
    private JButton saveButton;
    private JButton refreshButton;
    private ToolbarListener textListener;
    
    public Toolbar(){
        setBorder(BorderFactory.createEtchedBorder());
        
       // setFloatable(false);
        saveButton = new JButton();
        saveButton.setIcon(Utils.createIcon("/images/Save16.gif"));
        saveButton.setToolTipText("Save");
        
        refreshButton = new JButton(Utils.createIcon("/images/Refresh16.gif"));
        refreshButton.setToolTipText("Refresh");
        
        saveButton.addActionListener(this);
        refreshButton.addActionListener(this);
       // setLayout(new FlowLayout(FlowLayout.LEFT));
        add(saveButton);
        add(refreshButton);
    } 
   
    public void setToolbarListener(ToolbarListener listener){
        this.textListener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      
       if(e.getSource() == saveButton){
         if(textListener != null){
             textListener.saveEventOccured();
         }
       }else if(e.getSource() == refreshButton){
           if(textListener != null){
             textListener.refreshEventOccured();
         }
       }
    }
}
