package ca.team2706.vision.trackerboxreloaded.source;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import ca.team2706.vision.trackerboxreloaded.Main;

public class USBCameraCVSource extends CameraSource {

	private VideoCapture camera;
	private Mat frame;

	public USBCameraCVSource(String sourceID) {
		super(sourceID);
		camera = new VideoCapture(Integer.valueOf(sourceID));
		// Sets camera parameters
		int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G');
		camera.set(Videoio.CAP_PROP_FOURCC, fourcc);
		camera.set(Videoio.CAP_PROP_FRAME_WIDTH, Main.visionParams.width);
		camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, Main.visionParams.height);

		camera.read(frame);

		if (!camera.isOpened()) {
			// If the camera didn't open throw an error
			System.err.println("Error: Can not connect to camera");
			// Exit
			System.exit(1);
		}

		// Set up the camera feed
		camera.read(frame);
	}

	@Override
	public Mat getImage() {
		camera.read(frame);
		return frame;
	}

}
