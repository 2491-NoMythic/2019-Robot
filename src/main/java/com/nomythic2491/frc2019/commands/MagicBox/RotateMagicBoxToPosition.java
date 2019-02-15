/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.MotionProfiles;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.MagicBox.positionRotate;

public class RotateMagicBoxToPosition extends CommandBase {
  positionRotate startingPosition, targetPosition;
  boolean movingForward, movingBackwards;
  
  /**
   * Rotates the Magic Box a specified amount
   * @param position The target position the intake will rotate to, in encoder ticks (4096 per revolution)
   */
  public RotateMagicBoxToPosition(positionRotate position) {
    this.targetPosition = position;
    requires(magicbox);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startingPosition = Variables.currentMagicboxPosition;
    if (startingPosition == positionRotate.GROUND){
      if(targetPosition == positionRotate.FLAT){
        magicbox.runMagicboxMotionProfile(MotionProfiles.roatateFromFlatToDown, MotionProfiles.roatateFromFlatToDown.length, true);
      } else if(targetPosition == positionRotate.BACK){
        magicbox.runMagicboxMotionProfile(MotionProfiles.downToBack, MotionProfiles.downToBack.length, true);
      }
    }else if (startingPosition == positionRotate.FLAT){
      if(targetPosition == positionRotate.GROUND){
        magicbox.runMagicboxMotionProfile(MotionProfiles.roatateFromFlatToDown, MotionProfiles.roatateFromFlatToDown.length, false);
      } else if (targetPosition ==  positionRotate.BACK){
        magicbox.runMagicboxMotionProfile(MotionProfiles.flatToBack, MotionProfiles.flatToBack.length, true);
      }
    }else{
      if(targetPosition == positionRotate.FLAT){
        magicbox.runMagicboxMotionProfile(MotionProfiles.flatToBack, MotionProfiles.flatToBack.length, false);
      }else if (targetPosition == positionRotate.GROUND){
        magicbox.runMagicboxMotionProfile(MotionProfiles.downToBack, MotionProfiles.downToBack.length, false);
      }
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return magicbox.getIsMagicboxRunningMotionProfile();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
