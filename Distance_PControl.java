import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Distance_PControl {

	public static void straight(int dist, int desired) {
		EV3UltrasonicSensor sonic = new EV3UltrasonicSensor(SensorPort.S4);
		double kp = 40;
		int sampleSize = sonic.sampleSize();
		float[] sonicsample = new float[sampleSize];
		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();
		double angle_rotated = 0;
		double total_dist = 0;
		Motor.B.setSpeed(50);
  	Motor.C.setSpeed(50);

		while(total_dist < dist){
		sonic.fetchSample(sonicsample, 0);
		LCD.clear();
		//System.out.println(sonicsample[0]*100);

		double error = desired - sonicsample[0]*100;
	    //double new_error = error/interval;
	    double correction = kp*error;
	    double motorBspd, motorCspd;

	    if (error > 0.5){
	    	motorBspd = 70 + correction;
	        motorCspd = 30 + correction;
	    	}

	    else if (error < -0.5) {
	          motorCspd = 70 + correction;
	          motorBspd = 30 + correction;
	        }
	    else {
	      	  motorCspd = 50;
	      	  motorBspd = 50;
	        }

	    Motor.B.setSpeed((int)motorBspd);
  	  	Motor.C.setSpeed((int)motorCspd);
	    System.out.println("B: " + (int)motorBspd + " C: "+ (int)motorCspd);
  	  	Motor.B.forward();
  	  	Motor.C.forward();

  	  	angle_rotated = Motor.B.getTachoCount();
	    total_dist = (angle_rotated*Math.PI/180)*(5.5/2);//current distance

		}
		Motor.B.stop();
		Motor.C.stop();
		sonic.close();
	}

	public static void turn(int curr, int wanted) {
		double d = 13;
		double r = 5.5/2;
		double degree = ((Math.PI*d)/(4*r))*(180/(Math.PI));
		Motor.B.setSpeed(70);
		Motor.C.setSpeed(70);

		if (curr > wanted){
			Motor.B.resetTachoCount();
			Motor.C.resetTachoCount();
			Motor.B.rotateTo(-(int)degree,true);//rotate 90
			Motor.C.rotateTo((int)degree);

			Motor.B.resetTachoCount();
			Motor.C.resetTachoCount();
			double straight = curr - wanted;
			Motor.B.rotateTo((int)((straight/r)*(180/Math.PI)),true);//go straight
			Motor.C.rotateTo((int)((straight/r)*(180/Math.PI)));

			Motor.B.resetTachoCount();
			Motor.C.resetTachoCount();
			Motor.B.rotateTo((int)degree,true);//rotate -90
			Motor.C.rotateTo(-(int)degree);
		}
		else{
			Motor.B.resetTachoCount();
			Motor.C.resetTachoCount();
			Motor.B.rotateTo((int)degree,true);//rotate -90
			Motor.C.rotateTo(-(int)degree);

			Motor.B.resetTachoCount();
			Motor.C.resetTachoCount();
			double straight = wanted - curr;
			Motor.B.rotateTo((int)((straight/r)*(180/Math.PI)),true);//go straight
			Motor.C.rotateTo((int)((straight/r)*(180/Math.PI)));

			Motor.B.resetTachoCount();
			Motor.C.resetTachoCount();
			Motor.B.rotateTo(-(int)degree,true);//rotate +90
			Motor.C.rotateTo((int)degree);
		}
		Motor.B.stop();
		Motor.C.stop();

	}
	public static void main(String[] args) {

		System.out.println("We ready to go");

		while(!Button.ENTER.isDown()) {}//wait for down button
		Motor.B.setSpeed(50);
		Motor.C.setSpeed(50);

		straight(100,20);
		System.out.println("We go");
		turn(20,30);
		System.out.println("We turned");

		Motor.B.setSpeed(50);
		Motor.C.setSpeed(50);
		System.out.println("We set speed");
		straight(100,30);
		System.out.println("We go");

		Motor.B.setSpeed(50);
		Motor.C.setSpeed(50);
		turn(30,20);
		System.out.println("We turned");

		Motor.B.setSpeed(50);
		Motor.C.setSpeed(50);
		straight(100,20);

		Motor.B.stop();
		Motor.C.stop();
		System.out.println("We done");

	}
}
