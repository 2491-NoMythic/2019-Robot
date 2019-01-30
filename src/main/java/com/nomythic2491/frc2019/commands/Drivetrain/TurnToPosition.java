/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Drivetrain;

import com.nomythic2491.frc2019.commands.CommandBase;

public class TurnToPosition extends CommandBase {

  double speed, angle, initialPosition;

  /**
   * 
   * @param speed Speed the robot turns at from -1 to 1, positive numbers go right, negative go left
   * @param angle The angle the robot will turn to from 0 to 360
   */
  public TurnToPosition(double speed, double angle) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(drivetrain);
    this.speed = speed;
    this.angle = angle;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    initialPosition = drivetrain.getGyroAngle();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    drivetrain.drivePercentOutput(speed, -speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return drivetrain.getGyroAngle() >= Math.abs(initialPosition + angle);
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