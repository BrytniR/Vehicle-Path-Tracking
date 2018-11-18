// Uses an ultrasonic sensor to detect how far the robot is from a wall
// Robot maintains a distance of 20cm away from the wall using proportional
// feedback control and follows the wall for 100cm

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Distance_PControl {
		public static void main(String[] args) {
				EV3UltrasonicSensor sonic = new EV3UltrasonicSensor(SensorPort.S4);
				System.out.println("Ready to go");//displays on LEGO screen

				Motor.B.setSpeed(50);//initialize speed
				Motor.C.setSpeed(50);

				double desired = 20;//desired distance in cm
				double dist = 100;//total distance to travel in cm

				double kp = 40;//proportional feedback constant
				int sampleSize = sonic.sampleSize();
				float[] sonicsample = new float[sampleSize];
				Motor.B.resetTachoCount();//tracks wheel distance
				double angle_rotated = 0;
				double total_dist = 0;

				while(total_dist < dist){//while total 100cm has not been completed
							sonic.fetchSample(sonicsample, 0);
							LCD.clear();

							double error = desired - sonicsample[0]*100;//error from expected
						  double correction = kp*error;
					    double motorBspd, motorCspd;

					    if (error > 0.5){//turn to the right
					    	motorBspd = 70 + correction;
					      motorCspd = 30 + correction;
					   	}

					    else if (error < -0.5) {//turn to the left
					      motorCspd = 70 + correction;
					      motorBspd = 30 + correction;
					    }
					    else {//buffer region: move straight
					     	motorCspd = 50;
					      motorBspd = 50;
					    }
							//Show speeds
					    System.out.println("B: " + (int)motorBspd + " C: "+ (int)motorCspd);
					    Motor.B.setSpeed((int)motorBspd);
				  	 	Motor.C.setSpeed((int)motorCspd);
				  	  Motor.B.forward();
				  	  Motor.C.forward();

				  	  angle_rotated = Motor.B.getTachoCount();
					    total_dist = (angle_rotated*Math.PI/180)*(5.5/2);//current distance
				}
				Motor.B.stop();
				Motor.C.stop();

				System.out.println("Mission Complete");

		}
}
