package com.phonepe.elevator.test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.phonepe.elevator.Elevator;
import com.phonepe.elevator.ElevatorController;
import com.phonepe.elevator.db.ElevatorDB;
import com.phonepe.elevator.dto.ElevatorRequest;

import static org.junit.Assert.*;

public class ElevatorRequestTest {

    private ElevatorDB elevatorController;
  
    @Before
    public void setUp() throws Exception {

        elevatorController = ElevatorDB.INSTANCE;
        elevatorController.init(16,15);

    }

    @After
    public void tearDown() throws Exception {
       /* if(!elevatorController.isStopController()) {
            elevatorController.setStopController(true);
        }*/

    }

    @Test
    public void request1() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 2);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(5000);
        assertEquals(2, elevator.getCurrentFloor());
    }

    @Test
    public void request2() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 1);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(3000);
        ElevatorRequest elevatorRequest1 = new ElevatorRequest(3, 5);
        elevator = elevatorRequest1.submitRequest();
        Thread.sleep(10000);
        assertEquals(5, elevator.getCurrentFloor());
    }

    @Test
    public void request3() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 4);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(5000);
        ElevatorRequest elevatorRequest1 = new ElevatorRequest(0, 1);

        elevator = elevatorRequest1.submitRequest();
        Thread.sleep(3000);
        assertEquals(1, elevator.getCurrentFloor());
    }

}