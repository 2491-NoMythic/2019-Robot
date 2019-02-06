package com.nomythic2491.frc2019.commands.Fork;

import edu.wpi.first.wpilibj.DigitalInput;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.subsystems.Fork;
import com.nomythic2491.frc2019.Settings.Constants;

public class ElevateIntake extends CommandBase {

    DigitalInput limitSwitch = new DigitalInput(1);

    public ElevateIntake() {
        requires(fork);
    }

    protected void initialize() {
    }

    protected void execute() {



        if (!oi.getButton(ControllerMap.operatorController, ControllerMap.toggleElevation)) {

            if (isElevatorDown()== true) {

            }
            else if (isElevatorUp() == true) {

            }
            else {
                System.out.println("An error has occurred with the elevator. It may not be in position, or the encoder might be disconnected.");
            }
        }
        else {
            fork.stopElevator();
        }    
    }

    public boolean isElevatorDown() {
        return limitSwitch.get();
    }

    public boolean isElevatorUp() {
        return fork.getElevatorHeight() >= (Constants.kElevatorMaxHeight - Constants.kElevatorUncertainty);
    }

    // Will return true when command does not need further execution
    protected boolean isFinished() {
        return false;
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