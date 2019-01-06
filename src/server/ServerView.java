package server;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ServerView extends JFrame implements ActionListener{
	//创建一个文本框
	JLabel jlPort=new JLabel("端 口 号");//创建提示输入端口号标签
	JTextField jtfPort=new JTextField("9999");//用于输入端口号的文本框
	JButton jbStart=new JButton("启动");//创建"启动"按钮
	JButton jbStop=new JButton("关闭");//创建"关闭"按钮
	JTextArea textArea = new JTextArea();//创建左侧文本框
	JScrollPane jscrollPane = new JScrollPane(textArea);//创建滚动条
	JPanel jps=new JPanel();//创建一个JPanel对象
	ConcurrentHashMap<String,ServerThread> onlineList=new ConcurrentHashMap<String,ServerThread>();//当前在线用户的线程列表
	ServerSocket ss;//声明ServerSocket引用
	ServerView serverView;
	public ServerView(){
		//初始化右边框
		initRightJPanel();
		
		//初始化窗口
		initUi();
		
		serverView = this;
	}
	
	
	public void initUi(){
		
		jscrollPane.setPreferredSize(new Dimension(200, 300));
		textArea.setEditable(false);
		
		jscrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //设置滚动条垂直自动出现
		jscrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//设置滚动条水平自动出现
		
		JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jscrollPane,jps);//创建一个JSplitPane
		this.add(jsp);
		this.setTitle("服务器");
		this.setResizable(false);// 设置此窗口是否 可由用户调整大小
		this.setVisible(true); //设置窗口显示状态
		this.pack();//根据窗口里面的布局及组件的preferredSize来确定frame的最佳大小。
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void initRightJPanel(){
		jps.setPreferredSize(new Dimension(200, 300));
		jps.setLayout(null);//设为空布局
		
		//设置控件坐标和大小
		jlPort.setBounds(20,20,50,20);
		jtfPort.setBounds(85,20,60,20);
		jbStop.setBounds(85,50,60,20);
		jbStart.setBounds(18,50,60,20);
		
		jps.add(jlPort);
		jps.add(jtfPort);
		jps.add(jbStop);
		jps.add(jbStart);
		
		//给按钮注册事件监听
		jbStop.addActionListener(this);
		jbStart.addActionListener(this);
		//关闭按钮初始禁用
		jbStop.setEnabled(false);
	}
	
	
	/**
	 * 为启动和关闭注册事件
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbStart) StartEvent();// 当单击"启动"按钮时
		
		if (e.getSource() == jbStop) {// 单击"关闭"按钮后
			
		}

	}
	
	/**
	 * 启动按钮的事件
	 * @param args
	 */
	public void StartEvent(){
		int port = 0;
		
		try {
			port=Integer.parseInt(this.jtfPort.getText().trim());//获得用户输入的端口号，并转化为整型
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,"端口号只能是整数","错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
	
		if (port > 65535 || port < 0) {// 断口号不合法，给出提示信息
			JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数", "消息", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			
			ss=new ServerSocket(port);//创建ServerSocket对象监听端口
			
			/**
			 * 创建服务器总线程
			 * 实时监听端口，当发现连接请求的时候创建服务子线程
			 */
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true){
							Socket socket = ss.accept();
							new ServerThread(socket,serverView).start();
						}
						
					} catch (IOException e) {
						System.err.println("监听端口时出现错误！");
						e.printStackTrace();
					}
					
				}
			}).start();
			
			
			//给出启动成功的消息
			JOptionPane.showMessageDialog(this,"启动成功","消息",JOptionPane.INFORMATION_MESSAGE);
			jbStart.setEnabled(false);//禁用启动按钮
			jbStop.setEnabled(true);//开启关闭按钮
			
		} catch (IOException e) {
			System.err.println("点击启动按钮时出现错误！");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 更新用户在线列表
	 * @param args
	 */
	public void reUsersList() {
		textArea.setText(null);
		ServerThread serverThread;
		
		Enumeration <String> keys = onlineList.keys();
		
		while(keys.hasMoreElements()){
			serverThread = onlineList.get(keys.nextElement());
			String msg = "";
			msg += "IP:" + serverThread.socket.getInetAddress().toString() + "\n"; //拼接IP
			msg += "Port:" + serverThread.socket.getPort() + "\n"; //拼接昵称
			msg += "name:" + serverThread.getName() + "\n\n"; //拼接昵称
			textArea.append(msg);
		}
		
	}
	
	public static void main(String[] args) {
		
		ServerView sv = new ServerView();
		
		//ServerView sv2 = new ServerView();
	}


	
}
