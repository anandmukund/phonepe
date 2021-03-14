package com.phonepe.elevator.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.phonepe.elevator.Elevator;
import com.phonepe.elevator.ElevatorController;
import com.phonepe.elevator.dto.ElevatorRequest;

public class ElevatorMain {

    private static ElevatorController elevatorController;
    private static Thread elevatorControllerThread;

    public static void main(String [ ] args){
    	 elevatorController = ElevatorController.getInstance();
         
         elevatorControllerThread = new Thread(elevatorController);
      
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
        
         elevatorController.init(floor, elevator);
         elevatorControllerThread.start();
        while(true) {
            System.out.println("Enter sorce and target floor with single space like 1 2");
            Scanner input = new Scanner(System.in);
             
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
            int requestFloor = Integer.valueOf(requests[0]);
            int targetFloor = Integer.valueOf(requests[1]);
            if(requests.length != 2 || requestFloor < 0 || requestFloor > elevatorController.getFloor() ||targetFloor < 0 || targetFloor > elevatorController.getFloor() ){
            	System.out.println("#####INVALID REQUEST #########");
            }  else {
                ElevatorRequest elevatorRequest = new ElevatorRequest(requestFloor, targetFloor);
                Elevator elevator1 = elevatorRequest.submitRequest();
            }

            }

        }

    
}
