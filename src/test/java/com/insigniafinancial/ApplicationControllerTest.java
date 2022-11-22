package com.insigniafinancial;

import com.insigniafinancial.component.Direction;
import com.insigniafinancial.controller.ApplicationController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationControllerTest {
    private ApplicationController applicationController;

    @Before
    public void setup() {
        applicationController = new ApplicationController();
    }

    @Test
    public void testProcessCommandPlaceShouldInitARobotWithoutReturningMessageForValidCommand() {
        String command = "PLACE 0,1,NORTH";
        String message = applicationController.processCommand(command);
        assertEquals(1, (long) applicationController.getRobotCount());
        assertEquals(1, applicationController.getRobots().size());
        assertNotNull(applicationController.getActiveRobot());
        assertEquals(1, (long) applicationController.getActiveRobot().getId());
        assertEquals("Robot 1", applicationController.getActiveRobot().getName());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.NORTH, applicationController.getActiveRobot().getDirection());
        assertNull (message);
    }

    @Test
    public void testProcessCommandPlaceShouldNotInitARobotAndReturnMessageForInvalidCommand() {
        String command = "PLACE 1,1,1,NORTH";
        String message = applicationController.processCommand(command);
        assertNotEquals(2, (long) applicationController.getRobotCount());
        assertNotEquals(2, applicationController.getRobots().size());
        assertNotNull(message);
    }

    @Test
    public void testSetActiveRobotShouldSetTheSpecifiedRobotWithoutReturningMessageForValidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        applicationController.processCommand("PLACE 2,3,SOUTH");
        String command = "ROBOT 1";
        String message = applicationController.processCommand(command);
        assertNotNull(applicationController.getActiveRobot());
        assertEquals(1, (long) applicationController.getActiveRobot().getId());
        assertEquals("Robot 1", applicationController.getActiveRobot().getName());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.NORTH, applicationController.getActiveRobot().getDirection());
        assertNull (message);
    }

    @Test
    public void testSetActiveRobotShouldNotSetRobotAndReturnMessageForInvalidCommand() {
        String command = "ROBOT 11";
        String message = applicationController.processCommand(command);
        assertNull(applicationController.getActiveRobot());
        assertNotNull (message);
    }

    @Test
    public void testProcessCommandMoveShouldMoveTheRobotWithoutReturningMessageForValidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "MOVE";
        String message = applicationController.processCommand(command);
        assertNotNull(applicationController.getActiveRobot());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(2, (long) applicationController.getActiveRobot().getPositionY());
        assertNull(message);
    }

    @Test
    public void testProcessCommandMove7TimesFrom0AlongXAxisShouldRemainXAxis5WithoutReturningMessage() {
        applicationController.processCommand("PLACE 0,0,EAST");
        String command = "MOVE";
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        String message = applicationController.processCommand(command);
        assertEquals(5, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.EAST, applicationController.getActiveRobot().getDirection());
        assertNull(message);
    }

    @Test
    public void testProcessCommandMove7TimesFrom5AlongXShouldRemainXAxis0WithoutReturningMessage() {
        applicationController.processCommand("PLACE 5,5,WEST");
        String command = "MOVE";
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        String message = applicationController.processCommand(command);
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(5, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.WEST, applicationController.getActiveRobot().getDirection());
        assertNull(message);
    }

    @Test
    public void testProcessCommandMove7TimesFrom0AlongYAxisShouldRemainYAxis5WithoutReturningMessage() {
        applicationController.processCommand("PLACE 0,0,NORTH");
        String command = "MOVE";
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        String message = applicationController.processCommand(command);
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(5, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.NORTH, applicationController.getActiveRobot().getDirection());
        assertNull(message);
    }

    @Test
    public void testProcessCommandMove7TimesFrom5AlongYAxisShouldRemainYAxis0WithoutReturningMessage() {
        applicationController.processCommand("PLACE 5,5,SOUTH");
        String command = "MOVE";
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        applicationController.processCommand(command);
        String message = applicationController.processCommand(command);
        assertEquals(5, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.SOUTH, applicationController.getActiveRobot().getDirection());
        assertNull(message);
    }

    @Test
    public void testProcessCommandMoveShouldNotMoveTheRobotAndReturnErrorMessageForInvalidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "MOVIE";
        String message = applicationController.processCommand(command);
        assertNotNull(applicationController.getActiveRobot());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertNotNull(message);
    }

    @Test
    public void testProcessCommandReportShouldReturnRobotInfoMessageForValidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "REPORT";
        String message = applicationController.processCommand(command);
        assertEquals("Robot 1: 0,1,NORTH", message);
    }

    @Test
    public void testProcessCommandReportShouldReturnErrorMessageForValidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "RREPORT";
        String message = applicationController.processCommand(command);
        assertEquals(command.concat(applicationController.getMESSAGE_INVALID_COMMAND()), message);
    }

    @Test
    public void testProcessCommandDirectionShouldChangeTheRobotDirectionWithoutReturningMessageForValidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "SOUTH";
        String message = applicationController.processCommand(command);
        assertNotNull(applicationController.getActiveRobot());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.SOUTH, applicationController.getActiveRobot().getDirection());
        assertNull(message);
    }

    @Test
    public void testProcessCommandDirectionShouldNotChangeDirectionAndReturnMessageForInvalidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "SSOUTH";
        String message = applicationController.processCommand(command);
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.NORTH, applicationController.getActiveRobot().getDirection());
        assertNotNull(message);
    }

    @Test
    public void testProcessCommandRotationShouldChangeTheRobotDirectionWithoutReturningMessageForValidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "LEFT";
        String message = applicationController.processCommand(command);
        assertNotNull(applicationController.getActiveRobot());
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.WEST, applicationController.getActiveRobot().getDirection());
        assertNull(message);
    }

    @Test
    public void testProcessCommandRotationShouldNotChangeDirectionAndReturnMessageForInvalidCommand() {
        applicationController.processCommand("PLACE 0,1,NORTH");
        String command = "LLEFT";
        String message = applicationController.processCommand(command);
        assertEquals(0, (long) applicationController.getActiveRobot().getPositionX());
        assertEquals(1, (long) applicationController.getActiveRobot().getPositionY());
        assertEquals(Direction.NORTH, applicationController.getActiveRobot().getDirection());
        assertNotNull(message);
    }
}