package B737Max.GUI;

import B737Max.Components.SearchConfig;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ResultsScreen {
    private JPanel ResultsPanel;

    public ResultsScreen(){
        JFrame resultFrame = new JFrame("ResultsMenu");
        resultFrame.setContentPane((ResultsPanel));
        resultFrame.setSize(700, 500);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setVisible(true);




    }
}
