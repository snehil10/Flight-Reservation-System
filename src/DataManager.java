/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author devans
 */
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DataManager {
    FRSManager mgr;
    /**
     * Constructor for DataManager
     * @param frs link to FRSManager
     */
    public DataManager(FRSManager frs){
        this.mgr = frs;
    }
    /**
     * Reads the database spiceJet.csv to read the flight timings and details. 
     * @param SpiceFile File name of the spiceJet flight
     * @return ArrayList of details of SpiceJet Flight
     */
    public ArrayList<Flight> readSpiceJet(String SpiceFile){
        BufferedReader br = null;
        String line;
        ArrayList<Flight> domFlight = new ArrayList<>();
        try{
            br = new BufferedReader(new FileReader(SpiceFile));
            for(int i=0;i<5;i++) 
                line = br.readLine();
            while((line = br.readLine())!=null){
                StringTokenizer st = new StringTokenizer(line, "|");
                String origin = st.nextToken();
                String dest = st.nextToken();
            
                String days = st.nextToken();
                StringTokenizer daysST= new StringTokenizer(days, ",");
                ArrayList<Integer> frequency = new ArrayList<>();
                while(daysST.hasMoreTokens())
                    frequency.add(DataManager.getDay(daysST.nextToken().trim().toUpperCase().substring(0, 3)));
            
                String flightNO = st.nextToken();
            
                //Departure Time from source
                line = st.nextToken();
                int hour = Integer.parseInt(line.substring(0,2));
                if(hour==12) hour=0;
                int min = Integer.parseInt(line.substring(3,5));
                String AM_PM = line.substring(6,8);
                if(AM_PM.equals("PM")) hour+=12;
                int depTime = hour*60+min;
            
                //Arrival Time at destination
                line = st.nextToken();
                hour = Integer.parseInt(line.substring(0,2));
                if(hour==12) hour=0;
                min = Integer.parseInt(line.substring(3,5));
                AM_PM = line.substring(6,8);
                if(AM_PM.equals("PM")) hour+=12;
                int arrTime = hour*60+min;
            
                String via = st.nextToken();
            
                //Calendar Object for effective from
                String from =st.nextToken();
                int effFromYear = Integer.parseInt("20" + from.substring(7, 9));
                int effFromMonth = DataManager.getMonth(from.substring(3,6));
                int effFromDate = Integer.parseInt(from.substring(0,2));
                Calendar effFrom = Calendar.getInstance();
                effFrom.clear();
                effFrom.set(effFromYear, effFromMonth, effFromDate, 0, 0);
            
                // Calendar Object for Effective Till
                String till = st.nextToken();
                int effTillYear = Integer.parseInt("20" + till.substring(7, 9));
                int effTillMonth = DataManager.getMonth(till.substring(3,6));
                int effTillDate = Integer.parseInt(till.substring(0,2));
                Calendar effTill = Calendar.getInstance();
                effTill.clear();
                effTill.set(effTillYear, effTillMonth, effTillDate, 23, 59);
            
                //System.out.println(origin + " " + dest + " " + flightNO);
                Flight tempFlight = new Flight(flightNO, origin, dest, depTime, arrTime, effFrom, effTill, frequency, via);
                domFlight.add(tempFlight);
            } 
        } catch(FileNotFoundException ex){
            System.out.println("File Not Found!, " + ex);
        }
        catch(IOException ex){
            System.out.println("There was some problem in IO, on reading SpiceJet files. " + ex);
        }
        finally{
            if(br!=null) try {
                br.close();
            } catch (IOException ex) {
               System.out.println("Problem in closing Buffer Reader");
            }
            return domFlight;
        }
    }
    /**
     * Reads the database silkAir.csv to read the flight timings and details. 
     * @param SilkFile File name of the silkAir flight
     * @return ArrayList of details of SilkAir Flight
     */
   public ArrayList<Flight> readSilkAir(String SilkFile){
        BufferedReader br = null;
        String line;
        ArrayList<Flight> intFlight = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(SilkFile));
            for(int i=0;i<3;i++) 
                line = br.readLine();
        
            while((line = br.readLine())!=null){
                StringTokenizer st = new StringTokenizer(line, "|");
                String origin = st.nextToken();
                String dest = "SINGAPORE";
                String days = st.nextToken();
                StringTokenizer daysST= new StringTokenizer(days, ",");
                ArrayList<Integer> frequency = new ArrayList<>();
                while(daysST.hasMoreTokens())
                    frequency.add(DataManager.getDay(daysST.nextToken().toUpperCase().trim()));
            
                String flightNO = st.nextToken();
                String temp = st.nextToken();
            
                int nextDay;
                if(temp.length()>9) nextDay=1;
                else nextDay=0;
               
                int hour = Integer.parseInt(temp.substring(0,2));
                int min = Integer.parseInt(temp.substring(2,4));
                int depTime = hour*60 + min;
                hour = Integer.parseInt(temp.substring(5,7));
                min = Integer.parseInt(temp.substring(7,9));
                int arrTime = hour*60 + min;
            
                String remark;
                if(st.hasMoreTokens()){ 
                    remark = st.nextToken();
                } else remark = "";
                //System.out.println(origin + dest + days + flightNO + " " +  depTime + " " + arrTime + " " + remark);
                Flight tempFlight = new Flight(flightNO, origin, dest, depTime, arrTime, frequency, remark, nextDay);
                intFlight.add(tempFlight);
            }
        } catch(FileNotFoundException ex){
            System.out.println("File Not Found!, " + ex);
        } catch(IOException ex) {
            System.out.println("There was some problem in IO, on reading SilkAir files. " + ex);
        } finally {
            if(br!=null) try {
                br.close();
            } catch (IOException ex) {
               System.out.println("Problem in closing Buffer Reader");
            }
            return intFlight;
        }
    }
   /**
    * Writes the booked file details in bookedSeats.csv
    * @param confirmedFlight Combination of selected spice and silk flight
    * @param name Name of the customer
    * @param PNR PNR of the ticket 
    * @param seats Requested number of seats
    */
   public void writeBookedData(ComboFlight confirmedFlight, String name, Integer PNR, int seats){
       BufferedWriter br = null;
       try {
           SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YY");
           br = new BufferedWriter(new FileWriter("bookedSeats.csv", true));
           br.write(PNR + "|");
           br.write(name + "|");
           br.write(sdf.format(confirmedFlight.spiceDate.getTime()) + "|");
           br.write(confirmedFlight.spiceFlight.flightID + "|");
           br.write(confirmedFlight.spiceFlight.depTime + "|");
           br.write(confirmedFlight.spiceFlight.arrTime + "|");
           br.write(sdf.format(confirmedFlight.silkDate.getTime()) + "|");
           br.write(confirmedFlight.silkFlight.flightID + "|");
           br.write(confirmedFlight.silkFlight.depTime + "|");
           br.write(confirmedFlight.silkFlight.arrTime + "|");
           br.write(seats + "|");
           br.newLine();
           br.flush();
        } catch(FileNotFoundException ex){
            System.out.println("File Not Found!, " + ex);
        } catch(IOException ex) {
            System.out.println("There was some problem in IO, on wrinting in files. " + ex);
        } finally{
           if(br!=null) try {
               br.close();
           } catch (IOException ex) {
               System.out.println("Problem in closing Buffer Reader");
           }
       }
   }
   
   /**
    * Reads the file to get the maximum PNR generated so far, and returns one greater than that.
    * @return Integer, PNR to be used
    */
   public Integer getPNR(){
        BufferedReader  br=null;
        String line;
        Integer PNR = 123456788;
        try {
            br = new BufferedReader(new FileReader("bookedSeats.csv"));
            line = br.readLine();
            while((line=br.readLine())!=null){
                StringTokenizer st = new StringTokenizer(line, "|");
                PNR = Integer.parseInt(st.nextToken());
            }
        } catch(IOException ex){
            System.out.println("Could Not find file bookedSeats.csv");   
        } finally {
            if(br!=null) try {
                br.close();
            } catch (IOException ex) {
                System.out.println("Problem in closing Buffer Reader");
            }
            return ++PNR;
        }
    }
   /**
    * Reads the file bookedSeats.csv to find the number of seats already booked 
    * for a particular flight at a particular date, and sees if there are requested number
    * of seats available
    * @param spiceFlight SpiceJet Flight
    * @param silkFlight SilkAir Flight
    * @param spiceDate Date of SpiceJet Flight
    * @param silkDate Date of SilkAir Flight
    * @param requiredSeats Requested number of seats 
    * @return Boolean - true if requested number of seats are available, false otherwise
    */
   public boolean checkSeat(Flight spiceFlight, Flight silkFlight, Calendar spiceDate, Calendar silkDate, int requiredSeats){
        int reservedSeats = 0;
        int totalSeats = 15;
        BufferedReader  br=null;
        String line;
        try {
            br = new BufferedReader(new FileReader("bookedSeats.csv"));
            line = br.readLine();
            while((line=br.readLine())!=null){
                StringTokenizer st = new StringTokenizer(line, "|");
                String PNR = st.nextToken();
                String name = st.nextToken();
                String spiceJetDate = st.nextToken();
                String spiceJetFlightID = st.nextToken();
                String spiceDepTime = st.nextToken();
                String spiceArrTime = st.nextToken();
                String silkAirDate = st.nextToken();
                String silkAirFlightID = st.nextToken();
                String silkDepTime = st.nextToken();
                String silkArrTime = st.nextToken();
                int seats = Integer.parseInt(st.nextToken());
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YY");
                if(sdf.format(spiceDate.getTime()).equals(spiceJetDate) || sdf.format(silkDate.getTime()).equals(silkAirDate)){
                    if(spiceFlight.flightID.equals(spiceJetFlightID) || silkFlight.flightID.equals(silkAirFlightID)){
                        if(spiceFlight.depTime == Integer.parseInt(spiceDepTime) || silkFlight.depTime == Integer.parseInt(silkDepTime)){
                            reservedSeats += seats;
                        }
                    }
                }
            }
            return totalSeats >= requiredSeats+reservedSeats;
        } catch(IOException ex){
            System.out.println("Could Not find file bookedSeats.csv");   
        } finally {
            if(br!=null) try {
                br.close();
            } catch (IOException ex) {
                System.out.println("Problem in closing Buffer Reader");
            }     
        }
        return false;
    }
   /**
    * Reads the file bookedSeats.csv and writes the first line if not already 
    * written.
    */
   public void readFirst(){
       BufferedReader br = null;
        BufferedWriter bw = null;
       try{
            br = new BufferedReader(new FileReader("bookedSeats.csv"));     
            bw = new BufferedWriter(new FileWriter("bookedSeats.csv", true));
            if (br.readLine() == null) {
                    bw.write("PNR|NAME|SpiceDate|SpiceID|SpiceDepTime|SpiceArrTime|SilkDate|SilkID|SilkDepTime|SilkArrTime|Seats|");
                    bw.newLine();
                    bw.flush();
                } 
            } catch(FileNotFoundException ex){
                System.out.println("BookedSeats.csv not found");
            } catch(IOException ex){
                System.out.println("IO Error in writing first line in BookedSeats " + ex);
            } finally{
                try {
                if(br!=null) br.close();     
                if(bw!=null) bw.close();
                } catch (IOException ex) {
                      System.out.println("Problem in closing Buffer Reader");
                }
            }
   }
   /**
    * The method returns an Integer for the day of a week.
    * @param s First 3 letters of day of a week
    * @return Integer - 1 for SUN, 2 for MON and so on.
    */
   public static int getDay(String s){
       int day=0;
       switch(s){
           case "SUN": day=1;break;
           case "MON": day=2;break;
           case "TUE": day=3;break;
           case "WED": day=4;break;
           case "THU": day=5;break;
           case "FRI": day=6;break;
           case "SAT": day=7;break;
       }
       return day;
   }
   /**
    * The method returns integer for month you pass.
    * @param mon first three letters of month
    * @return Integer for a month - 0 for Jan, 1 for Feb and so on
    */
   public static int getMonth(String mon){
        int month=0;
        switch(mon){
            case "JAN": month=0; break;
            case "FEB": month=1; break;
            case "MAR": month=2; break;
            case "APR": month=3; break;
            case "MAY": month=4; break;
            case "JUN": month=5; break;
            case "JUL": month=6; break;
            case "AUG": month=7; break;
            case "SEP": month=8; break;
            case "OCT": month=9; break;
            case "NOV": month=10; break;
            case "DEC": month=11; break;
        }
        return month;
    }
}