package com.phonepe.elevator;

import java.util.*;

import com.phonepe.elevator.db.ElevatorDB;
import com.phonepe.elevator.enums.ElevatorState;

public class Elevator implements Runnable{

    private boolean operating;
    private int id;
    private ElevatorState elevatorState;
    private int currentFloor;
    private NavigableSet<Integer> floorStops;
    public Map<ElevatorState, NavigableSet<Integer>> floorStopsMap;
    public int time = 0;
    private boolean doorOpen;
    
    public Elevator(int id){
        this.id = id;
        setOperating(true);
    }

    public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getId() {
        return id;
    }

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    public boolean isOperating(){
        return this.operating;
    }

    public void setOperating(boolean state){
        this.operating = state;

        if(!state){
            setElevatorState(ElevatorState.MAINTAINANCE);
            this.floorStops.clear();
        } else {
            setElevatorState(ElevatorState.STATIONARY);
            this.floorStopsMap = new LinkedHashMap<ElevatorState, NavigableSet<Integer>>();
            ElevatorDB.INSTANCE.updateElevatorLists(this);
        }

        setCurrentFloor(0);
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void move(){
          
        	/*if(this.dooeOpen){
        		System.out.println("Elevator ID " + this.id + " | Current floor - " + getCurrentFloor() + "Door Closing");
            	this.dooeOpen = false;
            	return;
        	}*/
            Iterator<ElevatorState> iter = floorStopsMap.keySet().iterator();
            
            while(iter.hasNext()){
                elevatorState = iter.next();
                 
                // Get the floors that elevator will pass in the requested direction
                floorStops = floorStopsMap.get(elevatorState);
                iter.remove();
                Integer currFlr = null;
                Integer nextFlr = null;

                // Start moving the elevator
                while (!floorStops.isEmpty()) {

                    if (elevatorState.equals(ElevatorState.UP)) {
                        currFlr = floorStops.pollFirst();
                        nextFlr = floorStops.higher(currFlr);

                    } else if (elevatorState.equals(ElevatorState.DOWN)) {
                        currFlr = floorStops.pollLast();
                        nextFlr = floorStops.lower(currFlr);
                    } else {
                        return;
                    }

                    setCurrentFloor(currFlr);

                    if (nextFlr != null) {
                        // This helps us in picking up any request that might come while we are on the way.
                        generateIntermediateFloors(currFlr, nextFlr);
                    } else {
                        setElevatorState(ElevatorState.STATIONARY);
                        ElevatorDB.INSTANCE.updateElevatorLists(this);
                    }

                    System.out.println("Elevator ID " + this.id + " | Current floor - " + getCurrentFloor() + " | next move - " + getElevatorState());

                    try {
                    	/*System.out.println("Elevator ID " + this.id + " | Current floor - " + getCurrentFloor() + "Door Opening");
                    	this.dooeOpen = true;*/
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
           }


    /**
     * This method helps to generate list of floors that the elevator will
     * either stop or pass by when in motion.
     */
    private void generateIntermediateFloors(int initial, int target){

        if(initial==target){
        	/*System.out.println("Elevator ID " + this.id + " | Current floor - " + getCurrentFloor() + "Door Opening");
        	this.dooeOpen = true;*/
            return;
        }

        if(Math.abs(initial-target) == 1){
            return;
        }

        int n = 1;
        if(target-initial<0){
            // This means with are moving DOWN
            n = -1;
        }

        while(initial!=target){
            initial += n;
            if(!floorStops.contains(initial)) {
                floorStops.add(initial);
            }
        }
    }

    
    public void openDoor() {
    	this.doorOpen = true;
	}
    public void closeDoor() {
    	this.doorOpen = false;
	}

	@Override
    public void run() {
        while(true){
            if(isOperating()){
                move();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }
}
