package com.phonepe.elevator;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import com.phonepe.elevator.db.ElevatorDB;
import com.phonepe.elevator.dto.ElevatorRequest;
import com.phonepe.elevator.enums.ElevatorState;

public final class ElevatorController {
	ElevatorDB db = ElevatorDB.INSTANCE;
	
    /**
     * Select an elevator from the pool of operational elevators that can serve the
     * the request optimally
     * @param elevatorRequest  Represents the request for an elevator
     * @return Selected Elevator
     */
    public synchronized Elevator selectElevator(ElevatorRequest elevatorRequest) {
        Elevator elevator = null;
        ElevatorState elevatorState = getRequestedElevatorDirection(elevatorRequest);
        int requestedFloor = elevatorRequest.getRequestFloor();
        int targetFloor = elevatorRequest.getTargetFloor();
        elevator = findElevator(elevatorState, requestedFloor, targetFloor);
        // So that elevators can start moving again.
        notifyAll();
        return elevator;


    }  

    private  ElevatorState getRequestedElevatorDirection(ElevatorRequest elevatorRequest){
        ElevatorState elevatorState = null;
        int requestedFloor = elevatorRequest.getRequestFloor();
        int targetFloor = elevatorRequest.getTargetFloor();

        if(targetFloor - requestedFloor > 0){
            elevatorState = ElevatorState.UP;
        } else {
            elevatorState = ElevatorState.DOWN;
        }

        return elevatorState;
    }

    /**
     * Internal method to select an elevator and generate UP and/or DOWN paths for it.
     * @param elevatorState UP, DOWN or STATIONARY
     * @param requestedFloor Floor number where request is originating from
     * @param targetFloor Floor number where user wants to go
     * @return selected elevator
     */
    private  Elevator findElevator(ElevatorState elevatorState, int requestedFloor, int targetFloor) {
        Elevator elevator = null;

        // Data structure to hold distance of eligible elevators from the request floor
        // The keys represent the current distance of an elevator from request floor
        TreeMap<Integer, Integer> sortedKeyMap = new TreeMap<Integer, Integer>();
        if(elevatorState.equals(ElevatorState.UP)){

            // Let's go over all elevators that are either going UP or are STATIONARY
            for(Map.Entry<Integer, Elevator> elvMap : db.upMovingMap.entrySet()){
                Elevator elv = elvMap.getValue();
                Integer distance = requestedFloor - elv.getCurrentFloor();
                if(distance < 0 && elv.getElevatorState().equals(ElevatorState.UP)){
                    // No point selecting these elevators. They have already passed by our request floor
                    continue;
                } else {
                    sortedKeyMap.put(Math.abs(distance), elv.getId());
                }
            }

            if(!sortedKeyMap.isEmpty()){
            Integer selectedElevatorId = sortedKeyMap.firstEntry().getValue();
            elevator = db.upMovingMap.get(selectedElevatorId);
            }


        } else if(elevatorState.equals(ElevatorState.DOWN)){
            // Let's go over all elevators that are either going DOWN or are STATIONARY
            for(Map.Entry<Integer, Elevator> elvMap : db.downMovingMap.entrySet()){
                Elevator elv = elvMap.getValue();
                Integer distance = elv.getCurrentFloor() - requestedFloor;
                if(distance < 0 && elv.getElevatorState().equals(ElevatorState.DOWN)){
                    // No point selecting these elevators. They have already passed by our requested floor
                    continue;
                } else {
                    sortedKeyMap.put(Math.abs(distance), elv.getId());
                }
            }
            if(!sortedKeyMap.isEmpty()){
            Integer selectedElevatorId = sortedKeyMap.firstEntry().getValue();
            elevator = db.downMovingMap.get(selectedElevatorId);
            }

        }

      if(elevator != null){
        ElevatorRequest newRequest = new ElevatorRequest(elevator.getCurrentFloor(), requestedFloor);
        ElevatorState elevatorDirection = getRequestedElevatorDirection(newRequest);

        // helpful if we are moving in opposite direction to than that of request
        ElevatorRequest newRequest2 = new ElevatorRequest(requestedFloor, targetFloor);
        ElevatorState elevatorDirection2 = getRequestedElevatorDirection(newRequest2);

        NavigableSet<Integer> floorSet = elevator.floorStopsMap.get(elevatorDirection);
        if (floorSet == null) {
            floorSet = new ConcurrentSkipListSet<Integer>();
        }

        floorSet.add(elevator.getCurrentFloor());
        floorSet.add(requestedFloor);
        elevator.floorStopsMap.put(elevatorDirection, floorSet);

        NavigableSet<Integer> floorSet2 = elevator.floorStopsMap.get(elevatorDirection2);
        if (floorSet2 == null) {
            floorSet2 = new ConcurrentSkipListSet<Integer>();
        }
        floorSet2.add(requestedFloor);
        floorSet2.add(targetFloor);
        elevator.floorStopsMap.put(elevatorDirection2, floorSet2);
      }
        return elevator;
    }


    /**
     * update the state of elevator as soon as it changes the direction
     */
   

    
}
