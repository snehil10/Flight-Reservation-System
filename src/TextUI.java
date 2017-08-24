
import java.util.*;
import java.io.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author subham
 */
public class TextUI {
    public String source;
    public String destination;
    public Calendar date;
    public Integer seats;
    Integer ChosenIndex;
    String name;
    Integer PNR;
    
    FRSManager frs;
    public TextUI(FRSManager frs){
        this.frs = frs;
        this.date = Calendar.getInstance();
    }
    public void showtext() {
        Scanner sc=new Scanner(System.in);
        
        System.out.println("Welcome to the Flight Reservation System!");
        System.out.println("=========================================");
        System.out.println("Please enter Date of Travel (dd/mm/yyyy):");
       
        String dateObj = sc.next();
        int day = Integer.parseInt(dateObj.substring(0,2));
        int month = Integer.parseInt(dateObj.substring(3,5));
        int year = Integer.parseInt(dateObj.substring(6));
        date.set(year, month-1, day);
        date.set(Calendar.HOUR_OF_DAY, date.getActualMinimum(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, date.getActualMinimum(Calendar.MINUTE));
        date.set(Calendar.SECOND, date.getActualMinimum(Calendar.SECOND));
        date.set(Calendar.MILLISECOND, date.getActualMinimum(Calendar.MILLISECOND));
        
        System.out.println("Please enter Source City (DELHI, MUMBAI or PUNE):");
        source = sc.next().toUpperCase();
        System.out.print("Please enter Number of Tickets (1 to 10): ");
        seats = sc.nextInt();
        System.out.println("Destination City is SINGAPORE");
     
        System.out.println("You have entered:");
        System.out.println("Date of Travel - " + day + "/" + month + "/" + year);
        System.out.println("Source City - " + source);
        System.out.println("Destination City is SINGAPORE");        
        System.out.println("Number of Tickets - " + seats);
        
        frs.FlightSegment = frs.searchManager.SearchFlights(source, seats, date);
        
        System.out.println("Search Results");
        System.out.println("==============");
        
        for (int i=0;i<frs.FlightSegment.size();i++) {
            System.out.println("----" +(i+1) + "----");
            System.out.print(frs.FlightSegment.get(i).spiceFlight.source + " " + frs.FlightSegment.get(i).spiceFlight.destination + " " + frs.FlightSegment.get(i).spiceFlight.flightID + " ");
            System.out.println("Departure: " + frs.FlightSegment.get(i).spiceFlight.depTime/60 + ":" + frs.FlightSegment.get(i).spiceFlight.depTime%60 + " Arrival: " + frs.FlightSegment.get(i).spiceFlight.arrTime/60 + ":"+ frs.FlightSegment.get(i).spiceFlight.arrTime%60);
            
            System.out.print(frs.FlightSegment.get(i).silkFlight.source + " " + frs.FlightSegment.get(i).silkFlight.destination + " " + frs.FlightSegment.get(i).silkFlight.flightID + " ");
            System.out.println("Departure: " + frs.FlightSegment.get(i).silkFlight.depTime/60 + ":" + frs.FlightSegment.get(i).silkFlight.depTime%60 + " Arrival: " + frs.FlightSegment.get(i).silkFlight.arrTime/60 + ":"+ frs.FlightSegment.get(i).silkFlight.arrTime%60);
                        
            
            System.out.println(("Transit Time: " + frs.FlightSegment.get(i).transitTime/60) + " hours " + (frs.FlightSegment.get(i).transitTime%60) + " minitues");
            System.out.println(("Total Time: " + frs.FlightSegment.get(i).totalTime/60) + " hours " + (frs.FlightSegment.get(i).totalTime%60) + " minitues");
            System.out.println();   
        }
        
        System.out.println("Please enter your selection (Input the number):");
        ChosenIndex = sc.nextInt();
        System.out.println("Please enter your Name:");
        name = sc.next();
        
        System.out.println("You have selected ...");
        System.out.print(frs.FlightSegment.get(ChosenIndex-1).spiceFlight.source + " " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.destination + " " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.flightID + " ");
        System.out.println("Departure: " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.depTime/60 + ":" + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.depTime%60 + " Arrival: " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.arrTime/60 + ":"+ frs.FlightSegment.get(ChosenIndex-1).spiceFlight.arrTime%60);
            
        System.out.print(frs.FlightSegment.get(ChosenIndex-1).silkFlight.source + " " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.destination + " " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.flightID + " ");
        System.out.println("Departure: " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.depTime/60 + ":" + frs.FlightSegment.get(ChosenIndex-1).silkFlight.depTime%60 + " Arrival: " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.arrTime/60 + ":"+ frs.FlightSegment.get(ChosenIndex-1).silkFlight.arrTime%60);
                            
        System.out.println(("Transit Time: " + frs.FlightSegment.get(ChosenIndex-1).transitTime/60) + " hours " + (frs.FlightSegment.get(ChosenIndex-1).transitTime%60) + " minitues");
        System.out.println(("Total Time: " + frs.FlightSegment.get(ChosenIndex-1).totalTime/60) + " hours " + (frs.FlightSegment.get(ChosenIndex-1).totalTime%60) + " minitues");
        System.out.println();
        
        PNR =frs.dataManager.getPNR();
        frs.dataManager.writeBookedData(frs.FlightSegment.get(ChosenIndex),name,PNR, seats);
        System.out.println("Booking");
        System.out.println("=======");
        
        System.out.println("Congratulation! Your Flight Reservation was successfull");
        System.out.println("PNR No: " + PNR);
        System.out.println("Name: " + name);
        System.out.println("No. of booked Seats: " + seats);
        System.out.print(frs.FlightSegment.get(ChosenIndex-1).spiceFlight.source + " " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.destination + " " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.flightID + " ");
        System.out.println("Departure: " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.depTime/60 + ":" + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.depTime%60 + " Arrival: " + frs.FlightSegment.get(ChosenIndex-1).spiceFlight.arrTime/60 + ":"+ frs.FlightSegment.get(ChosenIndex-1).spiceFlight.arrTime%60);
            
        System.out.print(frs.FlightSegment.get(ChosenIndex-1).silkFlight.source + " " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.destination + " " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.flightID + " ");
        System.out.println("Departure: " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.depTime/60 + ":" + frs.FlightSegment.get(ChosenIndex-1).silkFlight.depTime%60 + " Arrival: " + frs.FlightSegment.get(ChosenIndex-1).silkFlight.arrTime/60 + ":"+ frs.FlightSegment.get(ChosenIndex-1).silkFlight.arrTime%60);
                            
        System.out.println(("Transit Time: " + frs.FlightSegment.get(ChosenIndex-1).transitTime/60) + " hours " + (frs.FlightSegment.get(ChosenIndex-1).transitTime%60) + " minitues");
        System.out.println(("Total Time: " + frs.FlightSegment.get(ChosenIndex-1).totalTime/60) + " hours " + (frs.FlightSegment.get(ChosenIndex-1).totalTime%60) + " minitues");
        System.out.println();
        
        
        System.out.println("Have a great Journey!");
        System.out.println("Goodbye, please visit us again.");
    }
}
