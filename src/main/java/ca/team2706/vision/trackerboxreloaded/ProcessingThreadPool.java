package ca.team2706.vision.trackerboxreloaded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;

/*
 * This class is magic
 * don't touch it or the Finlay
 * will be angry.
 */
public class ProcessingThreadPool {

	private static List<Processor> threads = new ArrayList<Processor>();

	private static List<DataPackage> datas = new ArrayList<DataPackage>();

	private static long counter = 0;

	public static void process(Mat frame) {

		synchronized (threads) {

			int size = threads.size();

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

					for (DataPackage p : datas) {

						if (p.getId() == counter) {
							results.add(p);
							datas.remove(p);
						}

					}

					try {
						Thread.sleep(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			
			//We have the results, now piece them back together
			
			

		}

		counter++;

	}

	private static Map<Integer, Mat> split(Mat frame, int size) {

		Map<Integer, Mat> mats = new HashMap<Integer, Mat>();

		int width = frame.width() / size;

		for (int i = 0; i < size; i++) {

			Mat f = frame.submat(i * width, 0, width, frame.height());

			mats.put(i, f);

		}

		return mats;

	}

	public static void put(DataPackage data) {
		datas.add(data);
	}

}
