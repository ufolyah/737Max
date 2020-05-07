package B737Max.GUI;

import B737Max.Components.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public SearchScreen() throws Exception{
        JFrame searchFrame = new JFrame("SearchMenu");
        searchFrame.setContentPane((SearchPanel));
        searchFrame.setSize(700, 600);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchFrame.setVisible(true);

        ServiceBase.load();

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
        }

        for(int i = 2020; i < 2021; i++){
            DepartureYear.addItem(i);
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

        pickSeating.addItem("Coach");
        pickSeating.addItem("First Class");

        for(int i = 0; i < 25; i++){
            if(i < 10){
                startTimeframe.addItem("0" + i + ":00");
                endTimeframe.addItem("0" + i + ":00");
            } else {
                startTimeframe.addItem(i + ":00");
                endTimeframe.addItem(i + ":00");
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

                }

                systemSearching.hideScreen();
                ResultsScreen resultsScreen = new ResultsScreen(theResults, searchFrame);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}