package ca.team2706.vision.trackerboxreloaded;

import ca.team2706.vision.trackerboxreloaded.Main.VisionData;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class ExampleServer {
	/**
	 * the vision table
	 */
	public static NetworkTable vision;

	public static void main(String[] args) {
		// Must be included!
		System.loadLibrary("opencv_java310");
		
		// Inits stuff
		NetworkTableInstance instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startServer();
		vision = instance.getTable("vision");
		while (true) {
			try {
				VisionData visionData = Main.VisionData.decode(vision);
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println("ctrX: "+visionData.ctrX);
				System.out.println("numTargetsFound: "+visionData.numTargetsFound);
				System.out.println("fps: "+visionData.fps);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
