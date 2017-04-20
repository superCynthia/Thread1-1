package whr.threadOperation;

/**
 * 对通道参与者进行抽象
 * 
 * @param <P>
 *            "产品"类型
 */
public interface Channel<P> {
	/**
	 * 从通道中取出一个"产品"
	 * 
	 * @return "产品"
	 * @throws InterruptedException
	 */
	P take() throws InterruptedException;

	/**
	 * 往通道中存储一个"产品"
	 * 
	 * @param product
	 *            "产品"
	 * @throws InterruptedException
	 */
	void put(P product) throws InterruptedException;
}
