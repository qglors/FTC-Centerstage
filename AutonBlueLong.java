package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutonBlueLong")
public class AutonBlueLong extends LinearOpMode {

    // initialize drive train motors
    private DcMotor LeftFront;
    private DcMotor RightFront;
    private DcMotor LeftBack;
    private DcMotor RightBack;

    // initialize claw and wrist servos
    private Servo Claw;
    private Servo Wrist;

    // initialize motor positions
    private int LeftFrontPos;
    private int LeftBackPos;
    private int RightFrontPos;
    private int RightBackPos;

    @Override
    public void runOpMode() {

        // declare variables for drive train motors
        LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        RightFront = hardwareMap.get(DcMotor.class, "RightFront");
        LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        RightBack = hardwareMap.get(DcMotor.class, "RightBack");

        // declare variables for claw and wrist servos
        Claw = hardwareMap.get(Servo.class, "Claw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");

        // reset encoders
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // reverse Left side wheel motors
        LeftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        // set motor positions to 0
        LeftFrontPos = 0;
        LeftBackPos = 0;
        RightFrontPos = 0;
        RightBackPos = 0;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        /////////////////////////////////////////////////
        // START AUTON
        /////////////////////////////////////////////////

        // grip pixel tighter
        Claw.setPosition(0.3);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot backwards to the pixel line
        drive(-1800, -1800, -1800, -1800, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // put the wrist down
        Wrist.setPosition(0.6);

        // wait 2 secs
        waitMilisecs(2000);

        // open the claw to drop the pixel on the line
        Claw.setPosition(0.7);

        // wait 1 sec
        waitMilisecs(1000);

        // lift the wrist back up
        Wrist.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // move slightly backwards
        drive(-300, -300, -300, -300, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe right sideways, through the middle gate, until bot reaches parking
        drive(3800, -3800, -3800, 3800, 0.9);

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
