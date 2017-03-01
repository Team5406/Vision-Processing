package org.usfirst.frc.team5406.vision;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5406.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionRunner.Listener;


public class CeltXListener implements Listener<GripPipeline>{

	@Override
	public void copyPipelineOutputs(GripPipeline pipeline) {
		
		if(!pipeline.findContoursOutput().isEmpty()) {
			//Creates a rectangle for the Contour
            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
            //Finds the value of the center of the Tape
            Robot.centerX = (double) (r.x + (r.width / 2));
            
            SmartDashboard.putNumber("Width", r.width);
            SmartDashboard.putNumber("Tape Y", r.y);
            
            SmartDashboard.putBoolean("isTapeVisible", true);
            
            /*contourWidth = r.width;
            
            imageWidth = (BOILER_DIAMETER / contourWidth) * CAMERA_WIDTH;
            contourDistance = (imageWidth / 2) / (Math.tan(WIDTH_ANGLE));
            
            distance = Math.cos(CAMERA_ANGLE) * contourDistance;
            
            SmartDashboard.putNumber("Image Width", imageWidth);
            SmartDashboard.putNumber("ContourDistance", contourDistance);
            SmartDashboard.putNumber("Distance", distance);*/
        }
		else
		{
			Robot.centerX = 0;
			SmartDashboard.putBoolean("isTapeVisible", false);
		}
		
	}
	


}
