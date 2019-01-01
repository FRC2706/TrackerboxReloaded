package ca.team2706.vision.trackerboxreloaded.source;

import org.opencv.core.Mat;

public abstract class CameraSource {
	
	protected String sourceID;
	
	public CameraSource(String sourceID) {
		this.sourceID = sourceID;
	}

	public abstract Mat getImage();

	public String getSourceID() {
		return sourceID;
	}
	
}
