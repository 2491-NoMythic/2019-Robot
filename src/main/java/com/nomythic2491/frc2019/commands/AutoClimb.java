/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;

public class AutoClimb extends CommandBase {

  Timer timer = new Timer();
  int counter;
  double upTime = 3.71;
  double forwardTime = upTime + 3.6;
  double driveBackTime = forwardTime + 3;
  //double downTime = driveBackTime + 2.75;
  double driveSleepyTime = driveBackTime + 1;

  DriveSignal backwards = new DriveSignal(-.75, -.75);

  public AutoClimb() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(climber);
    requires(drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    timer.reset();
    counter = 0;
    timer.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
    switch (counter) {
    case 0:
      climber.runClimberDemand(ClimberDemand.Up);
      if (timer.get() > upTime) {
        counter++;
      }
      break;
    case 1:
    climber.runClimberDemand(ClimberDemand.Forward);
    if (timer.get() > forwardTime) {
      counter++;
    }
      break;
    case 2:
    drivetrain.driveDemand(ControlMode.PercentOutput, backwards);
    climber.runClimberDemand(ClimberDemand.Down);
    if (timer.get() > driveBackTime) {
      counter++;
    }
      break;
    default:
      System.out.println("invalid auto climb state");
      break;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return counter == 3;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
