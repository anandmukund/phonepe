package com.phonepe.elevator.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.phonepe.elevator.Elevator;
import com.phonepe.elevator.enums.ElevatorState;

public enum ElevatorDB {

	INSTANCE;
	// All the UP moving elevators
    public  Map<Integer, Elevator> upMovingMap = new HashMap<Integer, Elevator>();
    
    public int floor;
    // All the DOWN moving elevators
    public Map<Integer, Elevator> downMovingMap = new HashMap<Integer, Elevator>();
    // STATIONARY elevators are part of UP and DOWN map both.
    public  List<Elevator> elevatorList =null;
    public void init(int floor,int elevator){
 	   elevatorList = new ArrayList<Elevator>(elevator);
 	    this.floor = floor;
 	    initializeElevators(elevator);
    }
    private  void initializeElevators(int elevators){
        for(int i=0; i<elevators; i++){
            Elevator elevator = new Elevator(i+1);
            Thread t = new Thread(elevator);
            t.start();
            elevatorList.add(elevator);
        }
    }
    public void updateElevatorLists(Elevator elevator){
        if(elevator.getElevatorState().equals(ElevatorState.UP)){
            upMovingMap.put(elevator.getId(), elevator);
            downMovingMap.remove(elevator.getId());
        } else if(elevator.getElevatorState().equals(ElevatorState.DOWN)){
        	downMovingMap.put(elevator.getId(), elevator);
        	upMovingMap.remove(elevator.getId());
        } else if (elevator.getElevatorState().equals(ElevatorState.STATIONARY)){
        	upMovingMap.put(elevator.getId(), elevator);
        	downMovingMap.put(elevator.getId(),elevator);
        } else if (elevator.getElevatorState().equals(ElevatorState.MAINTAINANCE)){
        	upMovingMap.remove(elevator.getId());
        	downMovingMap.remove(elevator.getId());
        }
    }
}
