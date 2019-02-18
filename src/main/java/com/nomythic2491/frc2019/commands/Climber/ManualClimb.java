/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Climber;

import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.Climber;


public class ManualClimb extends CommandBase {
  private boolean mDeploy, mStop;

  public ManualClimb(Boolean deploy, boolean stop) {
    // Use requires() here to declare subsystem dependencies
    requires(climber);
    mDeploy = deploy;
    mStop = stop;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    climber.engageRatchet(mDeploy);
    if (mDeploy) {
      climber.runClimberRacks(-1);
    } else {
      climber.runClimberRacks(1);
    }
   
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    if (mStop) {
      climber.runClimberRacks(0);
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
