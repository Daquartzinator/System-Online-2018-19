package org.firstinspires.ftc.teamcode._Auto.Steps;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;


public class UnlatchStep extends AutoLib.Step {
    DcMotor mEncoderMotor, mOtherMotor;    // motor to control
    double mPower;          // power level to use
    int mEncoderCount;      // target encoder count
    OpMode opMode;
    boolean mStop;          // stop motor when count is reached
    AutoLib.Timer mTimer;

    public UnlatchStep(OpMode opMode, DcMotor encoderMotor, DcMotor otherMotor, double power, int count) {
        mEncoderMotor = encoderMotor;
        mOtherMotor = otherMotor;
        mPower = power;
        mEncoderCount = count;
        this.opMode = opMode;
    }

    public boolean loop() {
        super.loop();

        boolean done = false;

        if(firstLoopCall()) {
            mTimer = new AutoLib.Timer(1.6);
            mTimer.start();
        }

        mEncoderMotor.setPower(mPower);
        mOtherMotor.setPower(mPower);

        opMode.telemetry.addData("encoder count", mEncoderMotor.getCurrentPosition());
        opMode.telemetry.addData("power", mPower);
        opMode.telemetry.addData("motor 1 power", mEncoderMotor.getPower());
        opMode.telemetry.addData("motor 2 power", mOtherMotor.getPower());
        opMode.telemetry.addData("time", mTimer.elapsed());



        // the rest of the time, just update power and check to see if we're done
       // done = Math.abs(mEncoderMotor.getCurrentPosition()) > Math.abs(mEncoderCount);
        if(mTimer.done()) {
            mEncoderMotor.setPower(0);
            mOtherMotor.setPower(0);
            done = true;
        }
        return done;
    }
}