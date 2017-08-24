/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author subham
 */
import java.util.*;

public class FRSManager {
    /**
     * Declarations
     */
    public DataManager dataManager;
    public SearchManager searchManager;
    public BookingManager bookingManager;
    public DisplayManager displayManager;
    
    public ArrayList<Flight> silkair;
    public ArrayList<Flight> spicejet;
    public ArrayList<ComboFlight> FlightSegment;
    /**
     * Main loop
     * @param args File names 
     */
    public static void main(String[] args){
        FRSManager manager = new FRSManager();
        manager.readPreliminary(manager);
        if(manager.spicejet.isEmpty()==false && manager.silkair.isEmpty()==false)
            manager.initializeOthers(manager);
    }
    /**
     * Reads the spiceFile and silkAir file. 
     * @param manager Instance of FRSManager.
     * @param args - File Name.
     */
    public void readPreliminary(FRSManager manager){
        manager.dataManager = new DataManager(manager);
        manager.dataManager.readFirst();
        manager.spicejet = manager.dataManager.readSpiceJet("spicejet.csv"); //args[0]
        manager.silkair = manager.dataManager.readSilkAir("silkair.csv"); //args[1]
    }
    /**
     * Show Input Screen
     * @param manager - Link to FRSManager
     */
    public void initializeOthers(FRSManager manager){
        FlightSegment = new ArrayList<>();
        
        manager.displayManager = new DisplayManager(manager);
        manager.searchManager = new SearchManager(manager);
        manager.bookingManager = new BookingManager(manager);
        
        manager.displayManager.showInputScreen();
    }
}
