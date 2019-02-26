/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Climber;

import com.nomythic2491.frc2019.ControlBoard;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.commands.CommandBase;

public class ClimberLoop extends CommandBase {
  private ControlBoard mBoard;

  public ClimberLoop() {
    // Use requires() here to declare subsystem dependencies
    requires(climber);
    mBoard = ControlBoard.getInstance();
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    climber.runClimberDemand(mBoard.getClimberDemand());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    climber.runClimberDemand(ClimberDemand.Stop);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
