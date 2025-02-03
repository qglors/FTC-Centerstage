package org.firstinspires.ftc.teamcode;

// import packages
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
// import packages needed for camera vision
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import java.util.List;

@Autonomous(name = "AutonRedShort")
public class AutonRedShort extends LinearOpMode {

    //////////////////////////////////////////////////////////////////////////////////
    // INITIALIZE VARIABLES FOR CAMERA VISION
    //////////////////////////////////////////////////////////////////////////////////

    // initialize beaconDetected to false
    private boolean beaconDetected = false;

    // initialize beacon position to the LEFT spike line
    private String beaconPosition = "left";

    // initialize timeElapsed to 0
    private int timeElapsed = 0; // measures how long the camera vision has taken

    // stores the name of our tensorflow file
    private static final String TFOD_MODEL_FILE = "model_20240113_160019.tflite";

    // stores our labels for the blue beacon
    private static final String[] LABELS = { "red", "r", "re", "e" };

    // true for webcam, false for phone camera
    private static final boolean USE_WEBCAM = true;

    // stores our instance of the TensorFlow Object Detection processor
    private TfodProcessor tfod;

    // stores our instance of the vision portal
    private VisionPortal visionPortal;

    //////////////////////////////////////////////////////////////////////////////////
    // INITIALIZE VARIABLES FOR ROBOT PARTS
    //////////////////////////////////////////////////////////////////////////////////

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

        // initialize the TensorFlow Object Detection processor
        initTfod();

        //////////////////////////////////////////////////////////////////////////////////
        // DECLARE VARIABLES FOR ROBOT PARTS
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

        // reverse certain wheel motors
        // RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        // reverse certain arm motors
        RightArm.setDirection(DcMotorSimple.Direction.REVERSE);

        // Brakes the arm when inactive so arm goes down slower instead of slamming
        // cause of gravity
        LeftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        //////////////////////////////////////////////////////////////////////////////////
        // WAIT FOR THE DRIVER TO PRESS PLAY
        //////////////////////////////////////////////////////////////////////////////////

        waitForStart();

        //////////////////////////////////////////////////////////////////////////////////
        // MOVE THE BOT FORWARDS CLOSER TO THE BLUE SPIKE LINES
        //////////////////////////////////////////////////////////////////////////////////

        drive(440, 440, 440, 440, 0.5); // bot cannot see objects from too far away :(

        //////////////////////////////////////////////////////////////////////////////////
        // USE WEBCAM TO SEE DETECT THE BEACON POSITION
        //////////////////////////////////////////////////////////////////////////////////

        if (opModeIsActive()) {

            // keep updating the object recognition info until the beacon is detected or
            // until ~7 seconds has psased
            while (beaconDetected == false && timeElapsed < 250) {

                // determine beacon position based on the TensorFlow Objext Detection (TFOD)
                // recognitions
                determineBeaconPosition();

                // push telemetry to the Driver Station
                telemetry.update();

                // save CPU resources by only resuming streaming when needed
                visionPortal.resumeStreaming();

                // share the CPU
                sleep(20);

                // increment timeElapsed
                timeElapsed += 1;
            }
        }

        // close the camera once the beacon is detected to save CPU
        visionPortal.close();

        //////////////////////////////////////////////////////////////////////////////////
        // RUN AUTONOMOUS DEPENDING ON THE BEACON POSITION:
        // PLACE PURPLE PIXEL ON THE SPIKE LINE WITH THE BEACON
        // PLACE YELLOW PIXEL ON THE BACKDROP
        //////////////////////////////////////////////////////////////////////////////////

        // if the beacon is on the middle spike line, run autonMiddle
        if (beaconPosition.equals("middle")) {
            autonMiddle();
        }

        // if the beacon is on the left spike line, run autonLeft
        else if (beaconPosition.equals("left")) {
            autonLeft();
        }

