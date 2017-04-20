package whr.s_ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import whr.socketThread.Server;
import whr.threadOperation.AttachmentProcessor;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static Server server = null;

	//static:以便在其他类中对JTextArea中的内容进行更新；volatile：保证无须显式锁的情况下该变量的内存可见性
	public static volatile JTextArea text = new JTextArea();
	private JButton jb3 = new JButton("开始执行默认操作");
	private JButton jb4 = new JButton("关闭默认操作");
	private JScrollPane scroll = new JScrollPane(text);

	public MainFrame(boolean isShow) {
		setTitle("文件管理服务器");
		init();
		setSize(500, 350);
		// 获取屏幕尺寸
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// 获取主界面的窗体尺寸
		Dimension frameSize = this.getSize();
		// 令主界面窗体居中
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("执行windowclosed");
				super.windowClosing(e);
				if (server != null)
					server.stopServer();
				else
					System.out.println("serversocket=null");
				System.exit(-1);
			}
		});
		this.setVisible(isShow);
		//server的操作会因循环持续，需要放在其他操作（窗口关闭事件或窗口显示等）后面
		server = new Server();
		server.startServer();
	}

	public void init() {
		JPanel jp = new JPanel();
		BoxLayout boxLayoutY = new BoxLayout(jp, BoxLayout.Y_AXIS);
		BoxLayout boxLayoutX;
		jp.setLayout(boxLayoutY);

		// 面板布局
		// 第一行
		JPanel jpr1 = new JPanel();
		boxLayoutX = new BoxLayout(jpr1, BoxLayout.X_AXIS);
		jpr1.setLayout(boxLayoutX);
		jpr1.add(Box.createHorizontalStrut(30));
		jpr1.add(this.jb3);
		jpr1.add(Box.createHorizontalStrut(30));
		jpr1.add(this.jb4);
		jpr1.add(Box.createHorizontalStrut(30));
		jb3.addActionListener(this);
		jb4.addActionListener(this);

		jp.add(Box.createVerticalStrut(30));
		jp.add(jpr1);
		jp.add(Box.createVerticalStrut(20));
		// 第三行
		scroll.setPreferredSize(new Dimension(320, 150));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jp.add(scroll);
		jp.add(Box.createVerticalStrut(30));

		this.add(jp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("开始执行默认操作")) {
			AttachmentProcessor ap = new AttachmentProcessor();
			ap.init();
		} else if (e.getActionCommand().equals("关闭默认操作")) {
			new AttachmentProcessor().shutdown();
		}
	}

}
