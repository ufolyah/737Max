package B737Max.GUI;

import B737Max.Components.SearchConfig;
import B737Max.Components.ServiceBase;
import B737Max.Components.Trips;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

class SystemSearching {
    JFrame systemSearchingFrame;
    private JPanel systemSearchPanel;
    private JLabel searchingLabel;

    public SystemSearching(){
        systemSearchingFrame = new JFrame("SystemSearchingMenu");
        systemSearchingFrame.setContentPane(systemSearchPanel);
        systemSearchingFrame.setSize(700, 300);
        systemSearchingFrame.setLocationRelativeTo(null);
        systemSearchingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        systemSearchingFrame.setVisible(true);



    }

    void hideScreen(){
        systemSearchingFrame.setVisible(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
