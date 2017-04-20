package whr.threadOperation;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import whr.s_ui.MainFrame;

//模式角色：Producer-Consumer.Producer
public class AttachmentProcessor {

	private final String ATTACHMENT_STORE_BASE_DIR = "src/savefiledir";

	// 模式角色：Producer-Consumer.Channel
	private static Channel<File> channal = new BlockingQueueChannel<File>(new ArrayBlockingQueue<File>(200));

	// 模式角色：Producer-Consumer.Consumer
	private final AbstractTerminatableThread pretreatmentThread = new AbstractTerminatableThread() {

		@Override
		protected void doRun() throws Exception {
			File file = null;
			file = channal.take();

			if (file != null && file.exists()) {
				System.out.println("从channel中取出了一个有效文件:" + file.getName());
				pretreatmentFile(file);
				terminationToken.reservations.decrementAndGet();
				System.out.println("处理了一个文件！");
				MainFrame.text.setText(MainFrame.text.getText() + "\n已处理文件" + file.getName());
			}
		}

		private void pretreatmentFile(File file) {
			// 模拟预处理时间
			Random rd = new Random();
			try {
				Thread.sleep(rd.nextInt(1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	public void init() {
		pretreatmentThread.terminationToken.setToShutdown(false);
		pretreatmentThread.start();
		MainFrame.text.setText(MainFrame.text.getText() + "\n开始默认处理操作");
	}

	public void shutdown() {
		pretreatmentThread.terminate();
		MainFrame.text.setText(MainFrame.text.getText() + "\n在下个任务处理完毕后将关闭默认处理操作");
	}

	// 将文件保存后放入channal
	public void saveAttachment(DataInputStream in, String documentId) throws IOException {
		File file = savaAsFile(in, documentId);
		try {
			channal.put(file);
			MainFrame.text.setText(MainFrame.text.getText() + "\n已保存文件" + file.getName());
		} catch (InterruptedException e) {
			System.out.println("放入channel队列失败！");
		}
		pretreatmentThread.terminationToken.reservations.incrementAndGet();
	}

	// 保存文件
	private File savaAsFile(DataInputStream in, String documentId) throws IOException {
		String dirName = ATTACHMENT_STORE_BASE_DIR;
		File file = new File(dirName + "/" + documentId);

		FileOutputStream fos = new FileOutputStream(file);
		byte[] inputByte = new byte[1024];
		int length = 0;

		while (true) {
			if (in != null)
				length = in.read(inputByte, 0, inputByte.length);
			if (length == -1)
				break;
			fos.write(inputByte, 0, length);
			fos.flush();
		}

		fos.close();
		in.close();
		return file;
	}

}
