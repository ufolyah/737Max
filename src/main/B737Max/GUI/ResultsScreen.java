package B737Max.GUI;

import B737Max.Components.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;

public class ResultsScreen {
    private JPanel ResultsPanel;
    private JComboBox ResultsBox;
    private JButton reserveTicketButton;
    private JComboBox sortByBox;
    private JButton returnToSearchButton;
    private JComboBox ResultsBoxR;
    private JLabel lblReturn;
    private JComboBox layoverBox;

    public ResultsScreen(Trips theResults, JFrame otherFrame, boolean isRoundTrip, Trips theResultsR){
        JFrame resultFrame = new JFrame("ResultsMenu");
        resultFrame.setContentPane((ResultsPanel));
        resultFrame.setSize(1920, 500);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setVisible(true);

        if(theResults.getTrips().length == 0 || theResultsR.getTrips().length == 0){
            JOptionPane.showMessageDialog(null, "No flights were found!");
            returnToSearch(otherFrame, resultFrame);
        }

        if(isRoundTrip){
            ResultsBoxR.setEnabled(true);
        } else {
            ResultsBoxR.setVisible(false);
            lblReturn.setVisible(false);
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

        if(isRoundTrip){
            for(Trip t: theResultsR.getTrips()){
                String theTrip;
                theTrip = printTrip.apply(t);
                ResultsBoxR.addItem(theTrip);
            }
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
        layoverBox.addItem("Any");
        layoverBox.addItem("Non-stop");
        layoverBox.addItem("1 stop");
        layoverBox.addItem("2 stops");


        reserveTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (ResultsBox.getSelectedIndex()==-1) {
                    JOptionPane.showMessageDialog(null, "You have not chosen a trip.");
                    return;
                }
                if (isRoundTrip && ResultsBoxR.getSelectedIndex()==-1) {
                    JOptionPane.showMessageDialog(null, "You have not chosen a return trip.");
                    return;
                }
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to reserve this flight?");
                if(result == JOptionPane.YES_OPTION){

                    Trip[] theTrip;
                    if(isRoundTrip){
                        theTrip = new Trip[2];
                        theTrip[0] = theResults.getTrips()[ResultsBox.getSelectedIndex()];
                        theTrip[1] = theResultsR.getTrips()[ResultsBoxR.getSelectedIndex()];
                    } else {
                        theTrip = new Trip[1];
                        theTrip[0] = theResults.getTrips()[ResultsBox.getSelectedIndex()];
                    }

                    //System.out.println(theTrip[0].toString());
                    try{
                        ServiceBase.reserve(theTrip);

                    } catch(Exception e){

                    }
                    JOptionPane.showMessageDialog(null, "Your ticket has been reserved! Now returning to search menu...");
                    returnToSearch(otherFrame, resultFrame);
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

                if(isRoundTrip){
                    if(sortByBox.getSelectedItem().toString() == "Lowest Price"){
                        theResults.sortBy(Comparator.comparing(Trip::getPrice));
                        theResultsR.sortBy(Comparator.comparing(Trip::getPrice));
                    } else if(sortByBox.getSelectedItem().toString() == "Earliest Arrival Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getArrivalTime));
                        theResultsR.sortBy(Comparator.comparing(Trip::getArrivalTime));
                    } else if(sortByBox.getSelectedItem().toString() == "Earliest Departure Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getDepartureTime));
                        theResultsR.sortBy(Comparator.comparing(Trip::getDepartureTime));
                    } else if(sortByBox.getSelectedItem().toString() == "Shortest Travel Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getTravelTime));
                        theResultsR.sortBy(Comparator.comparing(Trip::getTravelTime));
                    } else if(sortByBox.getSelectedItem().toString() == "Highest Price"){
                        theResults.sortBy(Comparator.comparing(Trip::getPrice).reversed());
                        theResultsR.sortBy(Comparator.comparing(Trip::getPrice).reversed());
                    } else if(sortByBox.getSelectedItem().toString() == "Longest Travel Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getTravelTime).reversed());
                        theResultsR.sortBy(Comparator.comparing(Trip::getTravelTime).reversed());
                    } else if(sortByBox.getSelectedItem().toString() == "Latest Arrival Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getArrivalTime).reversed());
                        theResultsR.sortBy(Comparator.comparing(Trip::getArrivalTime).reversed());
                    } else if(sortByBox.getSelectedItem().toString() == "Latest Departure Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getDepartureTime).reversed());
                        theResultsR.sortBy(Comparator.comparing(Trip::getDepartureTime).reversed());
                    }
                } else{
                    if(sortByBox.getSelectedItem().toString() == "Lowest Price"){
                        theResults.sortBy(Comparator.comparing(Trip::getPrice));
                    } else if(sortByBox.getSelectedItem().toString() == "Earliest Arrival Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getArrivalTime));
                    } else if(sortByBox.getSelectedItem().toString() == "Earliest Departure Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getDepartureTime));
                    } else if(sortByBox.getSelectedItem().toString() == "Shortest Travel Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getTravelTime));
                    } else if(sortByBox.getSelectedItem().toString() == "Highest Price"){
                        theResults.sortBy(Comparator.comparing(Trip::getPrice).reversed());
                    } else if(sortByBox.getSelectedItem().toString() == "Longest Travel Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getTravelTime).reversed());
                    } else if(sortByBox.getSelectedItem().toString() == "Latest Arrival Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getArrivalTime).reversed());
                    } else if(sortByBox.getSelectedItem().toString() == "Latest Departure Time"){
                        theResults.sortBy(Comparator.comparing(Trip::getDepartureTime).reversed());
                    }
                }


                ResultsBox.removeAllItems();
                // Print out the trips
                for(Trip t: theResults.getTrips()){
                    String theTrip;
                    theTrip = printTrip.apply(t);

                    ResultsBox.addItem(theTrip);
                }

                if(isRoundTrip){
                    ResultsBoxR.removeAllItems();
                    // Print out the trips
                    for(Trip t: theResultsR.getTrips()){
                        String theTrip;
                        theTrip = printTrip.apply(t);

                        ResultsBoxR.addItem(theTrip);
                    }
                }
            }
        });

        layoverBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String option = layoverBox.getSelectedItem().toString();

                if (option.equals("Any")) {
                    theResults.removeFilter("layover");
                } else if (option.equals("Non-stop")) {
                    theResults.filterBy("layover", (Trip t) -> t.getNumFlights() == 1);
                } else if (option.equals("1 stop")) {
                    theResults.filterBy("layover", (Trip t) -> t.getNumFlights() == 2);
                } else if (option.equals("2 stops")) {
                    theResults.filterBy("layover", (Trip t) -> t.getNumFlights() == 3);
                }
                ResultsBox.removeAllItems();
                for (Trip t : theResults.getTrips()) {
                    String theTrip;
                    theTrip = printTrip.apply(t);

                    ResultsBox.addItem(theTrip);
                }

                if (isRoundTrip) {
                    if (option.equals("Any")) {
                        theResultsR.removeFilter("layover");
                    } else if (option.equals("Non-stop")) {
                        theResultsR.filterBy("layover", (Trip t) -> t.getNumFlights() == 1);
                    } else if (option.equals("1 stop")) {
                        theResultsR.filterBy("layover", (Trip t) -> t.getNumFlights() == 2);
                    } else if (option.equals("2 stops")) {
                        theResultsR.filterBy("layover", (Trip t) -> t.getNumFlights() == 3);
                    }
                    ResultsBoxR.removeAllItems();
                    for (Trip t : theResultsR.getTrips()) {
                        String theTrip;
                        theTrip = printTrip.apply(t);

                        ResultsBoxR.addItem(theTrip);
                    }
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
