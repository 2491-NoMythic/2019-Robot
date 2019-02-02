/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;

public class BeginBoxElevation extends CommandBase {
  public BeginBoxElevation() {
    requires(magicbox);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if (magicbox.isElevatorDown()) {
      magicbox.elevateIntake(Constants.kElevatorVelocity);
      
    }

    else if (magicbox.isElevatorUp()) {
      magicbox.elevateIntake(-Constants.kElevatorVelocity);
    }

    else {
      System.out.println("An error has occurred with the elevator. It may not be in position, or the encoder might be disconnected.");
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    magicbox.elevateIntake(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
