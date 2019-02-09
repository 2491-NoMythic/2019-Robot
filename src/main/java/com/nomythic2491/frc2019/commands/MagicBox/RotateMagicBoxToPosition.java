/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;

public class RotateMagicBoxToPosition extends CommandBase {
  double targetPosition, startingPosition;
  boolean movingForward, movingBackwards;
  
  /**
   * Rotates the Magic Box a specified amount
   * @param position The target position the intake will rotate to, in encoder ticks (4096 per revolution)
   */
  public RotateMagicBoxToPosition(double position) {
    this.targetPosition = position;
    requires(magicbox);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startingPosition = magicbox.getIntakeRotatorPosition();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (targetPosition > startingPosition) {
      magicbox.rotateIntake(Constants.kBoxRotateVelocity);
      movingForward = true;
      movingBackwards = false;
    }

    else if (targetPosition < startingPosition) {
      magicbox.rotateIntake(-Constants.kBoxRotateVelocity);
      movingForward = false;
      movingBackwards = true;
    }

    else {
      magicbox.rotateIntake(0);
      movingForward = false;
      movingBackwards = false;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return ((movingForward && (magicbox.getIntakeRotatorPosition() >= (targetPosition - 5))) || (movingBackwards && (magicbox.getIntakeRotatorPosition() <= (targetPosition +5))));
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
