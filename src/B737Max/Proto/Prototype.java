package B737Max.Proto;

import B737Max.Components.Airport;
import B737Max.Components.Airports;
import B737Max.Components.Flight;
import B737Max.Components.ServerInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Prototype extends JFrame{

    private JTextField enterDate;
    private JPanel mainPanel;
    private JLabel dateLabel;
    private JLabel spacer;
    private JLabel locationLabel;
    private JComboBox flightLocations;
    private JButton searchAvailableFlightsButton;
    private JTextArea flightsList;

    public static void main(String[] args) throws Exception{
        Prototype thing = new Prototype();


    }


    public Prototype() throws Exception{
        setSize(600, 1000);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);

        ServerInterface service = ServerInterface.getInstance();
        Airports airports = Airports.getInstance();
        service.getAirports();
        Airport[] airs = airports.getList();

        for(Airport air : airs){
            flightLocations.addItem(air.getCode());
        }

        searchAvailableFlightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                try{
                    getFlights();
                } catch (Exception e){

                }
            }
        });
    }

    public void getFlights() throws Exception{

        flightsList.setText("");
        String airport = "";
        String date = "yyyy-mm-dd";

        date = enterDate.getText();
        airport = flightLocations.getSelectedItem().toString();

        ServerInterface service = ServerInterface.getInstance();

        service.getAirports();
        service.getAirplanes();
        Flight[] fList = service.getDepartureFlights(
                Airports.getInstance().selectByCode(airport),
                LocalDate.parse(date)
        );
        for (Flight i:fList) {
            //System.out.println("Flight " + i.getFlightNo() + " from " + i.getDepartureAirport().getName() + " to " + i.getArrivalAirport().getName());
            flightsList.append("Flight " + i.getFlightNo() + " from " + i.getDepartureAirport().getName() + " to " + i.getArrivalAirport().getName() + "\n");
        }
    }
}
