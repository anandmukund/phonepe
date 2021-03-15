package com.phonepe.elevator.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.phonepe.elevator.Elevator;
import com.phonepe.elevator.ElevatorController;
import com.phonepe.elevator.db.ElevatorDB;
import com.phonepe.elevator.dto.ElevatorRequest;

public class ElevatorMain {

  
    public static void main(String [ ] args){
    	 ElevatorDB db = ElevatorDB.INSTANCE;
         
    	 System.out.println("Enter number of elevator and floor seperated by space like 4 12"); 
         BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in)); 
       
             // Reading data using readLine 
             String requ = null;
				try {
					requ = reader1.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
       
         String[] req = requ.split(" ");
         int floor = Integer.valueOf(req[1]);
         int elevator = Integer.valueOf(req[0]);
         db.init(floor, elevator);
        while(true) {
            System.out.println("Enter sorce and target floor with single space like 1 2");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
          
                // Reading data using readLine 
                String requser = null;
				try {
					requser = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
          
            String[] requests = requser.split(" ");
            if(requests.length == 1 && requser.equalsIgnoreCase("0")){
            	System.out.println("Elevator Switching Off");
            	System.exit(0);
            }
            int requestFloor = Integer.valueOf(requests[0]);
            int targetFloor = Integer.valueOf(requests[1]);
            if(requests.length != 2 || requestFloor < 0 || requestFloor > db.floor ||targetFloor < 0 || targetFloor > db.floor ){
            	System.out.println("#####INVALID REQUEST #########");
            }  else {
                ElevatorRequest elevatorRequest = new ElevatorRequest(requestFloor, targetFloor);
                Elevator elevator1 = elevatorRequest.submitRequest();
            }

            }

        }

    
}
