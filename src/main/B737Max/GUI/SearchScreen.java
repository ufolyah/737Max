package B737Max.GUI;

import B737Max.Components.Airport;
import B737Max.Components.Airports;
import B737Max.Components.ServerAPIAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public SearchScreen() throws Exception{
        JFrame searchFrame = new JFrame("SearchMenu");
        searchFrame.setContentPane((SearchPanel));
        searchFrame.setSize(700, 500);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchFrame.setVisible(true);

        ServerAPIAdapter service = ServerAPIAdapter.getInstance();

        Airports airports = Airports.getInstance();
        service.getAirports();
        Airport[] airs = airports.getList();

        for(Airport air : airs){
            departureList.addItem(air.getCode());
        }

        for(Airport air : airs){
            arrivalList.addItem(air.getCode());
        }

        for(int i = 1; i < 32; i++){
            DepartureDays.addItem(i);
        }

        for(int i = 2020; i < 2031; i++){
            DepartureYear.addItem(i);
        }

        DepartureMonth.addItem("January");
        DepartureMonth.addItem("February");
        DepartureMonth.addItem("March");
        DepartureMonth.addItem("April");
        DepartureMonth.addItem("May");
        DepartureMonth.addItem("June");
        DepartureMonth.addItem("July");
        DepartureMonth.addItem("August");
        DepartureMonth.addItem("September");
        DepartureMonth.addItem("October");
        DepartureMonth.addItem("November");
        DepartureMonth.addItem("December");

        DepartureMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String theMonth = DepartureMonth.getSelectedItem().toString();
                if(theMonth == "January" || theMonth == "March" || theMonth == "May" || theMonth == "July" || theMonth == "August" || theMonth == "October" || theMonth == "December"){
                    DepartureDays.removeAllItems();
                    for(int i = 1; i < 32; i++){
                        DepartureDays.addItem(i);
                    }
                } else if (theMonth == "April" || theMonth == "June" || theMonth == "September" || theMonth == "November"){
                    DepartureDays.removeAllItems();
                    for(int i = 1; i < 31; i++){
                        DepartureDays.addItem(i);
                    }
                } else {
                    DepartureDays.removeAllItems();
                    int stopper;
                    int theYear = Integer.parseInt(DepartureYear.getSelectedItem().toString());
                    if(theYear == 2020 || theYear == 2024 || theYear == 2028){
                        stopper = 30;
                    } else{
                        stopper = 29;
                    }

                    for(int i = 1; i < stopper; i++){
                        DepartureDays.addItem(i);
                    }
                }
            }
        });

        DepartureYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String theMonth = DepartureMonth.getSelectedItem().toString();
                if(theMonth == "February"){
                    DepartureDays.removeAllItems();
                    int stopper;
                    int theYear = Integer.parseInt(DepartureYear.getSelectedItem().toString());
                    if(theYear == 2020 || theYear == 2024 || theYear == 2028){
                        stopper = 30;
                    } else{
                        stopper = 29;
                    }

                    for(int i = 1; i < stopper; i++){
                        DepartureDays.addItem(i);
                    }
                }
            }
        });
    }
}
