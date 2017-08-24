/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author snehil
 */
public class BookingManager {
    FRSManager frs;
    /**
     * Constructor for Booking Manager
     * @param frs - Link to FRSManager
     */
    BookingManager(FRSManager frs){
        this.frs = frs;
    }
    /**
     * Calls the function to write in bookedSeats.csv file
     * @param confirmedFlight Combination of selected file
     * @param name Name of the customer
     * @param PNR PNR number of the ticket
     * @param seats number of requested seats
     */
    public void bookTicket(ComboFlight confirmedFlight, String name, Integer PNR, Integer seats){
        frs.dataManager.writeBookedData(confirmedFlight, name, PNR, seats);
    }
}