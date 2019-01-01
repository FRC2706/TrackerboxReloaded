package ca.team2706.vision.trackerboxreloaded.source;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import ca.team2706.vision.trackerboxreloaded.Main;

public class ImageSource extends CameraSource{

	private Mat image;
	
	public ImageSource(String sourceID) {
		super(sourceID);
		try {
			this.image = Main.bufferedImageToMat(ImageIO.read(new File(sourceID)));
		} catch (IOException e) {
			//FATAL ERROR!
			e.printStackTrace();
			System.exit(1);
		}
		Imgproc.resize(image, image, Main.visionParams.sz);
	}

	@Override
	public Mat getImage() {
		return image;
	}

}
