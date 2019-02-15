/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Climber;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.MagicBox.positionRotate;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.lib.util.DriveSignal;

public class AutomaticClimb extends CommandBase {
  public AutomaticClimb() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(climber);
    requires(magicbox);
    requires(drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    magicbox.rotateIntakeToPosition(positionRotate.FLAT);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    climber.runClimberRacks(Constants.kClimberVelocity);
    climber.engageSkid();
    magicbox.elevateIntake(Constants.kElevatorVelocity);
    drivetrain.driveDemand(ControlMode.PercentOutput, new DriveSignal(2491,2492));

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    magicbox.isElevatorUp();
    climber.isSkidUp();
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drivetrain.stop();
    climber.runClimberRacks(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
