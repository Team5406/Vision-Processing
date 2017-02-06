package org.usfirst.frc.team5406.parts;

import edu.wpi.first.wpilibj.Joystick;

public class XboxController extends Joystick{

	//Trying out enum for button value
	//Value of the buttons
	public static enum XboxButton
	{
		A_BUTTON(1), B_BUTTON(2), X_BUTTON(3), Y_BUTTON(4),
		LEFT_BUMPER(5), RIGHT_BUMPER(6), BACK_BUTTON(7), START_BUTTON(8),
		LEFT_STICK(9), RIGHT_STICK(10);
		
		private final int value;
		
		private XboxButton(int value) { this.value = value; }
		public int getValue() { return value; }
	}
	
	//Prolly the joysticks and trigger values. Ask Pinto
	public static final int LEFT_X_AXIS = 0;
	public static final int LEFT_Y_AXIS = 1;
	public static final int LEFT_TRIGGER_AXIS = 2;
	public static final int RIGHT_TRIGGER_AXIS = 3;
	public static final int RIGHT_X_AXIS = 4;
	public static final int RIGHT_Y_AXIS = 5;
	
	//Region where 0 is set to be to remove small movements being registered
	private static final double xboxControllerDeadband = 0.2;
	
	//Used for the switch to get direction
	public static enum DirectionPad
	{
		UP, DOWN, LEFT, RIGHT, NONE
	}
	
	/**
	 * 
	 * @param port The port number it is plugged into
	 */
	public XboxController(int port) 
	{
		super(port);
	}

	/**
	 * 
	 * @param button Desired button number you want to check.
	 * @return true if button is pressed.
	 */
	public boolean isButtonPressed(XboxButton button)
	{
		return super.getRawButton(button.getValue());
	}
	
	//Getters and setters
	public double getLeftX() { return super.getRawAxis(LEFT_X_AXIS); }
	public double getLeftY() { return -super.getRawAxis(LEFT_Y_AXIS); }
	
	public double getRightX() { return super.getRawAxis(RIGHT_X_AXIS); }
	public double getRightY() { return -super.getRawAxis(RIGHT_Y_AXIS); }
	
	//Deadbands used to eliminate small movement to be registered
	public double getLeftTrigger(){ return super.getRawAxis(LEFT_TRIGGER_AXIS); }
	public boolean getLeftTriggerPressed(){ return Math.abs(super.getRawAxis(LEFT_TRIGGER_AXIS)) > xboxControllerDeadband; }
	
	public double getRightTrigger() { return super.getRawAxis(RIGHT_TRIGGER_AXIS); }
	public boolean getRightTriggerPressed(){ return Math.abs(super.getRawAxis(RIGHT_TRIGGER_AXIS)) > xboxControllerDeadband; }
	
	//Get value of the direction pad
	public DirectionPad getDirectionPad()
	{
		int direction = super.getPOV();
		switch(direction)
		{
		default:
			return DirectionPad.NONE;
		case 0:
			return DirectionPad.UP;
		case 90:
			return DirectionPad.RIGHT;
		case 180:
			return DirectionPad.DOWN;
		case 270:
			return DirectionPad.LEFT;
		}
	}
	
}
