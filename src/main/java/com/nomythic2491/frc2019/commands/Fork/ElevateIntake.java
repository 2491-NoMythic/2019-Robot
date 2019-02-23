/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.nomythic2491.frc2019.commands.Fork;

import edu.wpi.first.wpilibj.DigitalInput;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.subsystems.Fork;
import com.nomythic2491.frc2019.Settings.Constants;

public class ElevateIntake extends CommandBase {

    /**
     * Elevates or lowers the carriage depending on current state
     */
    public ElevateIntake() {
        requires(fork);
    }

    protected void initialize() {
        if (fork.isElevatorDown()) {
            fork.isElevatorRising = true;
        }

        else if (isElevatorUp()) {
         fork.isElevatorRising = false;
        }

        else {
         System.out.println("An error has occurred with the elevator. It may not be in position, or the encoder might be disconnected.");
        }
    }

    protected void execute() {
        if (!oi.getButton(ControllerMap.operatorController, ControllerMap.toggleElevation)) {
            if (fork.isElevatorRising == false && !fork.isElevatorDown()) {
                fork.elevateIntake(Constants.kElevatorVelocity);
            }

            else if (fork.isElevatorRising && !isElevatorUp()) {
                fork.elevateIntake(-Constants.kElevatorVelocity);
            }

            else {
            }
        }
        else {
            fork.stopElevator();
        }    
    }

    public boolean isElevatorUp() {
        return fork.getElevatorHeight() >= (Constants.kElevatorMaxHeight - Constants.kElevatorUncertainty);
    }

    // Will return true when command does not need further execution
    protected boolean isFinished() {
        return ((fork.isElevatorRising == true && fork.isElevatorUp()) || (fork.isElevatorRising == false && fork.isElevatorDown()));
    }

    // Called when isFinished returns true
    protected void end() {
        fork.stopElevator();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
} 
