/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author harsh
 */
public class DisplayManager {
    FRSManager frs;
    Booking booking;
    Confirmation confirm;
    Input Inp;
    SearchResults results;
    
    /**
     * Initializations
     * @param frs Link to FRSManager
     */
    DisplayManager(FRSManager frs){
        this.frs=frs;
        this.Inp = new Input(this);
        this.results = new SearchResults(this);
        this.booking = new Booking(this);
        this.confirm = new Confirmation(this);
    }
    /**
     * Shows Input Screen.
     */
    public void showInputScreen(){
        this.Inp.setVisible(true);
    }
    /**
     * Shows Result Screen and hides Input Screen.
     */
    public void showSearchResultScreen(){
        this.Inp.setVisible(false);
        this.results.setVisible(true);
    }
    /**
     * Shows Booking Screen and hides Result Screen.
     */
    public void showBookingScreen(){
        this.results.setVisible(false);
        this.booking.setVisible(true);
    }
    /**
     * Shows Confirmation Screen and hides Booking Screen.
     */
    public void showConfirmationScreen(){
        this.booking.setVisible(false);
        this.confirm.setVisible(true);
    }
}
