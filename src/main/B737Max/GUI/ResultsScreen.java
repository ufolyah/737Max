package B737Max.GUI;

import B737Max.Components.SearchConfig;
import B737Max.Components.ServiceBase;
import B737Max.Components.Trip;
import B737Max.Components.Trips;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class ResultsScreen {
    private JPanel ResultsPanel;
    private JComboBox ResultsBox;
    private JButton reserveTicketButton;
    private JComboBox sortByBox;
    private JButton returnToSearchButton;

    public ResultsScreen(Trips theResults, JFrame otherFrame){
        JFrame resultFrame = new JFrame("ResultsMenu");
        resultFrame.setContentPane((ResultsPanel));
        resultFrame.setSize(1920, 500);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setVisible(true);

        if(theResults.getTrips().length == 0){
            JOptionPane.showMessageDialog(null, "No flights were found!");
            returnToSearch(otherFrame, resultFrame);
        }

        // Print out the trips
        for(Trip t: theResults.getTrips()){
            String theTrip;
            theTrip = "Flight " + t.getFlights()[0].getFlightNo() + " from " + t.getFlights()[0].getDepartureAirport().getName() + " to "
                    + t.getFlights()[0].getArrivalAirport().getName() + ",     Departure Time: " + t.getDepartureTime()
                    + ",       Arrival Time: " + t.getArrivalTime() + ",    Seating Class: " + t.getSeatClass()[0].toString() + ",   Layovers: "
                    + t.getLayovers().length + ",    Price: $" + t.getPrice();

            ResultsBox.addItem(theTrip);
        }

        sortByBox.addItem("default");
        sortByBox.addItem("Highest Price");
        sortByBox.addItem("Lowest Price");
        sortByBox.addItem("Earliest Departure Time");
        sortByBox.addItem("Latest Departure Time");
        sortByBox.addItem("Earliest Arrival Time");
        sortByBox.addItem("Latest Arrival Time");
        sortByBox.addItem("Shortest Travel Time");
        sortByBox.addItem("Longest Travel Time");


        reserveTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to reserve this flight?");
                if(result == JOptionPane.YES_OPTION){
                    Trip[] theTrip = new Trip[1];
                    theTrip[0] = theResults.getTrips()[ResultsBox.getSelectedIndex()];
                    System.out.println(theTrip[0].toString());
                    try{
                        ServiceBase.reserve(theTrip);
                    } catch(Exception e){

                    }
                } else{

                }
            }
        });


        returnToSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                returnToSearch(otherFrame, resultFrame);
            }
        });
    }

    public void returnToSearch(JFrame otherFrame, JFrame resultFrame){
        otherFrame.setVisible(true);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.dispatchEvent(new WindowEvent(resultFrame, WindowEvent.WINDOW_CLOSING));
    }

}
