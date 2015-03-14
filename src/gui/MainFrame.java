package gui;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import oracle.jrockit.jfr.tools.ConCatRepository;

public class MainFrame extends JFrame {

    private TextPanel textPanel;
    private Toolbar toolbar;
    private FormPanel formPanel;
    private JFileChooser fileChooser;
    private Controller controller;
    private TablePanel tablePanel;
    private PrefsDialog prefsDialog;
    private Preferences prefs;
    private JSplitPane splitPane;
    private JTabbedPane tabPane;
    private MessagePanel messagePanel;
    
    public MainFrame() {
        super("Hello World");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        textPanel = new TextPanel();
        formPanel = new FormPanel();

        controller = new Controller();
        tablePanel = new TablePanel();
        prefsDialog = new PrefsDialog(this);
        tabPane = new JTabbedPane();
        messagePanel = new MessagePanel(this);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tabPane);
        
        splitPane.setOneTouchExpandable(true);
        
        tabPane.addTab("Person Database", tablePanel);
        tabPane.addTab("Mesages", messagePanel);
       
        prefs = Preferences.userRoot().node("db");

        tablePanel.setData(controller.getPeople());

        tablePanel.setPersonTableListener(new PersonTableListener() {
            public void rowDeleted(int row) {
                controller.removePerson(row);
            }
        });

        prefsDialog.setPrefsListener(new PrefsListener() {
            public void preferencesSet(String user, String password, int port) {
                prefs.put("user", user);
                prefs.put("password", password);
                prefs.putInt("port", port);
            }

        });
        String user = prefs.get("user", "");
        String password = prefs.get("password", "");
        Integer port = prefs.getInt("port", 3306);

        prefsDialog.setDefaults(user, password, WIDTH);
        fileChooser = new JFileChooser();

        fileChooser.addChoosableFileFilter(new PersonFileFilter());
        setJMenuBar(createMenuBar());

        toolbar.setToolbarListener(new ToolbarListener() {
            public void saveEventOccured() {
                connect();
                try {
                    controller.connect();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, ex.getCause().getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    controller.save();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Ne moze da se konektuje na bazu: " + ex.getCause().getMessage(), "Konektovanje", JOptionPane.ERROR_MESSAGE);
                }
                tablePanel.refresh();
            }

            public void refreshEventOccured() {
                connect();
                try {
                    controller.load();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, ex.getCause().getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        formPanel.setFormListener(new FormListener() {
            public void formEventOccured(FormEvent e) {

                controller.addPerson(e);
                tablePanel.refresh();
            }
        });
        
        addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent ev){
               controller.disconnect();
               dispose();
               System.gc();
           }
          });
       
        add(toolbar, BorderLayout.PAGE_START);
        add(splitPane, BorderLayout.CENTER);

        setMinimumSize(new Dimension(500, 400));
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void connect() {
       try{
           controller.connect();
       }catch(Exception ex){
           JOptionPane.showMessageDialog(MainFrame.this, ex.getCause().getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
       }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exportData = new JMenuItem("Export Data...");
        JMenuItem importData = new JMenuItem("Import Data...");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exportData);
        fileMenu.add(importData);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window");
        JMenu showMenu = new JMenu("Show");
        JMenuItem prefsItem = new JMenuItem("Preferences...");

        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        prefsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prefsDialog.setVisible(true);
            }
        });

        showFormItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
                if(menuItem.isSelected()){
                    splitPane.setDividerLocation((int)formPanel.getMinimumSize().getWidth());
                }
                formPanel.setVisible(menuItem.isSelected());
            }
        });

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                ActionEvent.CTRL_MASK));

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));

        importData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                ActionEvent.CTRL_MASK));

        importData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.loadFromFile(fileChooser.getSelectedFile());
                        tablePanel.refresh();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        exportData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.saveToFile(fileChooser.getSelectedFile());
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Could not save data to file", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int action = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure ?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    WindowListener[] listeners = getWindowListeners();
                    for(WindowListener listener : listeners){
                        listener.windowClosing(new WindowEvent(MainFrame.this, 0));
                    }
                }

            }
        });
        return menuBar;
    }

}
