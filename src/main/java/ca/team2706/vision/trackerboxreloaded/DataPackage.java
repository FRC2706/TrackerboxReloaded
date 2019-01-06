package ca.team2706.vision.trackerboxreloaded;

import ca.team2706.vision.trackerboxreloaded.Main.VisionData;

public class DataPackage implements Comparable<DataPackage>  {

	private long time;
	private VisionData data;
	private long id;
	private long threadId;

	public DataPackage(VisionData data, long time, long id, long threadId) {

		this.time = time;
		this.data = data;
		this.id = id;
		this.threadId = threadId;

	}

	public long getTime() {
		return time;
	}

	public long getId() {
		return id;
	}

	public VisionData getData() {
		return data;
	}

	public long getThreadId() {
		return threadId;
	}

	@Override
	public int compareTo(DataPackage other) {
		return Long.compare(threadId, other.threadId);
	}

	
}
