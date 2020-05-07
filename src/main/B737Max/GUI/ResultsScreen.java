package B737Max.GUI;

import B737Max.Components.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Function;

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

        Function<Duration, String> durStr = (Duration d) -> String.format("%02d",d.toHours()) +
                "H" +
                String.format("%02d", d.toMinutesPart()) +
                "M";

        Function<Trip, String> printPath = (Trip t) -> {
            StringBuilder str = new StringBuilder();
            Flight[] fList = t.getFlights();
            SeatClass[] sList = t.getSeatClass();
            Layover[] lList = t.getLayovers();
            str.append(fList[0].getDepartureAirport().getCode());
            for (int i=0; i<fList.length; i++) {
                str.append(" --(")
                        .append(sList[i].toString())
                        .append(")--> ")
                        .append(fList[i].getArrivalAirport().getCode());
                if (i!=fList.length-1) {
                    str.append("(Stay ")
                            .append(durStr.apply(lList[i].duration))
                            .append(")");
                }
            }
            return str.toString();
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");
        Function<Trip, String> printTrip = (Trip t) -> "Duration:" + durStr.apply(t.getTravelTime()) + "   Price:$" + String.format("%-8s", t.getPrice().toString()) + "  "
                + formatter.format(t.getDepartureTime()) + "->" + formatter.format(t.getArrivalTime()) + "   "
                + printPath.apply(t);

        // Print out the trips
        for(Trip t: theResults.getTrips()){
            String theTrip;
            theTrip = printTrip.apply(t);
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

        sortByBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(sortByBox.getSelectedItem().toString() == "Lowest Price"){
                    theResults.sortBy(Comparator.comparing(Trip::getPrice));
                } else if(sortByBox.getSelectedItem().toString() == "Earliest Arrival Time"){
                    theResults.sortBy(Comparator.comparing(Trip::getArrivalTime));
                } else if(sortByBox.getSelectedItem().toString() == "Earliest Departure Time"){
                    theResults.sortBy(Comparator.comparing(Trip::getDepartureTime));
                } else if(sortByBox.getSelectedItem().toString() == "Shortest Travel Time"){
                    theResults.sortBy(Comparator.comparing(Trip::getTravelTime));
                }

                ResultsBox.removeAllItems();
                // Print out the trips
                for(Trip t: theResults.getTrips()){
                    String theTrip;
                    theTrip = printTrip.apply(t);

                    ResultsBox.addItem(theTrip);
                }
            }
        });
    }

    public void returnToSearch(JFrame otherFrame, JFrame resultFrame){
        otherFrame.setVisible(true);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.dispatchEvent(new WindowEvent(resultFrame, WindowEvent.WINDOW_CLOSING));
    }

}