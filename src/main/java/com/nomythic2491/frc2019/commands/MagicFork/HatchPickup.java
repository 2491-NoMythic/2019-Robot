/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicFork;

import com.nomythic2491.frc2019.commands.CommandBase;

public class HatchPickup extends CommandBase {
  
  /**
  * Uses the hatch solenoid to pick up a hatch
  */
  public HatchPickup() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(magicfork);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
      magicfork.grabHatch();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    if(magicfork.isHatchIn()) {
      System.out.println("Hatch grabbed successfully");
    }

    else {
      System.out.println("Hatch grab failed");
      magicfork.dropHatch();
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
