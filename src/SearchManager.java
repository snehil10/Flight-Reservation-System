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
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;
public class SearchManager {
    /**
     * Initializations.
     */
    public FRSManager frs;
    public ArrayList<Flight> spiceSegments;
    public ArrayList<Flight> silkSegments;
    public ArrayList<ComboFlight> FlightSegment;
    public Integer PNR;
    /**
     * Constructor for SearchManager.
     * @param frs Instance of FRSManager, link to the FRSManager.
     */
    public SearchManager(FRSManager frs){
        this.frs=frs;
    }
    
    /**
     * The function returns an ArrayList of ComboFlight for traveling
     * according to the given constraints. A ComboFlight is a combination of
     * two flights which can be taken together with a transit time of more than 
     * two hours but less than six hours.
     * @param src String, Source City of travel. 
     * @param seats Integer, Requested number of seats.
     * @param searchedDate Calendar, Requested Date of travel.
     * @return ArrayList of ComboFlight, Connections of SilkAir and SpiceJet flights. 
     */
    public ArrayList<ComboFlight> SearchFlights(String src, int seats, Calendar searchedDate){
        spiceSegments = new ArrayList<>();
        silkSegments = new ArrayList<>();
        FlightSegment = new ArrayList<>();
        
        
        
        searchedDate.add(Calendar.DAY_OF_MONTH, 1);
        Calendar nextDate = Calendar.getInstance();
        //Searched date + 1 for Silk flights extending to next date
        nextDate.setTime(searchedDate.getTime());               
        searchedDate.add(Calendar.DAY_OF_MONTH, -1);
        
        SearchInSpice(src, searchedDate);                       
        SearchInSilk(searchedDate);
        ConnectSpiceSilk(src, seats, searchedDate, nextDate);
        
        Collections.sort(FlightSegment);
        return FlightSegment;
    }
    /**
     * Searches for SpiceJet flights which departs from the given source on the 
     * particular searched/requested date.
     * @param src String, Source City of travel.
     * @param searchedDate Calendar, Searched/Requested Date of travel.
     */
    public void SearchInSpice(String src, Calendar searchedDate){ 
        ArrayList<Flight> SpiceJets = this.frs.spicejet;
        for (Flight Jet : SpiceJets) {
            //If Requested Source City is equal to SpiceJet Source 
            if(Jet.source.substring(0,4).equalsIgnoreCase(src.substring(0, 4))){
                //If the Searched Date is between Effective Time of SpiceJet Flight
                if(searchedDate.compareTo(Jet.effFrom)>=0 && searchedDate.compareTo(Jet.effTill)<=0){
                    //If the Spice Jet Flight actually runs on the day of week
                    //Or it runs daily(0 stands for daily) 
                    if(Jet.daysOfWeek.contains(searchedDate.get(Calendar.DAY_OF_WEEK)) || Jet.daysOfWeek.contains(0)) {
                        spiceSegments.add(Jet);
                    }
                }
            }
        }
    }
    /**
     * Searches for SilkAir Flights flying on the Searched Date.
     * @param searchedDate Calendar, Searched/Requested Date of travel.
     */
    public void SearchInSilk(Calendar searchedDate){
        ArrayList<Flight> Silks = this.frs.silkair;
        for (Flight Jet : Silks) {
            //If the Searched Date is between Effective Time of SilkAir Flight
            if(searchedDate.compareTo(Jet.effFrom)>=0 && searchedDate.compareTo(Jet.effTill)<=0){
                //If the SilkAir Flight actually runs on the day of week
                if(Jet.daysOfWeek.contains(searchedDate.get(Calendar.DAY_OF_WEEK)))
                    silkSegments.add(Jet);
            }
        }
    }
    /**
     * The method connects the SpiceJet flight with the SilkAir flight and 
     * calculates the transit time between flights and the total time of flight.
     * The time difference between the two cities in India and Singapore is 
     * also taken care of.
     * @param jetSpice Flight, SpiceJet Flight from the selected combination. 
     * @param jetSilk Flight, SilkAir Flight from the selected combination.
     * @param spiceDate Calendar, Boarding Date of SpiceJet Flight.
     * @param silkDate Calendar, Boarding Date of SilkAir Flight.
     * @return ComboFlight, A connection of SpiceJet Flight and a SilkAir Flight through ComboFlight object. 
     */
    public ComboFlight getComboFlightConnection(Flight jetSpice, Flight jetSilk, Calendar spiceDate, Calendar silkDate){
        ComboFlight tempSegment = new ComboFlight(jetSpice, jetSilk, spiceDate, silkDate);
        
        //Calculating Transit Time for SpiceJet and SilkAir Flights
        if(jetSilk.depTime>=jetSpice.arrTime) 
            tempSegment.transitTime = jetSilk.depTime - jetSpice.arrTime;
        else tempSegment.transitTime = 1440 - jetSpice.arrTime + jetSilk.depTime;
        
        //TotalTime = TransitTime + FlightTime
        tempSegment.totalTime = tempSegment.transitTime; 
        if(jetSpice.arrTime>=jetSpice.depTime) 
            tempSegment.totalTime += jetSpice.arrTime - jetSpice.depTime;
        else tempSegment.totalTime += 1440 - jetSpice.depTime + jetSpice.arrTime;
        if(jetSilk.arrTime>=jetSilk.depTime)
            tempSegment.totalTime += jetSilk.arrTime - jetSilk.depTime;
        else tempSegment.totalTime += 1440 - jetSilk.depTime + jetSilk.arrTime;
        
        //Time difference between Indian Standard Time and Singapore Standard Time
        tempSegment.totalTime -= 150;
        return tempSegment;
    }
    /**
     * The method connects an ArrayList of SpiceJets and SilkAir Flights
     * according to the constraints. It checks if the transit time is between two and
     * six hours. It also checks available number of seats on the flights and availability 
     * of the flight on the day.
     * @param src String, Source City of travel.
     * @param seats Integer, Requested number of seats.
     * @param searchedDate Calendar, Searched/Requested Date of travel.
     * @param nextDate Calendar, a day after the searchedDate.
     */
    public void ConnectSpiceSilk(String src, int seats, Calendar searchedDate, Calendar nextDate){
        //For each avialble SpiceJet Flight on searched Date
        for(Flight jetSpice: spiceSegments){
            //For each available SilkAir Flight on searched Date
            for(Flight jetSilk : silkSegments){
                //Destination of SpiceJet Flight must be equal to Source of SilkAir Flight
                if(jetSpice.destination.substring(0,4).equalsIgnoreCase(jetSilk.source.substring(0,4))){
                    //Transit Time between the two flights must be between 120 and 360 minutes
                    if((jetSilk.depTime-jetSpice.arrTime)>=120 && (jetSilk.depTime-jetSpice.arrTime)<=360){
                        //Check if required number of seats is avialble and
                        //SilkAir flight is actually ruuning on the Searched Date by checking remarks
                        if(frs.dataManager.checkSeat(jetSpice, jetSilk, searchedDate, searchedDate, seats) && checkAnomaly(jetSilk, searchedDate)){
                            ComboFlight tempSegment;
                            //Create a ComboFlight and get the transit time and total time
                            tempSegment = getComboFlightConnection(jetSpice, jetSilk, searchedDate, searchedDate);
                            FlightSegment.add(tempSegment);
                        }
                    //If the connecting silk air flight is next day
                    } else if(jetSilk.depTime-jetSpice.arrTime<0){
                        ////Transit Time between the two flights must be between 120 and 360 minutes
                        if(1440-jetSpice.arrTime+jetSilk.depTime>=120 && 1440-jetSpice.arrTime+jetSilk.depTime<=360){
                            //The Silk Air flight runs on next day
                            if(nextDate.compareTo(jetSilk.effFrom)>=0 && nextDate.compareTo(jetSilk.effTill)<=0){
                                if(jetSilk.daysOfWeek.contains(nextDate.get(Calendar.DAY_OF_WEEK))){
                                    if(frs.dataManager.checkSeat(jetSpice, jetSilk, searchedDate, nextDate, seats) && checkAnomaly(jetSilk, nextDate)){
                                        ComboFlight tempSegment;
                                        //Create a ComboFlight and get the transit time and total time
                                        tempSegment = getComboFlightConnection(jetSpice, jetSilk, searchedDate, nextDate);
                                        FlightSegment.add(tempSegment);
                                    }
                                }   
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * The method takes in a SilkAir Flight and the Searched Date and checks the remarks
     * in SilkAir flight to see if the flight is available on the Searched Date.
     * @param silkJet SilkAir Flight.
     * @param searchedDate Date of travel.
     * @return Boolean, If the flight is available on the Date of travel, it returns true, else false.
     */
    public boolean checkAnomaly(Flight silkJet, Calendar searchedDate){
        String remark = silkJet.remark;
        //Four types of remarks are there in Silk Flight File.
        //Two Dates between which the flight runs seperated by a hyphen.
        if(remark.contains("-")){
          //Read both date
          Calendar start = Calendar.getInstance();
          Calendar end = Calendar.getInstance();
          
          int startMonth = DataManager.getMonth(remark.substring(0,3).trim());
          int startDate = Integer.parseInt(remark.substring(3,5).trim());
          start.clear();
          start.set(2016, startMonth, startDate);
          int endMonth;
          int endDate;
          if(remark.length()>12){
             endMonth = DataManager.getMonth(remark.substring(7,11).trim());
             endDate = Integer.parseInt(remark.substring(11).trim());
          } else {
              endMonth = DataManager.getMonth(remark.substring(7,10).trim());
              endDate = Integer.parseInt(remark.substring(10).trim());
          }
          end.clear();
          end.set(2016, endMonth, endDate);
          
          //Check to see if the searched date does not lie between the read date
          if(searchedDate.compareTo(start)<0 || searchedDate.compareTo(end)>0)
              return false;
        }
        //if the string starts with Dis - Change in Effective From Date
        else if(remark.startsWith("Eff")){
            //Read the Date
            Calendar temp = Calendar.getInstance();
            int month = DataManager.getMonth(remark.substring(5,8).trim());
            int date = Integer.parseInt(remark.substring(8).trim());
            temp.clear();
            temp.set(2016, month, date);
            //Check to see if the searched Date is before the read date
            if(searchedDate.compareTo(temp)<0)
                return false;
        }
        //String with several changes
        else if(remark.contains(",")){
            StringTokenizer st =  new StringTokenizer(remark, ",");
            Calendar tempCal = Calendar.getInstance();
            String temp = st.nextToken();
            //Change in Effective Till Date
            int month = DataManager.getMonth(temp.substring(6,9).trim());
            int date = Integer.parseInt(temp.substring(9,11).trim());
            tempCal.clear();
            tempCal.set(2016, month, date);
            if(searchedDate.compareTo(tempCal)>0)
                return false;
            
            //Changes in the exception dates - dates on which the flights donot run
            temp=st.nextToken();
            month = DataManager.getMonth(temp.substring(6,9).trim());
            date = Integer.parseInt(temp.substring(9).trim());
            tempCal.clear();
            tempCal.set(2016, month, date);
            if(searchedDate.compareTo(tempCal)==0)
                return false;
            
            temp=st.nextToken();
            month = DataManager.getMonth(temp.substring(1,4).trim());
            date = Integer.parseInt(temp.substring(4,6).trim());
            tempCal.clear();
            tempCal.set(2016, month, date);
            if(searchedDate.compareTo(tempCal)==0)
                return false;
            
            temp=st.nextToken();
            month = DataManager.getMonth(temp.substring(1,4).trim());
            date = Integer.parseInt(temp.substring(4).trim());
            tempCal.clear();
            tempCal.set(2016, month, date);
            if(searchedDate.compareTo(tempCal)==0)
                return false;        
        }
        //if the string starts with Dis - Change in Effective Till Date
        else if(remark.startsWith("Dis")){
            //Read the Date
            Calendar temp = Calendar.getInstance();
            int month = DataManager.getMonth(remark.substring(6,9).trim());
            int date = Integer.parseInt(remark.substring(9).trim());
            temp.clear();
            temp.set(2016, month, date);
            //Check to see if the searched date is after the read date
            if(searchedDate.compareTo(temp)>0)
                return false;
        }
        return true;
    }
}