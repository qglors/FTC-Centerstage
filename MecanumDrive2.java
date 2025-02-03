package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class MecanumDrive2 extends LinearOpMode {
    private DcMotor LeftFront = null;
    private DcMotor RightFront = null;
    private DcMotor LeftBack = null;
    private DcMotor RightBack = null;
    private DcMotor LeftArm = null;
    private DcMotor RightArm = null;
    private DcMotor LeftSuspension = null;
    private DcMotor RightSuspension = null;
    private Servo Wrist = null;
    private Servo Claw = null;
    private Servo DroneLauncher = null;
    private Servo PixelHolder = null;

    // initialize suspension motor positions to 0
    private int LeftSuspensionPos = 0;
    private int RightSuspensionPos = 0;

    @Override
    public void runOpMode() {

        // set speed
        double speed = 1.5;

        // set x and y for right joystick

        double x;
        double y;

        // set x and y for left joystick
        double xleft;
        double yleft;

        // double yleft = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        // double xleft = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        // double rx = gamepad1.left_stick_x;

        // for left joystick
        // double xleft;
        // double yleft;

        // declare variables for drive train motors
        LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        RightFront = hardwareMap.get(DcMotor.class, "RightFront");
        LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        RightBack = hardwareMap.get(DcMotor.class, "RightBack");

        // declare variables for arm motors
        LeftArm = hardwareMap.get(DcMotor.class, "LeftArm");
        RightArm = hardwareMap.get(DcMotor.class, "RightArm");

        // declare variables for suspension motors
        LeftSuspension = hardwareMap.get(DcMotor.class, "LeftSuspension");
        RightSuspension = hardwareMap.get(DcMotor.class, "RightSuspension");

        // reset suspension motors
        // LeftSuspension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // RightSuspension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // declare variables for claw and wrist servos
        Claw = hardwareMap.get(Servo.class, "Claw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");

        // declare variable for drone launcher
        DroneLauncher = hardwareMap.get(Servo.class, "DroneLauncher");

        PixelHolder = hardwareMap.get(Servo.class, "PixelHolder");

        // reverse certain wheel motors
        // RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        // reverse certain arm motors
        RightArm.setDirection(DcMotorSimple.Direction.REVERSE);

        // brakes the wheel motors when inactive
        // LeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // RightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // LeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // RightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // brakes the arm when inactive so arm goes down slower instead of slamming
        // cause of gravity
        LeftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // brakes the suspension when inactive so suspennsion goes down slower instead
        // of slamming due to gravity
        LeftSuspension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightSuspension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        LeftSuspension.setDirection(DcMotorSimple.Direction.REVERSE);

        // //reset suspension motors
        // LeftSuspension.setMode(DcMotor.RunMode.RESET_ENCODERS);
        // RightSuspension.setMode(DcMotor.RunMode.RESET_ENCODERS);

        // When code is initialized on the phone, telemetry will print
        telemetry.addData("Status", "Initialized, hi driver");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // When code is active on the phone, telemetry will print
            telemetry.addData("Status", "Running");
            telemetry.update();

            // set variables when right joystick is moved left or right
            x = gamepad1.right_stick_x;
            y = -gamepad1.right_stick_y;

            // set variables when left joystick is moved left or right
            xleft = gamepad1.left_stick_x;
            yleft = -gamepad1.left_stick_y;
            double rxleft = gamepad1.left_stick_x;

            // double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            // double LeftFrontPower = (yleft + xleft) / denominator;
            // double LeftBackPower = (yleft - xleft) / denominator;
            // double RightFrontPower = (yleft - xleft) / denominator;
            // double RightBackPower = (yleft + xleft) / denominator;

            // double denominator = Math.max(Math.abs(yleft) + Math.abs(xleft) +
            // Math.abs(rxleft), 1);
            // double LeftFrontPower = (yleft + xleft + rxleft) / denominator;
            // double LeftBackPower = (yleft + xleft + rxleft) / denominator;
            // double RightFrontPower = (yleft - xleft - rxleft) / denominator;
            // double RightBackPower = (yleft - xleft - rxleft) / denominator;

            // strafe on left joystick
            double denominator = Math.max(Math.abs(yleft) + Math.abs(xleft) + Math.abs(rxleft), 1);
            double LeftFrontPower = (yleft + xleft + rxleft) / denominator;
            double LeftBackPower = (yleft - xleft - rxleft) / denominator;
            double RightFrontPower = (yleft - xleft - rxleft) / denominator;
            double RightBackPower = (yleft + xleft + rxleft) / denominator;

            // right joystick speed + controls
            RightFront.setPower(speed * (y - x));
            LeftFront.setPower(speed * (y + x));
            RightBack.setPower(speed * (y - x));
            LeftBack.setPower(speed * (y + x));

            // left joystick speed
            RightFront.setPower(speed * (RightFrontPower));
            LeftFront.setPower(speed * (LeftFrontPower));
            RightBack.setPower(speed * (RightBackPower));
            LeftBack.setPower(speed * (LeftBackPower));

            /*
             * manualArmPower = gamepad1.right_trigger - gamepad1.left_trigger;
             * 
             * if (Math.abs(manualArmPower) > armManualDeadband) {
             * if (!manualMode) {
             * armLeft.setPower(0.0);
             * armRight.setPower(0.0);
             * armLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
             * armRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
             * manualMode = true;
             * }
             * armLeft.setPower(manualArmPower);
             * armRight.setPower(manualArmPower);
             * 
             * if (manualMode) {
             * armLeft.setTargetPosition(armLeft.getCurrentPosition());
             * armRight.setTargetPosition(armRight.getCurrentPosition());
             * armLeft.setPower(1.0);
             * armRight.setPower(1.0);
             * armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
             * armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
             * manualMode = false;
             * }
             */

            // LeftSuspension.setPower(1.0);
            // RightSuspension.setPower(1.0);
            // LeftArm.setPower(0.5);
            // RightArm.setPower(0.5);

            /*
             * if (gamepad1.a) {
             * LeftFront.setPower(2.0);
             * RightFront.setPower(2.0);
             * LeftBack.setPower(2.0);
             * RightBack.setPower(2.0);
             * }
             */

            // suspension raise
            if (gamepad1.right_bumper) {
                if (RightSuspension.getCurrentPosition() <= 19) {
                    LeftSuspension.setPower(2.0);
                    RightSuspension.setPower(2.0);
                    telemetry.addData("suspension raising ", RightSuspension.getCurrentPosition());
                }
            }

            // suspension lower
            else if (gamepad1.left_bumper) {
                if (RightSuspension.getCurrentPosition() >= -13545) {
                    LeftSuspension.setPower(-0.7);
                    RightSuspension.setPower(-0.7);
                    telemetry.addData("suspension lowering ", RightSuspension.getCurrentPosition());
                }
            }

            // suspension not powered
            else {
                LeftSuspension.setPower(0);
                RightSuspension.setPower(0);
            }

            // arm raise
            if (gamepad1.right_trigger > 0) {
                RightArm.setPower(0.7);
                LeftArm.setPower(0.7);
            }

            // arm lower
            else if (gamepad1.left_trigger > 0) {
                LeftArm.setPower(-0.5);
                RightArm.setPower(-0.5);
            }

            // arm not powered
            else {
                LeftArm.setPower(0);
                RightArm.setPower(0);
            }

            // claw open
            if (gamepad1.a) {
                Claw.setPosition(0.7);
            }

            // claw close
            if (gamepad1.b) {
                Claw.setPosition(0.25);
            }

            // if button is pressed, wrist servo is disabled and gravity takes over
            if (gamepad1.start) {
                Wrist.getController().pwmDisable();
            }

            // move wrist down
            if (gamepad1.x) {
                Wrist.getController().pwmEnable();
                Wrist.setPosition(0.52);
            }

            // move wrist up
            if (gamepad1.y) {
                Wrist.getController().pwmEnable();
                Wrist.setPosition(0.9);
            }

            // reset
            if (gamepad1.back) {
                DroneLauncher.getController().pwmEnable();
                DroneLauncher.setPosition(0.6);

            }

            // launch drone
            if (gamepad1.touchpad) {
                DroneLauncher.getController().pwmEnable();
                DroneLauncher.setPosition(0.3);

            }

            // pixel holder
            if (gamepad1.dpad_up) {
                PixelHolder.getController().pwmEnable();
                PixelHolder.setPosition(1.0);

            }

            // pixel holder
            if (gamepad1.dpad_down) {
                PixelHolder.getController().pwmEnable();
                PixelHolder.setPosition(0.0);

            }

            // provide claw position
            telemetry.addData("claw", Claw.getPosition());
            telemetry.addData("suspension ", RightSuspension.getCurrentPosition());
            telemetry.update();

            /*
             * if (gamepad1.left_bumper || gamepad1.right_bumper) {
             * Claw.setPosition(ClawOpenPosition);
             * }
             * else {
             * Claw.setPosition(ClawClosedPosition);
             * }
             */

            /*
             * if (gamepad1.left_trigger>0){
             * LeftArm.setPower(-0.2);
             * RightArm.setPower(-0.2);
             * }
             */

            /*
             * if (gamepad1.right_trigger>0){
             * head.setPower(1);
             * }
             * 
             * else if (gamepad1.left_trigger>0){
             * head.setPower(-1);
             * }
             * 
             * else {
             * head.setPower(0);
             * }
             */

            // RightFront.setPower(0.5*(yleft-xleft));
            // LeftFront.setPower(0.5*(yleft+xleft));
            // RightBack.setPower(0.5*(yleft-xleft));
            // LeftBack.setPower(0/5*(yleft+xleft));
        }
    }

    /**
     * Rotates the mecanum wheels until they reach a certain position
     * 
     * @ param LeftFrontTarget
     * @ param LeftBackTarget
     * @ param speed
     * @ return void
     */
    private void raiseSuspension(int LeftSuspensionTarget, int RightSuspensionTarget, double speed) {
        //
        LeftSuspensionPos += LeftSuspensionTarget;
        RightSuspensionPos += RightSuspensionTarget;
        //
        LeftSuspension.setTargetPosition(LeftSuspensionPos);
        RightSuspension.setTargetPosition(RightSuspensionPos);
        //
        LeftSuspension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightSuspension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //
        LeftSuspension.setPower(speed);
        RightSuspension.setPower(speed);

        while (opModeIsActive() && LeftSuspension.isBusy() && RightSuspension.isBusy()) {
            idle();
            telemetry.addData("suspension position ", RightSuspension.getCurrentPosition());

        }
    }
}