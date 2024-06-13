package secondProject;

public interface DiskScheduler {
	Result[] run(DiskRequest[] requests, int startPos) throws Throwable;
}
