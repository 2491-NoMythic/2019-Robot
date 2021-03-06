/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.lib.util.DriveSignal;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

public class RunSCurvePath extends CommandBase {
  Waypoint[] waypoints;
  Trajectory.Config config;
  int count;
  Trajectory left;
  Trajectory right;
  double mpstoetpms = 5133.214;
  String path;
  public RunSCurvePath() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(drivetrain);
    double wheelbase_width = 25.75/39.37;
    waypoints = new Waypoint[] {
      new Waypoint(-3,-1,0),
      new Waypoint(0,0,0)
    };
    config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.02, 3.3528, 8.0, 60.0);
    Trajectory trajectory = Pathfinder.generate(waypoints, config);
    TankModifier modifier = new TankModifier(trajectory);
    modifier.modify(wheelbase_width);
    left  = modifier.getLeftTrajectory();
    right = modifier.getRightTrajectory();
    count = 0;
    path = "";
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("Running");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Trajectory.Segment segLeft = left.get(count);
    Trajectory.Segment segRight = right.get(count);
    double adjustedSegLeft = segLeft.velocity*mpstoetpms;
    double adjustedSegRight = segRight.velocity*mpstoetpms;
    DriveSignal signal = new DriveSignal(adjustedSegRight,adjustedSegLeft); //2*mpstoetpms, 2*mpstoetpms
    drivetrain.driveDemand(ControlMode.Velocity, signal);
    count++;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // return left.length() == count;
    return count ==125;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("Done");
    drivetrain.stop();
    System.out.println(path);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
