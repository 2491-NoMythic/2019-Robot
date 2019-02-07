/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;

public class ElevateBox extends CommandBase {
  
  /**
   * Raises/lowers the magic box along the bars
   */
  public ElevateBox() {
    requires(magicbox);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if (magicbox.isElevatorDown()) {
      magicbox.isElevatorRising = true;
    }

    else if (magicbox.isElevatorUp()) {
      magicbox.isElevatorRising = false;
    }

    else {
      System.out.println("An error has occurred with the elevator. It may not be in position, or the encoder might be disconnected.");
    }
  }
  

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (magicbox.isElevatorRising && !magicbox.isElevatorUp()) {
      magicbox.elevateIntake(Constants.kElevatorVelocity);
    }

    else if (magicbox.isElevatorRising == false && !magicbox.isElevatorDown()) {
      magicbox.elevateIntake(-Constants.kElevatorVelocity);
    }

    else {
      System.out.println("An error has occurred in the intake elevation process. If this error appears every time the command stops, consider removing it.");
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return ((magicbox.isElevatorRising == true && magicbox.isElevatorUp()) || (magicbox.isElevatorRising == false && magicbox.isElevatorDown()));
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