        // if the beacon is on the right spike line, run autonRight
        else if (beaconPosition.equals("right")) {
            autonRight();
        }
    }

    /**
     * initializes the TensorFlow Object Detection processor
     * 
     * @return void
     */
    private void initTfod() {

        // create a new TensorFlow processor using a builder
        tfod = new TfodProcessor.Builder()

                // set the model file name to the blue beacon .tflite
                .setModelFileName(TFOD_MODEL_FILE)

                // set the model labels
                .setModelLabels(LABELS)

                // build the tensorflow processer
                .build();

        // create a new vision portal by using a builder
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // set the webcam to "Webcam 1"
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // enable live preview
        builder.enableLiveView(true);

        // show the live camera view without annotations
        builder.setAutoStopLiveView(false);

        // set the confidence threshold for TFOD recognitions
        tfod.setMinResultConfidence(0.40f);

        // set and enable the TFOD processor
        builder.addProcessor(tfod);

        // build the vision portal using the above settings
        visionPortal = builder.build();
    }

    /**
     * Determines the beacon position based on the TensorFlow Objext Detection
     * (TFOD) recognitions
     * 
     * @return void
     */
    private void determineBeaconPosition() {

        // display the number of objects detected
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // loop through each object detected
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2;
            double y = (recognition.getTop() + recognition.getBottom()) / 2;

            // determine the beacon position based on the x position of each object
            // detectedd
            if (x > 400) {
                beaconPosition = "right";
            } else if (x > 50 && x < 280) {
                beaconPosition = "middle";
            } else {
                beaconPosition = "left";
            }

            // set beaconDetected to true
            beaconDetected = true;

            // display info for each object detected
            telemetry.addData("", " ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
            telemetry.addData("Beacon Position", beaconPosition);
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
        drive(910, 910, 910, 910, 0.5);

        // wait 0.5 secs
        waitMilisecs(1000);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot backwards until bot is not hovering over the purple pixel
        drive(-800, -800, -800, -800, 0.3);

        // wait 1 sec
        waitMilisecs(1000);

        // strafe a little right
        drive(50, -50, -50, 50, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // turn bot counter clockwise so it faces backwards to the backdrop
        drive(-830, -830, 830, 830, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot backwards until bot gets close to the backdrop
        drive(-1000, -1000, -1000, -1000, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // lift wrist slightly up
        Wrist.setPosition(0.89);

        // wait 1 sec
        waitMilisecs(1000);

        // move slightly right to align with middle april tag
        drive(575, -575, -575, 575, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // raise arm until it hits the backdrop
        raiseArm(160, 160, 0.9);

        // wait 1 sec
        waitMilisecs(500);

        // raise arm until it hits the backdrop
        raiseArm(235, 235, 0.9);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards until bot hits the backdrop
        drive(-320, -320, -320, -320, 0.2);

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
        drive(1080, -1080, -1080, 1080, 0.5);

        // wait 1 sec
        waitMilisecs(1000);

        // move bot slightly backwards to inside parking
        drive(-400, -400, -400, -400, 0.5);

        /*
         * ////////corner parking
         * //strafe left to park in corner
         * drive(-990, 990, 990, -990, 0.5);
         * 
         * // wait 1 sec
         * waitMilisecs(700);
         * 
         * //go back
         * drive(-400, -400, -400, -400, 0.5);
         */
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
        waitMilisecs(700);

        // drive the bot forwards to the pixel lines
        drive(530, 530, 530, 530, 0.7);

        // wait 1 sec
        waitMilisecs(700);

        // turn bot clockwise so it faces forwards to the backdrop
        drive(850, 850, -850, -850, 0.5);

        // wait 0.5 secs
        waitMilisecs(700);

        // drive the bot backwards to the left pixel line
        drive(-430, -430, -430, -430, 0.5);

        // wait 0.5 secs
        waitMilisecs(700);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // drive slightly forwards
        drive(600, 600, 600, 600, 0.5);

        // wait 1 sec
        waitMilisecs(500);

        // turn bot 180 degrees counter clockwise so that bot faces backwards to the
        // backdrop
        drive(-1600, -1600, 1600, 1600, 0.3);

        // wait 1 sec
        waitMilisecs(500);

        // move backwards until bot gets close to backdrop
        drive(-1180, -1180, -1180, -1180, 0.3);

        // wait 1 sec
        waitMilisecs(700);

        // strafe slightly right to align with left april tag
        drive(240, -240, -240, 240, 0.5);

        // wait 1 sec
        waitMilisecs(700);

        // lift wrist slightly up
        Wrist.setPosition(0.9);

        // wait 1 sec
        waitMilisecs(700);

        // raise arm until it hits the backdrop
        raiseArm(160, 160, 0.9);

        // wait 1 sec
        waitMilisecs(700);

        // raise arm until it hits the backdrop
        raiseArm(237, 237, 0.9);

        // wait 1 sec
        waitMilisecs(700);

        // move bot slightly backwards until bot hits the backdrop
        drive(-70, -70, -70, -70, 0.2);

        // wait 1 sec
        waitMilisecs(700);

        // open claw to drop the pixel on the backdrop
        Claw.setPosition(0.3);

        // wait 1 sec
        waitMilisecs(700);

        // send telemetry message
        telemetry.addData("arm is about to start lowering: ", RightArm.getCurrentPosition());
        telemetry.update();

        // lower arm
        raiseArm(-235, -235, 0.3);

        // send telemetry message
        telemetry.addData("arm lowered: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(700);

        // drive bot slightly forward
        drive(100, 100, 100, 100, 0.5);

        // comment out parking to park in corner for mechcats

        // strafe slightly right to align with inside parking
        drive(800, -800, -800, 800, 0.5);

        // wait 1 sec
        waitMilisecs(700);

        // move bot slightly backwards to inside parking
        drive(-300, -300, -300, -300, 0.5);

        /*
         * ////////corner parking
         * //strafe left to park in corner
         * drive(-1240, 1240, 1240, -1240, 0.5);
         * 
         * // wait 1 sec
         * waitMilisecs(700);
         * 
         * //go back
         * drive(-400, -400, -400, -400, 0.5);
         */
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
        waitMilisecs(700);

        // drive the bot forwards to the pixel lines
        drive(660, 660, 660, 660, 0.7);

        // wait 1 sec
        waitMilisecs(700);

        // turn bot counter clockwise so it faces backwards to the backdrop
        drive(-850, -850, 850, 850, 0.5);

        // wait 0.5 secs
        waitMilisecs(500);

        // drive the bot backwards to the right pixel line
        drive(-350, -350, -350, -350, 0.5);

        // wait 0.5 secs
        // waitMilisecs(200);

        // strafe slightly right to the right pixel line
        // drive(150, -150, -150, 150, 0.5);

        // wait 0.5 secs
        waitMilisecs(700);

        // open the pixel holder to drop the pixel on the line
        PixelHolder.setPosition(1.0);

        // wait 1 sec
        waitMilisecs(1000);

        // move backwards until bot gets close to backdrop
        drive(-1200, -1200, -1200, -1200, 0.5);

        // wait 1 sec
        waitMilisecs(700);

        // strafe slightly left to align with right april tag
        drive(-850, 850, 850, -850, 0.5);

        // wait 1 sec
        waitMilisecs(700);

        // move backwards until bot hits backdrop
        drive(-100, -100, -100, -100, 0.3);

        // wait 1 sec
        waitMilisecs(700);

        // lift wrist slightly up
        Wrist.setPosition(0.9);

        // wait 1 sec
        waitMilisecs(700);

        // send telemetry message
        telemetry.addData("arm is about to start raising: ", RightArm.getCurrentPosition());
        telemetry.update();

        // raise arm until it hits the backdrop
        raiseArm(160, 160, 0.9);

        // send telemetry message
        telemetry.addData("arm raised: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(700);

        // raise arm until it hits the backdrop
        raiseArm(235, 235, 0.9);

        // wait 1 sec
        waitMilisecs(700);

        // move backwards until bot hits backdrop
        drive(-50, -50, -50, -50, 0.3);

        // wait 1 sec
        waitMilisecs(700);

        // open claw to drop the pixel on the backdrop
        Claw.setPosition(0.3);

        // wait 1 sec
        waitMilisecs(700);

        // send telemetry message
        telemetry.addData("arm is about to start lowering: ", RightArm.getCurrentPosition());
        telemetry.update();

        // lower arm
        raiseArm(-230, -230, 0.3);

        // send telemetry message
        telemetry.addData("arm lowered: ", RightArm.getCurrentPosition());
        telemetry.update();

        // wait 1 sec
        waitMilisecs(700);

        // drive bot slightly forward
        drive(100, 100, 100, 100, 0.5);

        // strafe slightly left to align with inside parking
        drive(1350, -1350, -1350, 1350, 0.5);

        // wait 1 sec
        waitMilisecs(700);

        // move bot slightly backwards to inside parking
        drive(-300, -300, -300, -300, 0.5);

        /*
         * ////////corner parking
         * //strafe left to park in corner
         * drive(-750, 750, 750, -750, 0.5);
         * 
         * // wait 1 sec
         * waitMilisecs(700);
         * 
         * //go back
         * drive(-400, -400, -400, -400, 0.5);
         */
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