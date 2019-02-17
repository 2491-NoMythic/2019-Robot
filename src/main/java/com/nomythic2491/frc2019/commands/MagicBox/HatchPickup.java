/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.commands.CommandBase;

//import edu.wpi.first.wpilibj.Timer;

public class HatchPickup extends CommandBase{
  //private Timer timer;

  /**
   * Runs the hatch pickup intake system.
   * If it is activated, it deactivates and vice versa.
   */
  public HatchPickup() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(magicbox);
    //timer = new Timer();
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    magicbox.retractSolenoid();
    }

    //timer.reset();

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    /**if (magicbox.leftSolenoidExtended()) {
      magicbox.extendRightHatchSolenoid();
      timer.delay(.25);
      magicbox.retractLeftHatchSolenoid();
    }
    else if (!magicbox.leftSolenoidExtended()) {
      magicbox.extendLeftHatchSolenoid();
      timer.delay(.25);
      magicbox.retractRightHatchSolenoid();
    }*/
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    magicbox.extendSolenoid();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
