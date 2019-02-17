/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.MagicBox.PositionRotate;

public class RotateMagicBoxToPosition extends CommandBase {
  PositionRotate startingPosition, targetPosition;
  boolean movingForward, movingBackwards;
  double mTarget;
  
  /**
   * Rotates the Magic Box a specified amount
   * @param position The target position the intake will rotate to, in encoder ticks (4096 per revolution)
   */
  public RotateMagicBoxToPosition(double target) {
    requires(magicbox);
    mTarget = target;//((target * 4096) / 360);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    magicbox.rotateToPoint(mTarget);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //System.out.println(magicbox.getIntakeRotatorPosition());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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