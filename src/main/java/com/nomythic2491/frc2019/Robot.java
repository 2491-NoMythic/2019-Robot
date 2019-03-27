/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.commands.AutoClimb;
import com.nomythic2491.frc2019.commands.AutoPlaceHatch;
import com.nomythic2491.frc2019.commands.drivetrain.RunSCurvePath;
import com.nomythic2491.frc2019.commands.drivetrain.TurnToPosition;
import com.nomythic2491.frc2019.subsystems.Climber;
import com.nomythic2491.frc2019.subsystems.Drivetrain;
import com.nomythic2491.frc2019.subsystems.MagicFork;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  ShuffleboardTab mainTab;
  public static ControlBoard controller;
  public static Drivetrain drivetrain;
  public static Climber climber;
  public static MagicFork magicFork;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    controller = ControlBoard.getInstance();
    drivetrain = Drivetrain.getInstance();
		climber = Climber.getInstance();
		magicFork = MagicFork.getInstance();

    Shuffleboard.addEventMarker("Robot initilized", EventImportance.kNormal);
    mainTab = Shuffleboard.getTab("Main");
    mainTab.add("AutoMode", m_chooser);
    mainTab.add(new RunSCurvePath());
    mainTab.add(new AutoPlaceHatch(true));
    mainTab.add(new TurnToPosition(20, true));
    mainTab.add("angle", Drivetrain.getInstance().getGyroAngle());
    mainTab.addPersistent("AngleToTurnTo", 22);
    mainTab.add(new AutoClimb());

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // SmartDashboard.putNumber("AngleToTurnTo", 22);
    // SmartDashboard.putNumber("angle", Drivetrain.getInstance().getGyroAngle());
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // if (m_autonomousCommand != null) {
    //   m_autonomousCommand.cancel();
    // }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
