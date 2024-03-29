package com.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * Creates and loads an AP table for a specified region. Once loaded, the main
 * function of this class is to map given AP BSSID's into an ID given by the AP
 * table file. New AP's can be automatically added to the table which will
 * increment the ID by 1.
 * 
 * @author James Humphrey
 * @author Kwaku Farkye
 */
public class APTable
{
   /**
    * Creates and initializes an empty AP table.
    */
   public APTable( boolean debugFile )
   {
      // Create and initialize the AP table to 0.
      aps = new ArrayList< AccessPoint >();
      aps.clear();

      tableName = "";
//      writeDebugFile = debugFile;
   }

   /**
    * Adds a new access point to the table.
    * 
    * @param newAp
    *           The new AP to be added to the table
    */
   public void addAPToTable( AccessPoint newAp )
   {
      // Get the ID number of the last entry in the table (should be the highest value).
      int topID = 0;
      ;

      // The first AP added should always begin with a value of 1.
      if( aps.size() > 0 )
      {
         topID = aps.get( aps.size() - 1 ).getID();
      }

      // Increment the next ID so it is unique to the table.
      newAp.setID( topID + 1 );

      // Add the new AP to the loaded table.
      aps.add( newAp );

      // Copy the new AP to the table file.
//      WriteFile write = new WriteFile( tableName, true );
      tableLog.save( newAp.getID() + " " + newAp.getBSSID() + "\n" );

//      tableLog.toast( "Added new AP to table with id " + newAp.getID() + " and BSSID " + newAp.getBSSID() );
      
      // Check if we want to write out more information about the AP to another debug file.
//      if( writeDebugFile )
//      {
//         WriteFile debugWrite = new WriteFile( tableName + "_debug.txt", true );
//         debugWrite.writeToFile( newAp.toString()  );
//      }
   }

   /**
    * Iterates through each AP in the provided list and maps its BSSID to the
    * corresponding ID value in the AP table file.
    * 
    * @param apList
    *           The list of access points to map
    * @param autoIncludeNew
    *           Indicates if you want to automatically include each AP into the
    *           table file if no mapping is found (new AP)
    */
   public void mapAPsToID( ArrayList< AccessPoint > apList, boolean autoIncludeNew )
   {
     System.out.println("hey were areadf");
     autoIncludeNew = true;
      for( int i = 0; i < apList.size(); i++ )
      {
         for( int j = 0; j < aps.size(); j++ )
         {
            if( apList.get( i ).getBSSID().equals( aps.get( j ).getBSSID() ) )
            {
               // Set the ID of the mapped AP in the list.
               apList.get( i ).setID( aps.get( j ).getID() );
               System.out.println( "Value mapped with id " + apList.get( i ).getID() + " and BSSID "
                     + apList.get( i ).getBSSID() + " and RSSI " + apList.get( i ).getRSSI() );
               break;
            }
         }
         System.out.println("id is " + apList.get( i ).getID());
         // Check if the AP was not found in the table.
         if( apList.get( i ).getID() < 0 )
         {
            System.out.println( "The AP was not found in the table" );

            // Check if the AP should be added to the table.
            if( autoIncludeNew )
            {
               addAPToTable( apList.get( i ) );
            }
            else
            {
               // Prompt the user if he wants the program to save the access point to the table.///
               addAPToTable( apList.get( i ) );
            }
         }
      }
   }

   /**
    * Loads each registered access point from the AP table into memory. This
    * function assumes the data is in the correct format as it does no error
    * checking on format.
    * 
    * @param tableName
    *           The file name of the AP table to load from
    */
   public void loadTable( String tableName )
   {
      this.tableName = tableName;
      String data;
      
      data = tableLog.load();

      // Create a scanner on the input stream so we can parse the file data.
      Scanner tabScan = new Scanner( data );

      // Clear the previously allocated AP table.
      aps.clear();
      
      // Loop through each line in the file which contains AP mapping data.
      while( tabScan.hasNext() )
      {
         // Create a new access point that we will be adding to our table.
         AccessPoint newAp = new AccessPoint();

         // Read the id number used for mapping APs.
         newAp.setID( tabScan.nextInt() );

         // Read the unique BSSID of the AP that will be used to map to an id value.
         newAp.setBSSID( tabScan.next() );

         // Add the AP to the table.
         aps.add( newAp );
      }

      // Output the loaded AP table for reference.
      System.out.println( "Loaded AP table:\n" + this + "\n" );      

   }

   /**
    * Create a new file with the specified name
    * 
    * @param fileName
    * 		The File that will be created
    */
   public void createFile(String fileName) {
       File newFile = new File( fileName );
       try {
		  newFile.createNewFile();
       } catch (IOException e) {
		  System.out.println("Couldnt create new file. Exiting program");
		  System.exit(0);
       }
   }
   
   /**
    * Accessor method for the list of access points loaded in from the table.
    * 
    * @return The array list of known access points.
    */
   public ArrayList< AccessPoint > getAPTable()
   {
      return aps;
   }

   /**
    * Outputs the ID and BSSID of every AP in the table for each line.
    */
   public String toString()
   {
      String tableStr = "";
      for( int i = 0; i < aps.size(); i++ )
      {
         tableStr += aps.get( i ).getID() + " " + aps.get( i ).getBSSID() + "\n";
      }

      return tableStr;
   }

   private ArrayList< AccessPoint > aps; // A list of APs stored in the table.
   private String tableName; // The file name of the AP table that is loaded.
   boolean writeDebugFile; // Indicates if we want to write another debug file which will include more information about the APs.
   private boolean multiAttempts = false; //Flag to see if there were multiple attempts of loading the access point table.
   private AndroidLog tableLog = new AndroidLog("example3.txt");
}
