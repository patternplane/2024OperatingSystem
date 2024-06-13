package secondProject;

public class QueueElement {
	public int id;
	public int acceptedTime;
	public int cylinder;
	
	public QueueElement(int acceptedTime, int cylinder, int id) {
		this.acceptedTime = acceptedTime;
		this.cylinder = cylinder;
		this.id = id;
	}
	
	public QueueElement(int acceptedTime, int cylinder) {
		this(acceptedTime, cylinder, -1);
	}
}
