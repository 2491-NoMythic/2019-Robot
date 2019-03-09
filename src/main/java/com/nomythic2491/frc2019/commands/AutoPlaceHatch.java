/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoPlaceHatch extends CommandBase {
  Timer timer;
  double tipToReleaseDelay;
  double releaseToDriveDelay;
  double driveLength;
  int state;
  DriveSignal drive;
  boolean b;
  public AutoPlaceHatch(boolean reverse) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(magicfork);
    requires(drivetrain);
    b = reverse;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    timer = new Timer();
    state = 0;
    tipToReleaseDelay = .1;
    releaseToDriveDelay = 0.3;
    driveLength = 1.5;
    drive = new DriveSignal(-.1, -.1);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    switch(state){
      case 0:
      {
        magicfork.tipIntake(b);
        timer.reset();
        state++;
        System.out.println("case 0");
      }
      case 1:
      {
        timer.start();
        if(timer.get() > tipToReleaseDelay){
          state++;
          timer.reset();
          System.out.println("case 1");
        }
      }
      case 2:
      {
        timer.start();
        if(timer.get() > releaseToDriveDelay){
          drivetrain.driveDemand(ControlMode.PercentOutput, drive);
          timer.reset();
          System.out.println("case 2");
          state++;
        }
      }
      case 3:
      {
        timer.start();
        if(timer.get() > driveLength){
          drivetrain.stop();
          System.out.println("case 0");
          state++;
        }
      }
      default:
      {
        System.out.println("CASE BROKE");
        end();
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return state >= 4;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("done");
    drivetrain.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
