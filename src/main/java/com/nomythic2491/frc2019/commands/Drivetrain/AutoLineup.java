/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.lib.util.DriveSignal;

public class AutoLineup extends CommandBase {
  DriveSignal driveSignal; 
  double currentAngle;
  double kP;
  public AutoLineup() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    kP = .0370; 
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    currentAngle = drivetrain.getLimelightTX();
    driveSignal = new DriveSignal(currentAngle * kP, -currentAngle * kP);
    drivetrain.driveDemand(ControlMode.PercentOutput, driveSignal);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drivetrain.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
