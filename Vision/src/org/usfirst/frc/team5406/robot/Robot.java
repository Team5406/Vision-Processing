package org.usfirst.frc.team5406.robot;


import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5406.vision.GripPipeline;
import org.usfirst.frc.team5406.vision.MyThread;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Robot extends IterativeRobot {
	
	//IP of the axis camera
	final static String AXIS_IP = "169.254.47.133";
	
	final static int TURRET_MOTOR = 9; //Not sure what actual ID is but i like the number 9
	
	//Center of the contour
	public static double centerX = 0.0;
	
	CANTalon turretMotor;
	
	//Filters Image
	GripPipeline gripPipeline;
	//Camera being used
	AxisCamera axisCamera;
	//Runs Vision Scanning
	MyThread thread;
	
	//Thread supplied by frc
	//VisionThread frcThread;
	
	@Override
	public void robotInit() {
		
		System.out.println("INIT");
		
		turretMotor = new CANTalon(TURRET_MOTOR);
		
		//For when the turret is needed
		turretMotor.changeControlMode(TalonControlMode.PercentVbus);
		turretMotor.setVoltageRampRate(50);
		
		//Instantiate items
		gripPipeline = new GripPipeline();
		axisCamera = CameraServer.getInstance().addAxisCamera(AXIS_IP);
		thread = new MyThread(gripPipeline, axisCamera);
		
		//Starts and runs thread
		thread.start();
		
		/* Code supplied by FRC website. I tried to make my own thread that mimics this thread to find the problem
		 * frcThread =  new VisionThread(axisCamera, gripPipeline, new VisionRunner.Listener<GripPipeline>()
				{

					@Override
					public void copyPipelineOutputs(GripPipeline pipeline) {
						
						if(!pipeline.findContoursOutput().isEmpty())
						{
							Rect r = Imgproc.boundingRect(pipeline.findContoursOutput().get(0));
							centerX = r.x + (r.width / 2);
						}
					}
				});
        frcThread.start();*/
		
        System.out.println("INIT Done");
        
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//Displays center of contour on dashboard
		SmartDashboard.putNumber("Contour", centerX);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

