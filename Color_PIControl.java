// Follows a black line on the ground using data from a color sensor and
// PI (Proportional, Integral) feedback control

import lejos.hardware.motor.*;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.lcd.*;

public class Color_PIControl {

	public static void main(String[] args) throws Exception {

    EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);

    int sampleSize = color.sampleSize(); //size of port

    float[] actual = new float[sampleSize];

		double desired = 0.2;
		double kp = 100; //proportional feedback constant
		double ki = 0.1; //integral feedback constant
		double integral = 0;
    double motorBspd = 50;
    double motorCspd = 50;
    double error, correction;
    Motor.B.setSpeed((int)motorBspd);//initialize motor speed
		Motor.C.setSpeed((int)motorCspd);

    Motor.B.forward();
    Motor.C.forward();

		while(!Button.ENTER.isDown()) {//Exit condition: press center button
      color.getRedMode().fetchSample(actual, 0); //get color sensor value
      error = desired - actual[0]; //difference from expected value
      correction = kp * error + integral * ki;

			//set speeds based on feedback controller and which direction to go
      if (error > 0.05 && motorBspd < 200) {
          motorBspd = 50 + correction;
          motorCspd = 20 + correction;
          integral += error;
        }

        else if (error < -0.05 && motorCspd < 200) {
          motorCspd = 50 + correction;
          motorBspd = 20 + correction;
          integral += error;
        }
        else {
      	  motorCspd = 50;
      	  motorBspd = 50;
      	  integral += error;
        }

    	  Motor.B.setSpeed((int)motorBspd);
    	  Motor.B.forward();
    	  Motor.C.setSpeed((int)motorCspd);
    	  Motor.C.forward();
				LCD.clear();
				System.out.println(correction);

		}
	color.close();
	}
}
