package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutonBlueShort")
public class AutonBlueShort extends LinearOpMode {

    // initialize beacon position
    private String beaconOnSpikeLine = "left";

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

        // move bot backwards until bot is not hovering over the purple pixel
        drive(-800, -800, -800, -800, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe a little left
        drive(-50, 50, 50, -50, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // turn bot clockwise so it faces backwards to the backdrop
        drive(830, 830, -830, -830, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot backwards until bot gets close to the backdrop
        drive(-950, -950, -950, -950, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.85);

        // wait 1 sec
        waitMilisecs(1000);

        // move slightly left to align with middle april tag
        drive(-620, 620, 620, -620, 0.5);

        // raise arm until it hits the backdrop
        raiseArm(225, 225, 0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards until bot hits the backdrop
        drive(-250, -250, -250, -250, 0.2);

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

        // strafe slightly left to align with inside parking
        drive(-1080, 1080, 1080, -1080, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards to inside parking
        drive(-330, -300, -300, -300, 0.5);
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

        // turn bot clockwise so it faces backwards to the backdrop
        drive(850, 850, -850, -850, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot forwards to the left pixel line
        drive(475, 475, 475, 475, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // move backwards until bot gets close to backdrop
        drive(-1600, -1600, -1600, -1600, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe slightly left to align with right april tag
        drive(-350, 350, 350, -350, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.85);

        // wait 1 sec
        waitMilisecs(1000);

        // raise arm until it hits the backdrop
        raiseArm(220, 220, 0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards until bot hits the backdrop
        drive(-300, -300, -300, -300, 0.2);

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
        drive(-700, 700, 700, -700, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards to inside parking
        drive(-300, -300, -300, -300, 0.5);
    }

    /**
     * Places the purple pixel on the left spike line, then places yellow pixel on
     * the backboard
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

        // turn bot clockwise so it faces backwards to the backdrop
        drive(850, 850, -850, -850, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot backwards to the left pixel line
        drive(-280, -280, -280, -280, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(0.95);

        // wait 1 sec
        waitMilisecs(1000);

        // move backwards until bot gets close to backdrop
        drive(-1050, -1050, -1050, -1050, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe slightly right to align with left april tag
        drive(370, -370, -370, 370, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.90);

        // wait 1 sec
        waitMilisecs(1000);

        // raise arm until it hits the backdrop
        raiseArm(235, 235, 0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards until bot hits the backdrop
        drive(-50, -50, -50, -50, 0.1);

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
        raiseArm(-245, -245, 0.3);

        // send telemetry message
        telemetry.addData("arm lowered: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(1000);

        // drive bot slightly forward
        drive(100, 100, 100, 100, 0.5);

        // strafe slightly left to align with inside parking
        drive(-1300, 1300, 1300, -1300, 0.5);

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