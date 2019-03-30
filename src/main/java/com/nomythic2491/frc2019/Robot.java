/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.commands.AutoClimb;
import com.nomythic2491.frc2019.commands.AutoPlaceHatch;
import com.nomythic2491.frc2019.commands.Drivetrain.RunSCurvePath;
import com.nomythic2491.frc2019.commands.Drivetrain.TurnToPosition;
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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

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

  boolean is30Sec;

  // I2C Variables
  private I2C.Port i2cPort;
  private byte[] dataSend;
  private byte[] dataRecieve;
  private I2C i2c;
  private int deviceNo;

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

    doI2cSetup();
    Shuffleboard.addEventMarker("Robot initilized", EventImportance.kNormal);
    mainTab = Shuffleboard.getTab("Main");
    mainTab.add("AutoMode", m_chooser);
    mainTab.add(new RunSCurvePath());
    mainTab.add(new AutoPlaceHatch(true));
    mainTab.add(new TurnToPosition(20, true));
    mainTab.add("angle", Drivetrain.getInstance().getGyroAngle());
    mainTab.addPersistent("AngleToTurnTo", 22);

  }

  private void doI2cSetup() {
    i2cPort = I2C.Port.kMXP; // when using the MXP expansion Port
    // i2cPort = I2C.Port.kOnboard; // when using the RoboRio built in port
    deviceNo = 4; // needs to match Arduino
    i2c = new I2C(i2cPort, deviceNo);
    dataSend = new byte[1];
    dataRecieve = new byte[1];
    System.out.println("Init I2C");

    dataSend[0] = 2;
    i2c.transaction(dataSend, 1, dataRecieve, 1);
    System.out.println("Turn purple lights on");
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
    is30Sec = false;
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    double timeLeft = DriverStation.getInstance().getMatchTime();
    // if less than 30 sec run once
    if (timeLeft <= 30 && is30Sec == false && timeLeft != -1) {
      dataSend[0] = 3;
      i2c.transaction(dataSend, 1, dataRecieve, 1);
      System.out.println("Turn on 30sec animation");
      is30Sec = true;
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
