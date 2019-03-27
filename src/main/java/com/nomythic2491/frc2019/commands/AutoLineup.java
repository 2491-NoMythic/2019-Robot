/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.ControlBoard;
import com.nomythic2491.frc2019.Robot;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.command.Command;

public class AutoLineup extends Command {
  DriveSignal driveSignal; 
  double currentAngle;
  double kP, kI, kD, integral, previousError, error, derivative, out;
  ControlBoard mBoard;
  boolean endEarly;
  public AutoLineup() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    kP = .025;//.0370
    kI = 0.04;
    kD = 0;
    integral = 0;
    previousError = 0;
    System.out.println("I Initilized");
    mBoard = ControlBoard.getInstance();
    endEarly = false; 
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    currentAngle = Robot.drivetrain.getLimelightTX();
    if(currentAngle == 2491){
      endEarly = true;
    }
    error = currentAngle; //dont blame me Emilio this is Amy's fault
    integral = integral + (error * .02);
    derivative = (error - previousError) / .02;
    previousError = error;
    out = kP * error + integral * kI + derivative * kD;
    driveSignal = new DriveSignal(out, -out);
    Robot.drivetrain.driveDemand(ControlMode.PercentOutput, driveSignal);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    System.out.println("Derivative: " + derivative + " Integral: " + integral);
    return (!mBoard.lineUp() || endEarly);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
