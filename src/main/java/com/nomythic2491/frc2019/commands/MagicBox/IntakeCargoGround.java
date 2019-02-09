/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeCargoGround extends CommandGroup {
  /**
   * Uses RotateMagicBoxToPosition and IntakeCargoUntilSuccess to pick up cargo from the ground.
   */
  public IntakeCargoGround() {
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.
    addSequential(new RotateMagicBoxToPosition(Constants.kBoxTipDownPosition));
    addSequential(new IntakeCargoUntilSuccess());
    addSequential(new RotateMagicBoxToPosition(Constants.kBoxTipUpPosition));


    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
