/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author snehil
 */
import java.util.Calendar;
public class ComboFlight implements Comparable<ComboFlight> {
    Flight spiceFlight;
    Flight silkFlight;
    Calendar spiceDate;
    Calendar silkDate;
    int totalTime;
    int transitTime;
    /**
     * Default Constructor 
     */
    ComboFlight(){}
    /**
     * Constructor
     * @param spice SpiceJet Flight
     * @param silk SilkAir Flight
     * @param SpiceDate SpiceJet Date - Calendar
     * @param SilkDate SilkAir Date - Calendar
     */
    ComboFlight(Flight spice, Flight silk, Calendar SpiceDate, Calendar SilkDate){
        this.spiceFlight = spice;
        this.silkFlight = silk;
        this.spiceDate = SpiceDate;
        this.silkDate = SilkDate;
    }
    /**
     * Implementation of compareTo function defined in Comparable class
     * @param temp ComboFlight object
     * @return 1 if this totalTime of this Calendar Object is more than totalTime of temp Calendar object
     * -1 if total time this Calendar Object is less than totalTime of temp Calendar Object, 0 if same
     */
    public int compareTo(ComboFlight temp){
        if(totalTime > temp.totalTime) 
            return 1;
        else if(totalTime < temp.totalTime)
            return -1;
        return 0;
    }
}
