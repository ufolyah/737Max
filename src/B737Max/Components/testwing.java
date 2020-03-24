package B737Max.Components;

import javax.swing.*;
import java.time.LocalDate;

public class testwing extends JFrame {
    public static void main(String[] args) throws Exception{
        testwing thing = new testwing();

        ServerInterface service = ServerInterface.getInstance();

        thing.getFlights(service);
    }

    JPanel p = new JPanel();
    JButton b = new JButton("Hey there!");
    JTextField t = new JTextField("Duff");
    JTextArea ta = new JTextArea("Multi-line test\nI think it worked", 20, 5);

    public testwing(){
        setSize(400, 300);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //UtilDateModel model = new UtilDateModel();
        //JDatePanelImpl datePanel = new JDatePanelImpl(model);
        //JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);

        //frame.add(datePicker);

        setVisible(true);
    }

    public void getFlights(ServerInterface theService) throws Exception{
        theService.getAirports();
        theService.getAirplanes();
        Flight[] fList = theService.getDepartureFlights(
                Airports.getInstance().selectByCode("BOS"),
                LocalDate.parse("2020-05-10")
        );
        for (Flight i:fList) {
            System.out.println(i);
        }
    }
}
