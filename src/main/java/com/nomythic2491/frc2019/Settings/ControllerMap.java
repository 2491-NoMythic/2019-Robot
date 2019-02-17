/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.Settings;

/**
 * Add your docs here.
 */
public class ControllerMap {
    //Controllers
    public static final int driveControllerLeft = 0;
    public static final int driveControllerRight = 1;
    public static final int operatorController = 0;
    
    //Left drive controller
    public static final int driveMainAxisLeft = 1;

    //Right drive controller
    public static final int driveMainAxisRight = 1;

    //Operator controller
    public static final int hatchButton = 4; //Y
    public static final int manualCargoPickup = 2; //A
    public static final int manualCargoOutput = 3; //B
    public static final int toggleElevation = 7; //Left trigger
    public static final int resetClimber = 9; //Back
    
    public static final int autoClimb = 10; //Start
    public static final int tipBoxForward = 6; //Right bumper
    public static final int tipBoxUp = 5; //Left bumper
    public static final int tipBoxBack = 1; //X
    public static final int deployLineupPins = 8; //Right trigger

    //Operator General
    public static final int cargoIntakeAxis = 1;
    public static final int cargoOutputAxis = 2;
}
