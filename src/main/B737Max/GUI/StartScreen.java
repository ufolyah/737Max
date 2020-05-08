package B737Max.GUI;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.concurrent.TimeUnit;

/**
 *  Entry Screen of Reservation System GUI.
 */
public class StartScreen {
    private JPanel panel1;
    private JLabel PlaneLogo;
    private JLabel Welcome;

    /**
     * Entry point of Reservation System GUI.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        JFrame mainframe = new JFrame("Startup");
        mainframe.setContentPane((new StartScreen().panel1));
        mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainframe.setSize(800, 350);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        mainframe.setVisible(false);

        SearchScreen searcher = new SearchScreen();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        PlaneLogo = new JLabel(new ImageIcon(getClass().getResource("PlaneIcon.png")));
    }
}
