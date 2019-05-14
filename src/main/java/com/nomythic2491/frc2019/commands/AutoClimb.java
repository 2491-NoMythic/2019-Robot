/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.Robot;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoClimb extends Command {

  Timer timer = new Timer();
  int counter;
  double time;

  double carriageUp = 2;
  double mUpTime;// 3.4;
  double forwardTime;
  double driveUpTime;
  double driveSleepyTime;
  DriveSignal driveForward = new DriveSignal(-.1, -0.1);
  DriveSignal driveSleepy = new DriveSignal(-0.30, -0.30);
  DriveSignal driveBackwards = new DriveSignal(-0.6, -0.6);

  public AutoClimb(double upTime, double downTime) {
    requires(Robot.climber);
    requires(Robot.drivetrain);
    requires(Robot.magicFork);
    setInterruptible(false);
    carriageUp = 2;
    mUpTime = carriageUp + upTime; // 3.
    forwardTime = mUpTime + 3.6;
    driveUpTime = forwardTime + downTime;// 3.1;
    driveSleepyTime = driveUpTime + 1.5;
  }

  @Override
  protected void initialize() {
    timer.reset();
    Robot.drivetrain.commandActive(true);
    Robot.climber.commandActive(true);
    Robot.climber.commandActive(true);
    counter = 0;
    timer.start();
  }

  @Override
  protected void execute() {
    time = timer.get();
    switch (counter) {
    case 0:
      Robot.magicFork.runGamepieceDemand(Constants.GamepieceDemand.Climb, 0);
      if (time > carriageUp) {
        counter++;
      }
      break;
    case 1:
      Robot.climber.runClimberDemand(ClimberDemand.Up);
      if (time > mUpTime) {
        counter++;
      }
      break;
    case 2:
      Robot.climber.runClimberDemand(ClimberDemand.Forward);
      Robot.drivetrain.driveDemand(ControlMode.PercentOutput, driveForward);
      if (time > forwardTime) {
        counter++;
      }
      break;
    case 3:
      Robot.drivetrain.driveDemand(ControlMode.PercentOutput, driveBackwards);
      Robot.climber.runClimberDemand(ClimberDemand.Down);
      System.out.println("Case 3");
      if (time > driveUpTime) {
        Robot.climber.runClimberDemand(ClimberDemand.Stop);
        counter++;
      }
      break;
    case 4:
      Robot.drivetrain.driveDemand(ControlMode.PercentOutput, driveSleepy);
      System.out.println("Case 4");
      if (time > driveSleepyTime) {
        Robot.drivetrain.stop();
        counter++;
      }
      break;
    default:
      System.out.println("invalid auto climb state");
      break;
    }
  }

  @Override
  protected boolean isFinished() {
    return counter >= 5;
  }

  @Override
  protected void end() {
    System.out.println("Case: " + counter);
    timer.stop();
    Robot.climber.commandActive(false);
    Robot.drivetrain.commandActive(false);
    Robot.magicFork.commandActive(false);
  }

  @Override
  protected void interrupted() {
  }
}
