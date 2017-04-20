package whr.threadOperation;

/**
 * 可停止线程的抽象
 *
 */
public interface Terminatable {
	// 请求目标线程停止
	public void terminate();
}
