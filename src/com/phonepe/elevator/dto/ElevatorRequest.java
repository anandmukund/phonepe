package com.phonepe.elevator.dto;

import com.phonepe.elevator.Elevator;
import com.phonepe.elevator.ElevatorController;

public class ElevatorRequest {
    private int requestFloor;
    private int targetFloor;

    public ElevatorRequest(int requestFloor, int targetFloor){
        this.requestFloor = requestFloor;
        this.targetFloor = targetFloor;
    }

    public int getRequestFloor() {
        return requestFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public Elevator submitRequest(){
        return ElevatorController.getInstance().selectElevator(this);
    }
}
