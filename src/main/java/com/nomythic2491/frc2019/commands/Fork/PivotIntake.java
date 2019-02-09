package com.nomythic2491.frc2019.commands.Fork;

import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.subsystems.Fork;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PivotIntake extends CommandBase {

    public PivotIntake() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(fork);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(fork.getSoleniodPosition() == Value.kForward) {
            fork.pivotIntake(Value.kReverse);
        }
        else {
            System.out.println("An error has occured with the pivot system. Make sure that the Fork is set to its default position.");
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(!oi.getButton(ControllerMap.operatorController, ControllerMap.togglePivot)) {
            if(fork.getSoleniodPosition() == Value.kReverse) {
                fork.pivotIntake(Value.kForward);
            }

            else if(fork.getSoleniodPosition() == Value.kForward) {
                fork.pivotIntake(Value.kReverse);
            }

            else {
            }
        }
        else {
            fork.stopPivot();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if(fork.getSoleniodPosition() == Value.kForward) {
            return true;
        }

        else if(fork.getSoleniodPosition() == Value.kReverse) {
            return true;
        }

        else{
            return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        fork.stopPivot();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}