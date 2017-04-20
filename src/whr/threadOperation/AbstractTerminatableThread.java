package whr.threadOperation;

/**
 * 可停止的抽象线程。
 * 
 * 模式角色：Two_phaseTermination.AbstractTerminatableThread
 *
 */
public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

	// 模式角色：Two_phaseTermination.TerminationToken
	public final TerminationToken terminationToken;

	public AbstractTerminatableThread() {
		this(new TerminationToken());
	}

	/**
	 * 
	 * @param terminationToken
	 *            线程间共享的线程标志实例
	 */
	public AbstractTerminatableThread(TerminationToken terminationToken) {
		super();
		this.terminationToken = terminationToken;
		terminationToken.register(this);
	}

	protected abstract void doRun() throws Exception;

	protected void doCleanup(Exception cause) {
	}

	protected void doTerminate() {
	}

	@Override
	public void run() {
		Exception ex = null;
		try {
			for (;;) {
				// 在执行线程的处理逻辑前先判断线程停止的标志
				if (terminationToken.isToShutdown() && terminationToken.reservations.get() <= 0)
					break;
				doRun();
			}
		} catch (Exception e) {
			// 使得线程能够响应interrupt调用而退出
			ex = e;
		} finally {
			try {
				doCleanup(ex);
			} finally {
				terminationToken.notifyTreadTermination(this);
			}
		}
	}

	@Override
	public void interrupt() {
		terminate();
	}

	// 请求停止线程
	@Override
	public void terminate() {
		terminationToken.setToShutdown(true);
		try {
			doTerminate();
		} finally {
			// 若无待处理的任务，则试图强制终止线程
			if (terminationToken.reservations.get() <= 0)
				super.interrupt();
		}
	}

}
