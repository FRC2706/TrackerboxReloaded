package ca.team2706.vision.trackerboxreloaded;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import ca.team2706.vision.trackerboxreloaded.Main.VisionData;

public class Processor extends Thread {

	private List<MatPackage> processQueue = new ArrayList<MatPackage>();

	private boolean running = true;

	private long threadId;

	public Processor(long threadId) {
		this.threadId = threadId;
	}

	@Override
	public void run() {

		while (running) {

			if (processQueue.size() > 0) {

				MatPackage mP = processQueue.get(0);

				Mat frame = mP.getFrame();

				// Process the frame!
				// Log when the pipeline starts
				long pipelineStart = System.nanoTime();
				// Process the frame
				VisionData visionData = Pipeline.process(frame, Main.visionParams, Main.use_GUI);
				// Log when the pipeline stops
				long pipelineEnd = System.nanoTime();

				DataPackage data = new DataPackage(visionData, pipelineEnd - pipelineStart, mP.getId(), threadId);

				ProcessingThreadPool.put(data);

			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public List<MatPackage> getProcessQueue() {
		return processQueue;
	}

	public long getThreadId() {
		return threadId;
	}
	
	
	
}
