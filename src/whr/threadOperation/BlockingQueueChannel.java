package whr.threadOperation;

import java.util.concurrent.BlockingQueue;

/**
 * 基于阻塞队列的通道实现
 * 
 * @param <P>
 *            "产品类型"
 */

public class BlockingQueueChannel<P> implements Channel<P> {
	
	private final BlockingQueue<P> queue;
	
	public BlockingQueueChannel(BlockingQueue<P> queue) {
		this.queue = queue;
	}

	@Override
	public P take() throws InterruptedException {
		P p = queue.take();
		System.out.println("channal -1");
		return p;
	}

	@Override
	public void put(P product) throws InterruptedException {
		queue.put(product);
		System.out.println("channal +1");
	}

}
