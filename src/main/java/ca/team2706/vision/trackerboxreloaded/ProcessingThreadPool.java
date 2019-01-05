package ca.team2706.vision.trackerboxreloaded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;

import ca.team2706.vision.trackerboxreloaded.Main.VisionData;

/*
 * This class is magic
 * don't touch it or the Finlay
 * will be angry.
 */
public class ProcessingThreadPool implements Runnable {

	private static List<Processor> threads = new ArrayList<Processor>();

	private static List<DataPackage> datas = new ArrayList<DataPackage>();

	private static long counter = 0;

	@SuppressWarnings("unused")
	private static double fps = 0;

	@SuppressWarnings("unused")
	private static long lastTime, frequency;

	@SuppressWarnings("unused")
	private static long currThreadId = 0;

	public static boolean stop = false;

	public static VisionData process(Mat frame) {

		frequency = System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		synchronized (threads) {

			long startTime = System.currentTimeMillis();

			int size = threads.size();

			if (size == 0) {
				System.out.println("NO THREADS!");
				return null;
			}

			Map<Integer, Mat> mats = split(frame, size);

			for (int i = 0; i < mats.keySet().size(); i++) {

				Processor p = threads.get(i);

				Mat m = mats.get(i);

				MatPackage mP = new MatPackage(counter, m);

				p.getProcessQueue().add(mP);

			}

			List<DataPackage> results = new ArrayList<DataPackage>();

			while (results.size() < size) {

				synchronized (datas) {
					try {
						for (DataPackage p : datas) {

							if (p.getId() == counter) {
								results.add(p);
								datas.remove(p);
							}

						}
					} catch (Exception e) {

					}
					try {
						Thread.sleep(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

			// We have the results, now piece them back together
			List<DataPackage> sorted = sort(results);

			if (sorted.size() == 0) {
				// This should not happen!!! BAD BAD BAD
				return null;
			}

			Mat output = new Mat(frame.height(), frame.width(), sorted.get(0).getData().outputImg.type());
			Mat binmask = new Mat(frame.height(), frame.width(), sorted.get(0).getData().binMask.type());
			Mat rawMat = frame.clone();

			for (DataPackage p : sorted) {

				long id = p.getId();

				Mat m1 = p.getData().outputImg;
				Mat m2 = p.getData().binMask;

				m1.copyTo(output.colRange((int) (m1.width() * id), (int) (m1.width() * (id + 1))));
				m2.copyTo(binmask.colRange((int) (m2.width() * id), (int) (m2.width() * (id + 1) - 1)));

			}

			if (sorted.size() == 0) {
				System.out.println("SORTING RETURNED SIZE OF 0");
				return null;
				// This is bad
			}

			VisionData visionData = new VisionData();

			visionData.outputImg = output;
			visionData.binMask = binmask;
			visionData.rawImage = rawMat;

			Pipeline.contours(visionData, Main.visionParams);

			// Selects the prefered target
			Pipeline.selectPreferredTarget(visionData, Main.visionParams);

			fps = (double) (1000) / (System.currentTimeMillis() - startTime);

			counter++;

			return visionData;

		}

	}

	private static List<DataPackage> sort(List<DataPackage> data) {

		List<DataPackage> sorted = new ArrayList<DataPackage>();

		int i = 0;

		while(sorted.size() < data.size()) {

			boolean stop = false;

			for (DataPackage p : data) {

				if (!stop) {
					
					if (p.getThreadId() == i) {
						sorted.add(p);
						System.out.println(p.getThreadId()+" "+i);
						i++;
						stop = true;
						if(sorted.size() >= data.size()) {
							break;
						}
					}

				}

			}
		}

		return sorted;

	}

	private static Map<Integer, Mat> split(Mat frame, int size) {

		Map<Integer, Mat> mats = new HashMap<Integer, Mat>();

		int width = frame.width() / size;

		for (int i = 0; i < size; i++) {

			Mat f = new Mat(width, frame.height(), frame.type());
			frame.colRange(i * width, (i + 1) * width).copyTo(f);

			mats.put(i, f);

		}

		return mats;

	}

	public static void put(DataPackage data) {
		datas.add(data);
	}

	@Override
	public void run() {

		/*
		 * while(true) {
		 * 
		 * int numNeeded = (int) ((1000 / fps) / frequency)+1;
		 * 
		 * 
		 * //Do not touch this or it will break!!! if(numNeeded <= 0) { numNeeded = 1; }
		 * 
		 * for(int i = 0; i < threads.size(); i++) { if(i+1 > numNeeded) {
		 * threads.get(i).finish = true; threads.remove(i); } }
		 * 
		 * while(threads.size() < numNeeded) { Processor p = new
		 * Processor(currThreadId); threads.add(p); p.start(); currThreadId++; }
		 * 
		 * try { Thread.sleep(10); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 * 
		 * }
		 */

		// That code is broken so just use 4 threads
		for (int i = 0; i < 4; i++) {
			Processor p = new Processor(i);

			p.start();

			threads.add(p);
		}

	}

	public static void start() {
		new Thread(new ProcessingThreadPool()).start();
	}

}
