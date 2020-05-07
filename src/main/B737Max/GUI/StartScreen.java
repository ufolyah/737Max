package B737Max.GUI;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.concurrent.TimeUnit;

public class StartScreen {
    private JPanel panel1;
    private JLabel PlaneLogo;
    private JLabel Welcome;

    public static void main(String[] args) throws Exception {
        JFrame mainframe = new JFrame("Startup");
        mainframe.setContentPane((new StartScreen().panel1));
        mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainframe.setSize(800, 350);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);

        TimeUnit.MILLISECONDS.sleep(500);


        mainframe.setVisible(false);

        SearchScreen searcher = new SearchScreen();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        PlaneLogo = new JLabel(new ImageIcon(getClass().getResource("PlaneIcon.png")));
    }
}
