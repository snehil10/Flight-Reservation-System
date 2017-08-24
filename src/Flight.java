/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author snehil
 */
import java.util.ArrayList;
import java.util.Calendar;
public class Flight {
    String flightID;
    String source;
    String destination;
    ArrayList<Integer> daysOfWeek;
    int arrTime;
    int depTime;
    int extendsNextDay;
    Calendar effFrom;
    Calendar effTill;
    String via;
    String remark;
    /**
     * Constructor for SpiceJet Flight.
     * @param ID FlightID
     * @param src Source City
     * @param dest Destination City
     * @param dTime Departure time
     * @param aTime Arrival Time
     * @param eFrom Effective from
     * @param eTill Effective Till
     * @param dow Days of Week the flight runs
     * @param via Intermediate stops
     */
    public Flight(String ID, String src, String dest, int dTime, int aTime, Calendar eFrom, Calendar eTill, ArrayList<Integer> dow, String via){
        this.arrTime = aTime;
        this.daysOfWeek = dow;
        this.depTime = dTime;
        this.destination = dest;
        this.effFrom = eFrom;
        this.effTill = eTill;
        this.flightID = ID;
        this.source = src;
        this.via = via;
    }
    /**
     * Constructor for SilkAir Flight.
     * @param ID Flight ID
     * @param src Source
     * @param dest Destination
     * @param dTime Departure time
     * @param aTime Arrival Time
     * @param dow Days of Week the flight runs
     * @param remark Remarks - Anomalies in Flight Flying Dates
     * @param next - if Flight extend to next day
     */
    public Flight(String ID, String src, String dest, int dTime, int aTime, ArrayList<Integer> dow, String remark, int next){
        this.arrTime = aTime;
        this.daysOfWeek = dow;
        this.depTime = dTime;
        this.destination = dest;
        this.flightID = ID;
        this.source = src;
        this.remark = remark;
        this.extendsNextDay=next;
        this.effFrom = Calendar.getInstance();
        this.effFrom.clear();
        this.effFrom.set(2016, 8, 1, 0, 0);
        this.effTill = Calendar.getInstance();
        this.effTill.clear();
        this.effTill.set(2016, 10, 13, 23, 59);
    }
}
