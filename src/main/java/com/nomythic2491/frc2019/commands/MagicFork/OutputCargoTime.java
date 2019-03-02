/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicFork;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;

import edu.wpi.first.wpilibj.Timer;

public class OutputCargoTime extends CommandBase {

  private Timer timer;
  private double time;
  
  /**
   * Runs the intake motor backwards for a given period of time to shoot a cargo
   */
  public OutputCargoTime(double time) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    timer = new Timer();
    requires(magicfork);
    this.time = time;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    timer.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    magicfork.runIntake(Constants.kMFShootSpeed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return timer.get() >= time;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    magicfork.runIntake(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
