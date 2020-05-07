package B737Max.GUI;

import B737Max.Components.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SearchScreen {

    private JLabel lblEnterInfo;
    private JPanel SearchPanel;
    private JComboBox departureList;
    private JLabel lblPickAirport;
    private JLabel lblPickArrive;
    private JComboBox arrivalList;
    private JComboBox DepartureMonth;
    private JComboBox DepartureYear;
    private JComboBox DepartureDays;
    private JButton searchAvailableFlightsButton;
    private JComboBox startTimeframe;
    private JComboBox endTimeframe;
    private JComboBox pickSeating;
    private JCheckBox roundTripCheckBox;
    private JComboBox departureListR;
    private JComboBox arrivalListR;
    private JComboBox departureMonthR;
    private JComboBox departureDayR;
    private JComboBox departureYearR;
    private JComboBox startTimeframeR;
    private JComboBox endTimeframeR;
    private JComboBox pickSeatingR;

    public SearchScreen() {
        JFrame searchFrame = new JFrame("SearchMenu");
        searchFrame.setContentPane((SearchPanel));
        searchFrame.setSize(1200, 600);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchFrame.setVisible(true);

        try {
            ServiceBase.load();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connection Error. Check your internet connection or retry a few seconds later.");
            System.exit(0);
            return;
        }


        Airports airports = Airports.getInstance();

        Airport[] airs = airports.getList();

        for(Airport air : airs){
            departureList.addItem(air.getName());
        }

        for(Airport air : airs){
            arrivalList.addItem(air.getName());
        }

        for(int i = 9; i < 32; i++){
            DepartureDays.addItem(i);
            departureDayR.addItem(i);
        }

        for(int i = 2020; i < 2021; i++){
            DepartureYear.addItem(i);
            departureYearR.addItem(i);
        }

//        DepartureMonth.addItem("January");
//        DepartureMonth.addItem("February");
//        DepartureMonth.addItem("March");
//        DepartureMonth.addItem("April");
          DepartureMonth.addItem("May");
//        DepartureMonth.addItem("June");
//        DepartureMonth.addItem("July");
//        DepartureMonth.addItem("August");
//        DepartureMonth.addItem("September");
//        DepartureMonth.addItem("October");
//        DepartureMonth.addItem("November");
//        DepartureMonth.addItem("December");

        departureMonthR.addItem("May");

        pickSeatingR.addItem("Coach");
        pickSeatingR.addItem("First Class");
        pickSeating.addItem("Coach");
        pickSeating.addItem("First Class");

        for(int i = 0; i < 25; i++){
            if(i < 10){
                startTimeframe.addItem("0" + i + ":00");
                endTimeframe.addItem("0" + i + ":00");
                startTimeframeR.addItem("0" + i + ":00");
                endTimeframeR.addItem("0" + i + ":00");
            } else {
                startTimeframe.addItem(i + ":00");
                endTimeframe.addItem(i + ":00");
                startTimeframeR.addItem(i + ":00");
                endTimeframeR.addItem(i + ":00");
            }
        }

//        DepartureMonth.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                String theMonth = DepartureMonth.getSelectedItem().toString();
//                if(theMonth == "January" || theMonth == "March" || theMonth == "May" || theMonth == "July" || theMonth == "August" || theMonth == "October" || theMonth == "December"){
//                    DepartureDays.removeAllItems();
//                    for(int i = 1; i < 32; i++){
//                        DepartureDays.addItem(i);
//                    }
//                } else if (theMonth == "April" || theMonth == "June" || theMonth == "September" || theMonth == "November"){
//                    DepartureDays.removeAllItems();
//                    for(int i = 1; i < 31; i++){
//                        DepartureDays.addItem(i);
//                    }
//                } else {
//                    DepartureDays.removeAllItems();
//                    int stopper;
//                    int theYear = Integer.parseInt(DepartureYear.getSelectedItem().toString());
//                    if(theYear == 2020 || theYear == 2024 || theYear == 2028){
//                        stopper = 30;
//                    } else{
//                        stopper = 29;
//                    }
//
//                    for(int i = 1; i < stopper; i++){
//                        DepartureDays.addItem(i);
//                    }
//                }
//            }
//        });

//        DepartureYear.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                String theMonth = DepartureMonth.getSelectedItem().toString();
//                if(theMonth == "February"){
//                    DepartureDays.removeAllItems();
//                    int stopper;
//                    int theYear = Integer.parseInt(DepartureYear.getSelectedItem().toString());
//                    if(theYear == 2020 || theYear == 2024 || theYear == 2028){
//                        stopper = 30;
//                    } else{
//                        stopper = 29;
//                    }
//
//                    for(int i = 1; i < stopper; i++){
//                        DepartureDays.addItem(i);
//                    }
//                }
//            }
//        });

        searchAvailableFlightsButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                searchFrame.setVisible(false);
                SystemSearching systemSearching = new SystemSearching();


                SearchConfig searchConfig = new SearchConfig();
                SearchConfig searchConfigR = new SearchConfig();
                // Convert the departure time frame to something usable
                int departStartTime = Integer.parseInt(startTimeframe.getSelectedItem().toString().substring(0, 2));
                int departEndTime = Integer.parseInt(endTimeframe.getSelectedItem().toString().substring(0, 2));
                LocalTime departureTime = departStartTime==24?LocalTime.MAX:LocalTime.of(departStartTime, 0, 0);
                LocalTime departureEnd = departEndTime==24?LocalTime.MAX:LocalTime.of(departEndTime, 0, 0);



                // Set the departure time frame
                searchConfig.setDepartureWindow(departureTime, departureEnd);


                // Convert departure date to something usable
                int departMonth = 5;
                int departDay = Integer.parseInt(DepartureDays.getSelectedItem().toString());
                int departYear = Integer.parseInt(DepartureYear.getSelectedItem().toString());

                LocalDate departureDate = LocalDate.of(departYear, departMonth, departDay);


                // Set the departure date
                searchConfig.setDepartureDate(departureDate);


                // Find the departureAirport
                Airport departPort = airports.selectByName(departureList.getSelectedItem().toString());
                Airport arrivePort = airports.selectByName(arrivalList.getSelectedItem().toString());



                // Set arrival and departure airports
                searchConfig.setArrivalAirport(arrivePort);
                searchConfig.setDepartureAirport(departPort);


                SeatClass seatClass;


                // Set seating preference
                if(pickSeating.getSelectedItem().toString().equals("Coach")){
                    seatClass = SeatClass.COACH;
                } else {
                    seatClass = SeatClass.FIRST;
                }



                searchConfig.setPreferredSeatClass(seatClass);


                // Search for trips with the given parameters
                Trips theResults = new Trips();
                try{
                    theResults = ServiceBase.searchFlights(searchConfig);
                } catch (Exception e){
                    systemSearching.hideScreen();
                    searchFrame.setVisible(true);
                    if (e instanceof IOException) {
                        JOptionPane.showMessageDialog(null, "Connection error.\n Check your internet connection or retry a few seconds later");
                    } else {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                    return;

                }

                boolean validSearch = true;
                Trips theResultsR = new Trips();
                if(roundTripCheckBox.isSelected()){
                    int departStartTimeR = Integer.parseInt(startTimeframeR.getSelectedItem().toString().substring(0, 2));
                    int departEndTimeR = Integer.parseInt(endTimeframeR.getSelectedItem().toString().substring(0, 2));
                    LocalTime departureTimeR = departStartTimeR==24?LocalTime.MAX:LocalTime.of(departStartTimeR, 0, 0);
                    LocalTime departureEndR = departEndTimeR==24?LocalTime.MAX:LocalTime.of(departEndTimeR, 0, 0);

                    searchConfigR.setDepartureWindow(departureTimeR, departureEndR);

                    int departDayR = Integer.parseInt(departureDayR.getSelectedItem().toString());
                    int departYearR = Integer.parseInt(departureYearR.getSelectedItem().toString());
                    LocalDate departureDateR = LocalDate.of(departYearR, departMonth, departDayR);
                    searchConfigR.setDepartureDate(departureDateR);

                    searchConfigR.setArrivalAirport(departPort);
                    searchConfigR.setDepartureAirport(arrivePort);

                    SeatClass seatClassR;

                    if(pickSeatingR.getSelectedItem().toString().equals("Coach")){
                        seatClassR = SeatClass.COACH;
                    } else {
                        seatClassR = SeatClass.FIRST;
                    }

                    searchConfigR.setPreferredSeatClass(seatClassR);

                    // Search for trips with the given parameters

                    try{
                        theResultsR = ServiceBase.searchFlights(searchConfigR);
                    } catch (Exception e){
                        if (e instanceof IOException) {
                            JOptionPane.showMessageDialog(null, "Connection error.\n Check your internet connection or retry a few seconds later");
                        } else {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                        }
                        return;
                    }

                    if(departDay >= departDayR){
                        JOptionPane.showMessageDialog(null, "The return trip is before the outgoing trip!");
                        validSearch = false;
                    }
                }

                systemSearching.hideScreen();
                if(!validSearch){
                    searchFrame.setVisible(true);
                } else {
                    if(roundTripCheckBox.isSelected()){
                        ResultsScreen resultsScreen = new ResultsScreen(theResults, searchFrame, true, theResultsR);
                    } else {
                        ResultsScreen resultsScreen = new ResultsScreen(theResults, searchFrame, false, theResults);
                    }
                }
            }
        });
        roundTripCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(roundTripCheckBox.isSelected()){
                    departureDayR.setEnabled(true);
                    departureMonthR.setEnabled(true);
                    departureYearR.setEnabled(true);
                    pickSeatingR.setEnabled(true);
                    startTimeframeR.setEnabled(true);
                    endTimeframeR.setEnabled(true);
                } else {
                    departureDayR.setEnabled(false);
                    departureMonthR.setEnabled(false);
                    departureYearR.setEnabled(false);
                    pickSeatingR.setEnabled(false);
                    startTimeframeR.setEnabled(false);
                    endTimeframeR.setEnabled(false);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
