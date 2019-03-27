/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.Robot;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoClimb extends Command {

  Timer timer = new Timer();
  int counter;
  double time;

  double upTime = 3.71;
  double forwardTime = upTime + 3.6;
  double driveUpTime = forwardTime + 3;
  double driveSleepyTime = driveUpTime + 1;

  DriveSignal driveBackwards = new DriveSignal(-.75, -.75);

  public AutoClimb() {
    requires(Robot.climber);
    requires(Robot.drivetrain);
    requires(Robot.magicFork);
    setInterruptible(false);
  }

  @Override
  protected void initialize() {
    timer.reset();
    counter = 0;
    timer.start();
  }

  @Override
  protected void execute() { 
    time = timer.get();
    switch (counter) {
    case 0:
      Robot.climber.runClimberDemand(ClimberDemand.Up);
      if (time > upTime) {
        counter++;
      }
      break;
    case 1:
      Robot.climber.runClimberDemand(ClimberDemand.Forward);
      if (time > forwardTime) {
        counter++;
      }
      break;
    case 2:
    Robot.drivetrain.driveDemand(ControlMode.PercentOutput, driveBackwards);
      Robot.climber.runClimberDemand(ClimberDemand.Down);
      if (time > driveUpTime) {
        Robot.climber.runClimberDemand(ClimberDemand.Stop);
        counter++;
      }
      break;
    case 3:
    Robot.drivetrain.driveDemand(ControlMode.PercentOutput, driveBackwards);
      if (time > driveSleepyTime) {
        Robot.drivetrain.stop();
        counter++;
      }
    default:
      System.out.println("invalid auto climb state");
      break;
    }
  }

  @Override
  protected boolean isFinished() {
    return counter == 4;
  }

  @Override
  protected void end() {
    timer.stop();
    Robot.climber.commandDone();
  }

  @Override
  protected void interrupted() {
  }
}
