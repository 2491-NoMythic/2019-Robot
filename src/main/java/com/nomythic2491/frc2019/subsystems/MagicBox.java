/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Settings.Constants;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class MagicBox extends Subsystem {
  
  private static MagicBox instance;
  private TalonSRX intake, rotator, elevatorLeft, elevatorRight;
  private DoubleSolenoid spindleRight, spindleLeft;

  public static MagicBox getInstance() {
    if (instance == null) {
      instance = new MagicBox();
    }
    return instance;
  }

  private MagicBox() {
    intake = new TalonSRX(Constants.kIntakeRollerId);
    rotator = new TalonSRX(Constants.kRotator);
    elevatorRight = new TalonSRX(Constants.kElevatorRight);
    elevatorLeft = new TalonSRX(Constants.kElevatorLeft);
    spindleRight = new DoubleSolenoid(Constants.kRightHatchOutChannel, Constants.kRightHatchInChannel);
    spindleLeft = new DoubleSolenoid(Constants.kLeftHatchOutChannel, Constants.kLeftHatchInChannel);
  }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
