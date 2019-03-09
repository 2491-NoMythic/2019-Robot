/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Drivetrain;

import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.frc2019.commands.CommandBase;

import edu.wpi.first.wpilibj.Timer;

public class TurnToPosition extends CommandBase {

  private double target, initialPosition, relative;
  private Timer timer;
  private boolean type; 

  /**
   * 
   * @param speed Speed the robot turns at from -1 to 1, positive numbers go right, negative go left
   * @param angle The angle the robot will turn to from 0 to 360
   */
  public TurnToPosition(double angle, boolean absolute) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    type = absolute;
    target = angle;
    timer = new Timer();

    requires(drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    initialPosition = drivetrain.getGyroAngle();
    drivetrain.setInputRange(0,360); 
    drivetrain.setAbsoluteTolerance(2);
    drivetrain.getPIDController().setPID(Variables.proportionalRotate,Variables.integralRotate, Variables.derivativeRotate);

    relative = (drivetrain.getGyroAngle() + target);
    if (type) {
          drivetrain.setSetpoint(target);
    }
    else {
        drivetrain.setSetpoint(relative);
    }
    drivetrain.enable();
  }
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return drivetrain.onTarget();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drivetrain.stop();
    drivetrain.disable();

    System.out.println("Change in angle: " + (drivetrain.getRawGyroAngle()- initialPosition));
    System.out.println("Time taken: " + timer.get());
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
