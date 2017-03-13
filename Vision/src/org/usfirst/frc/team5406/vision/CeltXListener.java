package org.usfirst.frc.team5406.vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5406.robot.Robot;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionRunner.Listener;


public class CeltXListener implements Listener<GripPipelineM1004>{

	CvSource output;
	
	public CeltXListener()
	{
		super();
		
		output = CameraServer.getInstance().putVideo("Contour", 480, 320);
		
	}
	
	@Override
	public void copyPipelineOutputs(GripPipelineM1004 pipeline) {
		
		Mat contourFrame = pipeline.hsvThresholdOutput();
		
		SmartDashboard.putNumber("Rand", Math.random());
		
		if(!pipeline.findContoursOutput().isEmpty()) {
			//Creates a rectangle for the Contour
            
 
            MatOfPoint2f smallContour = new MatOfPoint2f();
            MatOfPoint2f bigContour = new MatOfPoint2f();
            
            int small = 0;
            
            for(int i = 0; i < pipeline.findContoursOutput().size(); i++)
            	if(Imgproc.boundingRect(pipeline.findContoursOutput().get(i)).width > 5)
            	{
            		small = i;
            		break;
            	}
            
            
            
            pipeline.findContoursOutput().get(small).convertTo(smallContour, CvType.CV_32F);
            
            Rect r = Imgproc.boundingRect(pipeline.findContoursOutput().get(small));
            
            int big = 0;
            
            for(int i = small + 1; i < pipeline.findContoursOutput().size(); i++)
            	if(Imgproc.boundingRect(pipeline.findContoursOutput().get(i)).width > 5)
            	{
            		big = i;
            		break;
            	}
            pipeline.findContoursOutput().get(big).convertTo(bigContour, CvType.CV_32F);
            
            RotatedRect smallRect = Imgproc.minAreaRect(smallContour);
            RotatedRect bigRect = Imgproc.minAreaRect(bigContour);
            
            double bigHeight = bigRect.size.width < bigRect.size.height ? bigRect.size.width : bigRect.size.height;
            double smallHeight = smallRect.size.width < smallRect.size.height ? smallRect.size.width : smallRect.size.height;
            
            if(bigHeight < smallHeight)
            {
            	RotatedRect temp = bigRect;
            	bigRect = smallRect;
            	smallRect = temp;
            }
            
            
            Point smallRectVertices[] = new Point[4];
            Point bigRectVertices[] = new Point[4];
            
            Point topRight = bigRectVertices[0];
            Point bottomLeft = smallRectVertices[0];
            	
            smallRect.points(smallRectVertices);
            bigRect.points(bigRectVertices);
            
            for(int i = 0; i < bigRectVertices.length - 1; i++)
            	for(int y = 0; y < bigRectVertices.length - 1 - i; y++)
            		if(bigRectVertices[y].x < bigRectVertices[y + 1].x)
            		{
            			Point temp = bigRectVertices[y];
            			bigRectVertices[y] = bigRectVertices[y + 1];
            			bigRectVertices[y + 1] = temp;
            		}
            
            topRight = bigRectVertices[0].y < bigRectVertices[1].y ? bigRectVertices[0]
            		: bigRectVertices[1];
            
            for(int i = 0; i < smallRectVertices.length - 1; i++)
            	for(int y = i; y < smallRectVertices.length - 1; y++)
            		if(smallRectVertices[y].x > smallRectVertices[y + 1].x)
            		{
            			Point temp = smallRectVertices[y];
            			smallRectVertices[y] = smallRectVertices[y + 1];
            			smallRectVertices[y + 1] = temp;
            		}
            
            bottomLeft = smallRectVertices[0].y > smallRectVertices[1].y ? smallRectVertices[0]
            		: smallRectVertices[1];
            
            //Finds the value of the center of the Tape
            Robot.centerX = bottomLeft.x + ((topRight.x - bottomLeft.x) / 2);
            
            SmartDashboard.putNumber("CenterX", Robot.centerX);
            SmartDashboard.putNumber("CenterC", bigRect.center.x);
            
            SmartDashboard.putString("Top Right", topRight.x + " , " + topRight.y);
            SmartDashboard.putString("Bottom Left", bottomLeft.x + " , " + bottomLeft.y);
            
            SmartDashboard.putNumber("VWidth", smallRect.size.width);
            SmartDashboard.putNumber("VHeight", smallRect.size.height);
            SmartDashboard.putNumber("VTape Y", r.y);
            
            SmartDashboard.putNumber("RWidth", bigRect.size.width);
            SmartDashboard.putNumber("RHeight", bigRect.size.height);
            SmartDashboard.putNumber("RAngle", bigRect.angle);
            
            for(int i = 0; i < 4; i++)
            	SmartDashboard.putString("Vertex " + i, bigRectVertices[i].x + ", " + bigRectVertices[i].y);
            
            for(int i = 0; i < 4; i++)
            	SmartDashboard.putString("smallVertex " + i, smallRectVertices[i].x + ", " + smallRectVertices[i].y);
            
            SmartDashboard.putBoolean("isTapeVisible", true);
            
            double length = Math.sqrt(Math.pow(topRight.x - bottomLeft.x, 2) + Math.pow(topRight.y - bottomLeft.y, 2));
            
            SmartDashboard.putNumber("Length", length);
            
            for(int i = 0; i < 4; i++)
            	Imgproc.line(contourFrame, bigRectVertices[i], bigRectVertices[(i + 1) % 4], new Scalar(255, 0, 0), 1, 8, 0);
            for(int i = 0; i < 4; i++)
            	Imgproc.line(contourFrame, smallRectVertices[i], smallRectVertices[(i + 1) % 4], new Scalar(255, 0, 0), 1, 8, 0);
            /*
             * Camera filters
             * 
             * Color Level: 100
             * Brightness: 50
             * Sharpness: 50
             * Contrast: 100
             * 
             * White Balance: Fixed Indoor
             * 
             * Exposure: 4
             * 
             * Backlight: false
             */
            SmartDashboard.putNumber("Contour #", pipeline.findContoursOutput().size());
            
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
		
		
		output.putFrame(contourFrame);
	}
	


}
