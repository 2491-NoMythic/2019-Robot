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
public class Variables {
    // PID
    public static double proportionalRotate = 0.01;
    public static double integralRotate = 0;
    public static double derivativeRotate = 0;

    public static boolean useGyroPID;

    // debug Mode
    public static boolean debugMode = false;

    //Acceleration Number
    public static double linearAccelerationValue = 0.05;

    /*
    The degree of acceleration where 1st degree is linear
    */
    public static int degreeofaccel = 1;

    //Controllers
    public static boolean isTwoStick = false;
}
