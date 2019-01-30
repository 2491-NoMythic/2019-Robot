/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Settings.Constants;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class MagicBox extends Subsystem {
  
  private static MagicBox instance;
  private TalonSRX intake, rotateIntake, elevatorLeft, elevatorRight;
  private DoubleSolenoid spindleRight, spindleLeft;

  public static MagicBox getInstance() {
    if (instance == null) {
      instance = new MagicBox();
    }
    return instance;
  }

  private MagicBox() {
    intake = new TalonSRX(Constants.kIntakeRollerId);
    rotateIntake = new TalonSRX(Constants.kRotator);
    elevatorRight = new TalonSRX(Constants.kElevatorRight);
    elevatorLeft = new TalonSRX(Constants.kElevatorLeft);
    spindleRight = new DoubleSolenoid(Constants.kRightHatchOutChannel, Constants.kRightHatchInChannel);
    spindleLeft = new DoubleSolenoid(Constants.kLeftHatchOutChannel, Constants.kLeftHatchInChannel);
  }

  /**
   * Runs the intake at a given speed
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void runIntake(double speed) {
    intake.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Elevates the intake at a specified speed
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void elevateIntake(double speed){
    elevatorLeft.set(ControlMode.PercentOutput, speed);
    elevatorRight.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Rotates the intake at a speed
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void rotateIntake(double speed){
    rotateIntake.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Stops the elevator from moving
   */
  public void stopElevator(){
    elevateIntake(0);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
