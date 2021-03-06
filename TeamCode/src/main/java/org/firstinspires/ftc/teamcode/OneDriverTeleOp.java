/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Tank Drive w/ Lift (One Controller)", group="TeleOp")
public class OneDriverTeleOp extends OpMode {

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    Servo intakeFlip, intakeBar1, intakeBar2;
    DcMotor motorLift1, motorLift2;
    DcMotor intakeExtend;
    DcMotor intakeSpin;


    float power = 0;
    boolean lowPowerLift = false;
    boolean bPressed = false, fliped = false;

    public OneDriverTeleOp() {

    }

    public void init() {

        motorFrontRight = hardwareMap.dcMotor.get("fr");
        motorFrontLeft = hardwareMap.dcMotor.get("fl");
        motorBackRight = hardwareMap.dcMotor.get("br");
        motorBackLeft = hardwareMap.dcMotor.get("bl");
        motorLift1 = hardwareMap.dcMotor.get("l1");
        motorLift2 = hardwareMap.dcMotor.get("l2");
        intakeExtend = hardwareMap.dcMotor.get("extend");
        intakeSpin = hardwareMap.dcMotor.get("spin");

        intakeFlip = hardwareMap.servo.get("flip");
        intakeBar1 = hardwareMap.servo.get("b1");
        intakeBar2 = hardwareMap.servo.get("b2");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);    // switch to left motors to switch which side is front
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorLift2.setDirection(DcMotorSimple.Direction.REVERSE);

        motorFrontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLift1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLift2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lowPowerLift = false;
    }


    public void loop() {
        float right = gamepad1.left_stick_y;    // currently switched so switch which side is front
        float left = gamepad1.right_stick_y;
        float rt = (1 - gamepad1.right_trigger)/2;
        float lt = (1 - gamepad1.left_trigger)/2;
        float liftPower = 0;


        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        left =  (float)scaleInput(left);
        right = (float)scaleInput(right);
        rt = (float)scaleInput(rt);
        lt = (float)scaleInput(lt);

        if(gamepad1.y) {
            intakeFlip.setPosition(1.00);
        }
        else {
            intakeFlip.setPosition(0.4);
        }

        if(gamepad1.x) {
            intakeBar1.setPosition(0);
            intakeBar2.setPosition(1.0);
        }
        else {
            intakeBar1.setPosition(1.0);
            intakeBar2.setPosition(0);
        }



        // clip the right/left values so that the values never exceed +/- 1
        left = Range.clip(left, -1, 1);
        right = Range.clip(right, -1, 1);
        rt = Range.clip(rt,(float)0,(float)0.5);
        lt = Range.clip(lt,(float)0,(float)0.5);
        liftPower = rt - lt;

        motorFrontRight.setPower(right*.7);
        motorBackRight.setPower(right*.7);
        motorFrontLeft.setPower(left*.7);
        motorBackLeft.setPower(left*.7);

        if(gamepad1.x){
            lowPowerLift = true;
        }else if(gamepad1.y){
            lowPowerLift = false;
        }

        if(gamepad1.dpad_up){
            motorLift1.setPower(1);
            motorLift2.setPower(1);
        }else if(gamepad1.dpad_down){
            motorLift1.setPower(-1);
            motorLift2.setPower(-1);
        }else{
            motorLift1.setPower(0);
            motorLift2.setPower(0);
        }

        intakeExtend.setPower(gamepad1.right_trigger-gamepad1.left_trigger);
        if(gamepad1.left_bumper) {
            intakeSpin.setPower(-1.0);
        }
        else if(gamepad1.right_bumper) {
            intakeSpin.setPower(1.0);
        }
        else {
            intakeSpin.setPower(0.0);
        }


        if(lowPowerLift){
            telemetry.addData("Low Power Lift", "On");
        }else{
            telemetry.addData("Low Power Lift", "Off");
        }

        telemetry.addData("Lift Encoder 1", motorLift1.getCurrentPosition());
        telemetry.addData("Lift Encoder 2", motorLift2.getCurrentPosition());

        /*
         * Send telemetry data back to driver station.
         */
        telemetry.addData("Text", "*** v2.1 ***");
        telemetry.addData("gamepad1:", gamepad1);
    }

    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        return dVal*dVal*dVal;		// maps {-1,1} -> {-1,1}
    }
}
