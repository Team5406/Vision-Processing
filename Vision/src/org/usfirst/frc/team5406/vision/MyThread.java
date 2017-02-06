package org.usfirst.frc.team5406.vision;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5406.robot.Robot;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.first.wpilibj.CameraServer;

public class MyThread extends Thread {
	
	//Filters Image
	GripPipeline gripPipeline;
	//Axis Camera
	AxisCamera axisCamera;
	
	//Used to filter
	Mat source = new Mat();
    
	//Converts Image into Mat
    CvSink cvSink;
    
	/**
	 * Test Thread Used to mimic Vision Thread
	 * @param gripPipeline
	 * @param axisCamera
	 */
	public MyThread(GripPipeline gripPipeline, AxisCamera axisCamera)
	{
		this.gripPipeline = gripPipeline;
		this.axisCamera = axisCamera;
		cvSink = CameraServer.getInstance().getVideo(axisCamera);
	}
	
	@Override
	public void run()
	{
		System.out.println("Thread Running");
		
		while(!interrupted())
		{
			System.out.println("Frame to source");
			//Puts frame into source
			cvSink.grabFrame(source);
			
			System.out.println("Process");
			//Filters source
			gripPipeline.process(source);
			
			System.out.println("Sets Center");
			//Sets centerX to value (Please apply a synchronized block somehwere here
			if (!gripPipeline.findContoursOutput().isEmpty()) {
				//Creates a rectangle for the Contour
	            Rect r = Imgproc.boundingRect(gripPipeline.findContoursOutput().get(0));
	            Robot.centerX = (double) (r.x + (r.width / 2));
	        }
			
			System.out.println("Thread GO");
		}
	}

}
