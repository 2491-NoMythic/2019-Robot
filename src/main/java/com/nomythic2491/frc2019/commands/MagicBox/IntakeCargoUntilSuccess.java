/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.MagicBox.positionRotate;

public class IntakeCargoUntilSuccess extends CommandBase {

  RotateMagicBoxToPosition controlMagicbox;
  
  /**
   * Runs the cargo intake until cargo appears in the magic box.
   */
  public IntakeCargoUntilSuccess() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(magicbox);
    controlMagicbox = new RotateMagicBoxToPosition(positionRotate.GROUND);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    controlMagicbox.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    magicbox.runIntake(Constants.kCargoIntakeVelocity);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return magicbox.isCargoIn();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    magicbox.runIntake(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
