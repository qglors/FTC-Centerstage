package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutonRedShort")
public class AutonRedShort extends LinearOpMode {

    // initialize beacon position
    private String beaconOnSpikeLine = "right";

    // initialize drive train motors
    private DcMotor LeftFront;
    private DcMotor RightFront;
    private DcMotor LeftBack;
    private DcMotor RightBack;

    // initialize arm motors
    private DcMotor LeftArm;
    private DcMotor RightArm;

    // initialize claw, wrist, and pixel holder servos
    private Servo Claw;
    private Servo Wrist;
    private Servo PixelHolder;

    // initialize drive train motor positions to 0
    private int LeftFrontPos = 0;
    private int LeftBackPos = 0;
    private int RightFrontPos = 0;
    private int RightBackPos = 0;

    // initialize arm motor positions to 0
    private int RightArmPos = 0;
    private int LeftArmPos = 0;

    @Override
    public void runOpMode() {

        //////////////////////////////////////////////////////////////////////////////////
        // SETUP: DECLARE VARIABLES AND WAIT FOR START
        //////////////////////////////////////////////////////////////////////////////////

        // declare variables for drive train motors
        LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        RightFront = hardwareMap.get(DcMotor.class, "RightFront");
        LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        RightBack = hardwareMap.get(DcMotor.class, "RightBack");

        // declare variables for arm motors
        RightArm = hardwareMap.get(DcMotor.class, "RightArm");
        LeftArm = hardwareMap.get(DcMotor.class, "LeftArm");

        // declare variables for claw and wrist servos
        Claw = hardwareMap.get(Servo.class, "Claw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        PixelHolder = hardwareMap.get(Servo.class, "PixelHolder");

        // reset encoders
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // reset encoders
        RightArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // reverse Left side wheel motors
        LeftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        // reverse right arm motor
        RightArm.setDirection(DcMotorSimple.Direction.REVERSE);

        // send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // wait for the game to start (driver presses PLAY)
        waitForStart();

        //////////////////////////////////////////////////////////////////////////////////
        // USE WEBCAM TO SEE DETECT THE BEACON POSITION
        //////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////
        // RUN AUTONOMOUS DEPENDING ON THE BEACON POSITION:
        // PLACE PURPLE PIXEL ON THE SPIKE LINE WITH THE BEACON
        // PLACE YELLOW PIXEL ON THE BACKDROP
        //////////////////////////////////////////////////////////////////////////////////

        // if the beacon is on the middle spike line, run autonMiddle
        if (beaconOnSpikeLine.equals("middle")) {
            autonMiddle();
        }

        // if the beacon is on the left spike line, run autonLeft
        else if (beaconOnSpikeLine.equals("left")) {
            autonLeft();
        }

        // if the beacon is on the right spike line, run autonRight
        else if (beaconOnSpikeLine.equals("right")) {
            autonRight();
        }
    }

    /**
     * Places the pixel on the middle spike line, then places yellow pixel on the
     * backboard
     * 
     * @ return void
     */
    private void autonMiddle() {

        // grip pixel tighter
        Claw.setPosition(0.7);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot forwards to the pixel line
        drive(1350, 1350, 1350, 1350, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe right sideways until bot gets close to the backdrop
        drive(1400, -1400, -1400, 1400, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // turn bot counter clockwise so it faces backwards to the backdrop
        drive(-800, -800, 800, 800, 0.5); // ADJUST THESE LMAO TO TURN CLOCKWISE

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.85);

        // wait 1 sec
        waitMilisecs(1000);

        // move slightly left to align with middle april tag
        drive(-500, 500, 500, -500, 0.5);

        // raise arm until it hits the backdrop
        raiseArm(225, 225, 0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards until bot hits the backdrop
        drive(-200, -200, -200, -200, 0.2);

        // wait 1 sec
        waitMilisecs(1000);

        // open claw to drop the pixel on the backdrop
        Claw.setPosition(0.3);

        // wait 1 sec
        waitMilisecs(1000);

        // send telemetry message
        telemetry.addData("arm is about to start lowering: ", RightArm.getCurrentPosition());
        telemetry.update();

        // lower arm
        raiseArm(-225, -225, 0.3);

        // send telemetry message
        telemetry.addData("arm lowered: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(1000);

        // drive bot slightly forward
        drive(100, 100, 100, 100, 0.5);

        // strafe slightly right to align with inside parking
        drive(1000, -1000, -1000, 1000, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards to inside parking
        drive(-300, -300, -300, -300, 0.5);
    }

    /**
     * Places the pixel on the left spike line, then places yellow pixel on the
     * backboard
     * 
     * @ return void
     */
    private void autonLeft() {

        // grip pixel tighter
        Claw.setPosition(0.7);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot forwards to the pixel lines
        drive(1000, 1000, 1000, 1000, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // turn bot counter clockwise so it faces backwards to the backdrop
        drive(-850, -850, 850, 850, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot forwards to the left pixel line
        drive(380, 380, 380, 380, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // move backwards until bot gets close to backdrop
        drive(-1700, -1700, -1700, -1700, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe slightly right to align with left april tag
        drive(270, -270, -270, 270, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.85);

        // wait 1 sec
        waitMilisecs(1000);

        // raise arm until it hits the backdrop
        raiseArm(235, 235, 0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards until bot hits the backdrop
        drive(-70, -70, -70, -70, 0.2);

        // wait 1 sec
        waitMilisecs(1000);

        // open claw to drop the pixel on the backdrop
        Claw.setPosition(0.3);

        // wait 1 sec
        waitMilisecs(1000);

        // send telemetry message
        telemetry.addData("arm is about to start lowering: ", RightArm.getCurrentPosition());
        telemetry.update();

        // lower arm
        raiseArm(-235, -235, 0.3);

        // send telemetry message
        telemetry.addData("arm lowered: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(1000);

        // drive bot slightly forward
        drive(100, 100, 100, 100, 0.5);

        // strafe slightly right to align with inside parking
        drive(800, -800, -800, 800, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards to inside parking
        drive(-300, -300, -300, -300, 0.5);
    }

    /**
     * Places the pixel on the right spike line, then places yellow pixel on the
     * backboard
     * 
     * @ return void
     */
    private void autonRight() {

        // grip pixel tighter
        Claw.setPosition(0.7);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot forwards to the pixel lines
        drive(1000, 1000, 1000, 1000, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // turn bot counter clockwise so it faces backwards to the backdrop
        drive(-850, -850, 850, 850, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot backwards to the right pixel line
        drive(-400, -400, -400, -400, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // move backwards until bot gets close to backdrop
        drive(-1200, -1200, -1200, -1200, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe slightly left to align with right april tag
        drive(-800, 800, 800, -800, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move backwards until bot hits backdrop
        drive(-100, -100, -100, -100, 0.3);

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.9);

        // wait 1 sec
        waitMilisecs(1000);

        // raise arm until it hits the backdrop
        raiseArm(220, 220, 0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // open claw to drop the pixel on the backdrop
        Claw.setPosition(0.3);

        // wait 1 sec
        waitMilisecs(1000);

        // send telemetry message
        telemetry.addData("arm is about to start lowering: ", RightArm.getCurrentPosition());
        telemetry.update();

        // lower arm
        raiseArm(-230, -230, 0.3);

        // send telemetry message
        telemetry.addData("arm lowered: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(1000);

        // drive bot slightly forward
        drive(100, 100, 100, 100, 0.5);

        // strafe slightly left to align with inside parking
        drive(1300, -1300, -1300, 1300, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards to inside parking
        drive(-300, -300, -300, -300, 0.5);
    }

    /**
     * Rotates the mecanum wheels until they reach a certain position
     * 
     * @ param LeftFrontTarget
     * @ param LeftBackTarget
     * @ param RightFrontTarget
     * @ param RightBackTarget
     * @ param speed
     * @ return void
     */
    private void drive(int LeftFrontTarget, int LeftBackTarget, int RightFrontTarget, int RightBackTarget,
            double speed) {
        //
        LeftFrontPos += LeftFrontTarget;
        LeftBackPos += LeftBackTarget;
        RightFrontPos += RightFrontTarget;
        RightBackPos += RightBackTarget;
        //
        LeftFront.setTargetPosition(LeftFrontPos);
        LeftBack.setTargetPosition(LeftBackPos);
        RightFront.setTargetPosition(RightFrontPos);
        RightBack.setTargetPosition(RightBackPos);
        //
        LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //
        LeftFront.setPower(speed);
        LeftBack.setPower(speed);
        RightFront.setPower(speed);
        RightBack.setPower(speed);
        //
        while (opModeIsActive() && LeftFront.isBusy() && LeftBack.isBusy() && RightFront.isBusy()
                && RightBack.isBusy()) {
            idle();
        }
    }

    /**
     * Rotates the arm motors until they reach a certain position
     * 
     * @ param rightArmTarget
     * @ param leftArmTarget
     * @ param speed
     * @ return void
     */
    private void raiseArm(int RightArmTarget, int LeftArmTarget, double speed) {
        //
        RightArmPos += RightArmTarget;
        LeftArmPos += LeftArmTarget;
        //
        RightArm.setTargetPosition(RightArmPos);
        LeftArm.setTargetPosition(LeftArmPos);
        //
        RightArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //
        RightArm.setPower(speed);
        LeftArm.setPower(speed);
        //
        while (opModeIsActive() && RightArm.isBusy() && LeftArm.isBusy()) {
            idle();
        }
    }

    /**
     * Pause the code for a certain amount of time
     * 
     * @ param secs The number of secs you want to wait
     * @ return void
     */
    private void waitMilisecs(int milisecs) {
        try {
            Thread.sleep(milisecs);
        } catch (InterruptedException e) {
        }
    }

}

Mecum drive 2

package org.firstinspires.ftc.teamcode
;

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
    // private DcMotor head = null;
    private Servo Wrist = null;
    private Servo Claw = null;
    private Servo DroneLauncher = null;
    private Servo PixelHolder = null;

    @Override
    public void runOpMode() {

        // set speed
        double speed = 1.0;

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

        // declare variables for claw and wrist servos
        Claw = hardwareMap.get(Servo.class, "Claw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");

        // declare variable for drone launcher
        DroneLauncher = hardwareMap.get(Servo.class, "DroneLauncher");

        PixelHolder = hardwareMap.get(Servo.class, "PixelHolder");

        // declare variables for arm motors
        // LeftArm = hardwareMap.get(____, "Left");
        // RightArm = hardwareMap.get(____, "Right");

        // LeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        // need for mecanum

        // reverse left side wheel motors
        LeftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightArm.setDirection(DcMotorSimple.Direction.REVERSE);

        // Brakes the arm when inactive so arm goes down slower instead of slamming
        // cause of gravity
        LeftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            double LeftFrontPower = (yleft - xleft - rxleft) / denominator;
            double LeftBackPower = (yleft + xleft + rxleft) / denominator;
            double RightFrontPower = (yleft + xleft + rxleft) / denominator;
            double RightBackPower = (yleft - xleft - rxleft) / denominator;

            // right joystick speed + controls
            RightFront.setPower(speed * (y - x));
            LeftFront.setPower(speed * (y + x));
            RightBack.setPower(speed * (y - x));
            LeftBack.setPower(speed * (y + x));

            // left joystick speed
            RightFront.setPower(0.9 * (LeftFrontPower));
            LeftFront.setPower(0.9 * (LeftBackPower));
            RightBack.setPower(0.9 * (RightFrontPower));
            LeftBack.setPower(0.9 * (RightBackPower));

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
             * }
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
                Wrist.setPosition(0.55);
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
}