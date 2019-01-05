package ca.team2706.vision.trackerboxreloaded;

import org.opencv.core.Mat;

public class MatPackage {

	private long id;
	private Mat frame;

	public MatPackage(long id, Mat frame) {
		super();
		this.id = id;
		this.frame = frame;
	}

	public long getId() {
		return id;
	}

	public Mat getFrame() {
		return frame;
	}

	
}
