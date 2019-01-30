/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Drivetrain;

import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.frc2019.commands.CommandBase;

public class Drive extends CommandBase {

  private double turnSpeed, currentLeftSpeed, currentRightSpeed, lastLeftSpeed, lastRightSpeed;	


  public Drive() {
    requires(drivetrain);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    lastLeftSpeed = currentLeftSpeed;
    lastRightSpeed = currentRightSpeed;

    currentLeftSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveControllerLeft, ControllerMap.driveMainAxisLeft, 0.1);
    currentRightSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveControllerRight, ControllerMap.driveMainAxisRight, 0.1);

    //Left Joystick
    switch(Variables.degreeofaccel){
      case 1:
        double rightDeltay = (currentLeftSpeed - lastLeftSpeed);
        double signOfValue = (rightDeltay / Math.abs(rightDeltay));
        if (Math.abs(rightDeltay) > Variables.linearAccelerationValue){
          if (Math.abs(currentLeftSpeed) - Math.abs(lastLeftSpeed) > 0) {
            //System.out.println(currentLeftSpeed + " was too high, setting to " + (lastLeftSpeed + (Variables.accelerationSpeed * signOfLeftAcceleration)));
            currentLeftSpeed = lastLeftSpeed + (Variables.linearAccelerationValue * signOfValue);
            
          }
        }
      default:
    }

    //Right Joystick
    switch(Variables.degreeofaccel){
      case 1:
      double rightDeltay = (currentRightSpeed - lastRightSpeed);
      double signOfRightValue = (rightDeltay / Math.abs(rightDeltay));
      if (Math.abs(rightDeltay) > Variables.linearAccelerationValue) { // otherwise the power is below 0.05 accel and is fine
				if (Math.abs(currentRightSpeed) - Math.abs(lastRightSpeed) > 0) {
					//System.out.println(currentRightSpeed + " was too high, setting to " + (lastRightSpeed + (Variables.accelerationSpeed * signOfRightAcceleration)));
					currentRightSpeed = lastRightSpeed + (Variables.linearAccelerationValue * signOfRightValue);
				}
				// if the difference between the numbers is positive it is going up
			}
      default:
    }

    drivetrain.drivePercentOutput(currentLeftSpeed, currentRightSpeed);
    
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
