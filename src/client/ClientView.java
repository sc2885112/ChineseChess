package client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import enu.ClientMenu;

@SuppressWarnings("serial")
public class ClientView extends JFrame implements ActionListener{
	JLabel jlHost=new JLabel("主机名");//创建提示输入主机名的标签
	JLabel jlPort=new JLabel("端口号");////创建提示输入端口号标签
	JLabel jlNickName=new JLabel("昵    称");//创建提示输入昵称的标签
	JTextField jtfHost=new JTextField("localhost");//创建输入主机名的文本框，默认值是"127.0.0.1"
	JTextField jtfPort=new JTextField("9999");//创建输入端口号的文本框，默认值是9999
	JTextField jtfNickName=new JTextField("Play1");//创建输入昵称的文本框，默认值是Play1
	JButton jbConnection=new JButton("连  接");//创建"连接"按钮
	JButton jbExtConnection=new JButton("断  开");//创建"断开"按钮
	JButton jbAdmitDefeat=new JButton("认  输");//创建"认输"按钮
	JButton jbChallenge=new JButton("挑  战");//创建"挑战"按钮
	JComboBox jcbNickList=new JComboBox();//创建存放当前用户的下拉列表框
	JTextArea textArea = new JTextArea();//创建聊天框	
	JScrollPane jscrollPane = new JScrollPane(textArea);
	JTextArea sendMessageText=new JTextArea();//创建发送消息的文本框
	JButton jbSendMessage=new JButton("发送");//创建"连接"按钮
	Checkerboard checkerboard = new Checkerboard(); //创建棋盘;
	Rule rule  = new Rule(checkerboard); //创建规则类
	JPanel rightJPanel = new JPanel();//创建右边菜单栏
	public String opponentName = null; //正在对弈的对手
	public boolean isCanMove = true; //是否轮到我走棋的标记
	public boolean userCamp = true; //判断用户阵营
	private ClientThread clientThread ; //客户端线程
	ClientView clientView = this;
	Socket socket; //连接点
	int preX,preY;
	
	public ClientView(){
		initRightJPanl();//初始化右边框
		initButton();
		initCheckerboard();//初始化棋盘
	}
	
	/**
	 * 初始化右边框
	 * @return
	 */
	public void initRightJPanl(){
		rightJPanel.setPreferredSize(new Dimension(200, 700));
		rightJPanel.setLayout(null);//设为空布局
		
		jlHost.setBounds(10,10,50,20);
		rightJPanel.add(jlHost);//添加"主机名"标签
		
		jtfHost.setBounds(100,10,80,20);
		rightJPanel.add(jtfHost);//添加用于输入主机名的文本框
		
		jlPort.setBounds(10,40,50,20);
		rightJPanel.add(jlPort);//添加"端口号"标签
		
		jtfPort.setBounds(100,40,80,20);
		rightJPanel.add(jtfPort);//添加用于输入端口号的文本框
		
		jlNickName.setBounds(10,70,50,20);
		rightJPanel.add(jlNickName);//添加"昵称"标签
		
		jtfNickName.setBounds(100,70,80,20);
		rightJPanel.add(jtfNickName);//添加用于输入昵称的文本框
		
		jbConnection.setBounds(10,100,80,20);
		rightJPanel.add(jbConnection);//添加"连接"按钮
		
		jbExtConnection.setBounds(110,100,80,20);
		rightJPanel.add(jbExtConnection);//添加"断开"按钮
		
		jcbNickList.setBounds(10,130,180,20);
		rightJPanel.add(jcbNickList);//添加用于显示当前用户的下拉列表框
		
		jbChallenge.setBounds(10,160,80,20);
		rightJPanel.add(jbChallenge);//添加"挑战"按钮
		
		jbAdmitDefeat.setBounds(110,160,80,20);
		rightJPanel.add(jbAdmitDefeat);//添加"认输"按钮
		
		jscrollPane.setBounds(10, 190, 180, 400);
		textArea.setEditable(false);//设置l聊天框不可编辑
		textArea.setBackground(new Color(200, 200, 200));//设置背景色
		jscrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //设置滚动条垂直自动出现
		jscrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//设置滚动条水平自动出现
		rightJPanel.add(jscrollPane);//添加聊天框
		
		sendMessageText.setBounds(10, 600, 180, 50);
		rightJPanel.add(sendMessageText);//添加发送的消息框
		
		jbSendMessage.setBounds(130, 660, 60, 20);
		rightJPanel.add(jbSendMessage);//添加发送按钮
		rightJPanel.setBackground(new Color(128, 128, 128));
	}
	
	public void initButton(){
		//为按钮注册监听
		jbConnection.addActionListener(this);
		jbExtConnection.addActionListener(this);
		jbChallenge.addActionListener(this);
		jbAdmitDefeat.addActionListener(this);
		jbSendMessage.addActionListener(this);
		
		//设置按钮状态为不可用
		jbExtConnection.setEnabled(false);
		jbChallenge.setEnabled(false);
		jbAdmitDefeat.setEnabled(false);
		jbSendMessage.setEnabled(false);
	}
	
	/**
	 * 为各个按钮注册事件
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == jbConnection) openConnection(); //请求连接
		if (e.getSource() == jbChallenge) launchChallenge();//发起挑战
		if (e.getSource() == jbExtConnection) extConnection();//断开连接
		if (e.getSource() == jbAdmitDefeat) admitDefeat(); //认输
		if (e.getSource() == jbSendMessage) sendMessage(); //发送消息
	}
	
	public void openConnection() {
		int port = 0;
		try {// 获得用户输入的断口号并转化为整型
			port = Integer.parseInt(jtfPort.getText().trim());
		} catch (Exception e) {// 不是整数，给出错误提示
			JOptionPane.showMessageDialog(this, "端口号只能是整数", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (port > 65535 || port < 0) {// 端口号不合法，给出错误提示
			JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = jtfNickName.getText().trim();// 获得昵称
		if (name.length() == 0) {// 昵称为空，给出错误提示信息
			JOptionPane.showMessageDialog(this, "玩家姓名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			socket = new Socket(jtfHost.getText().trim(), port);
			clientThread = new ClientThread(socket, this);// 创建客户端代理线程
			clientThread.start();// 启动客户端代理线程
			jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			jbConnection.setEnabled(false);// 将"连接"按钮设为不可用
			jbExtConnection.setEnabled(true);// 将"断开"按钮设为可用
			jbChallenge.setEnabled(true);// 将"挑战"按钮设为可用
			JOptionPane.showMessageDialog(this, "已连接到服务器", "提示", JOptionPane.INFORMATION_MESSAGE);// 连接成功，给出提示信息
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "连接服务器失败", "错误", JOptionPane.ERROR_MESSAGE);// 连接失败，给出提示信息
			return;
		}
	}
	
	
	/**
	 * 发起挑战
	 */
	public void launchChallenge() {
		try {
			String name = (String)jcbNickList.getSelectedItem();

			if (name == null ||  name.equals("")) {
				JOptionPane.showMessageDialog(this, "请选择对方名字", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}

			clientThread.out.writeUTF(ClientMenu.LAUNCH_CHALLENGE + name);	// 向服务器发送挑战信息
			JOptionPane.showMessageDialog(this, "已提出挑战,请等待恢复...", "提示", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	//断开连接
	public void extConnection(){
		try {
			if(clientThread != null){
				if(opponentName != null){//如果有正在对弈的对手则带上对手的名字方便服务器回发信息
					clientThread.out.writeUTF(ClientMenu.USER_EXIT + opponentName);//向服务器发送断开连接的请求
				}else{
					clientThread.out.writeUTF(ClientMenu.USER_EXIT);//向服务器发送断开连接的请求
				}
				
				clientThread.sign = false; //终止线程
				clientThread = null;
			}
			userCamp = true;//初始化阵营
			opponentName = null;//对手置空
			isCanMove = true;//初始化走棋标记
			textArea.setText(null);//初始化聊天框
			//初始化各种按钮
			jtfHost.setEnabled(true);
			jtfPort.setEnabled(true);
			jtfNickName.setEnabled(true);
			jbConnection.setEnabled(true);
			jbExtConnection.setEnabled(false);
			jbChallenge.setEnabled(false);
			jbAdmitDefeat.setEnabled(false);
			jbSendMessage.setEnabled(false);
			jcbNickList.removeAllItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 认输
	public void admitDefeat() {

		try {
			if (JOptionPane.showConfirmDialog(this, "您确定要认输吗？", "认输！",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				clientThread.out.writeUTF(ClientMenu.ADMIT_DEFEAT + opponentName);// 向服务器发送认输的请求
				jbChallenge.setEnabled(true);// 将"挑战"按钮设可用
				jbAdmitDefeat.setEnabled(false);// 将"认输"按钮设为不可用
				userCamp = true;//初始化阵营
				opponentName = null;//对手置空
				isCanMove = true;//初始化走棋标记
				checkerboard.initPiece(false);// 初始化棋盘
				jbSendMessage.setEnabled(false);//将发送按钮不可用
				textArea.setText(null);//初始化聊天框
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 发送消息
	 */
	public void sendMessage() {

		try {
			String message = sendMessageText.getText().trim();
			if (opponentName == null)
				return; // 如果没有正在对战的对手直接返回

			if (message != null && message.length() > 0) {

				clientThread.out.writeUTF(ClientMenu.CHAT + message + "-" + opponentName);// 向服务器发送消息
				textArea.append("你对"+opponentName+"说:" + "\n    " + message + "\n\n");
				sendMessageText.setText(null);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	private void initCheckerboard() {
		
		//添加鼠标点击事件
		checkerboard.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int x = e.getX() / Checkerboard.DIAMETER;
				int y = e.getY() / Checkerboard.DIAMETER;

				boolean isMove = false;

				Piece piece = isStep();
				if (piece != null) {// 判断我方阵营有没有棋子被选中
					if(isCanMove){ //判断是否轮到我走棋
						if (rule.getRule(piece, x, y)) {// 判断是否可以落子
							
							if (checkerboard.pieceList[x][y] == null
									|| checkerboard.pieceList[x][y].getCamp() != piece.getCamp()) {
								
								Piece target = checkerboard.pieceList[x][y];
								int startX = piece.getX();
								int startY = piece.getY();
								pieceMove(piece , x ,y); //走棋
								checkerboard.repaint();//重绘
								
								if (opponentName == null) {
									if (userCamp) {
										userCamp = false;
									} else {
										userCamp = true;
									}
								} else {
									//发送走棋的消息
									try {
										clientThread.out.writeUTF(
												ClientMenu.MOVE + opponentName + "," + startX + "," + startY + "," + x + "," + y);
										
										isCanMove = false;
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
								
								if (target != null)// 判断是否分出胜负
									isWinning(target, x, y);
								
								isMove = true;
								
							}
						}
					}
					
				}

				// 设置棋子的选中状态
				if (!isMove) {// 判断是否落子
					if (isEmtry(x, y) != null) { // 判断点击的地方是否为空
						if (checkerboard.pieceList[x][y].getCamp() == userCamp) {// 判断该棋子阵营和玩家是否相同
							if (checkerboard.pieceList[x][y].getSelection()) {
								checkerboard.pieceList[x][y].setSelection(false);
							} else {
								checkerboard.pieceList[x][y].setSelection(true);
							}

							if (preX != x || preY != y) {
								if (checkerboard.pieceList[preX][preY] != null)
									checkerboard.pieceList[preX][preY].setSelection(false);
							}

							preX = x;
							preY = y;
						}
					}
				}

				checkerboard.repaint();
			}
		});
		
		//添加一个关闭事件
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String str = "是否要退出游戏?";
				// 添加消息对话框
				if (JOptionPane.showConfirmDialog(clientView, str, "退出游戏",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					extConnection(); //通知服务器断开连接
					System.exit(0); // 退出
				}
			}
		});
		
		
		JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,checkerboard,rightJPanel);//创建一个JSplitPane
		jsp.setEnabled(false); //设置分割条禁止拖动
		add(jsp);
		setTitle("中国象棋");
		setResizable(false);// 设置此窗口是否可由用户调整大小
		setVisible(true); //设置窗口显示状态
		pack();//根据窗口里面的布局及组件的preferredSize来确定frame的最佳大小。
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * 判断是否分出胜负
	 * @return
	 */
	public void isWinning(Piece target ,int x, int y ){
		
		if (target.getName() == "将" || target.getName() == "帥") {
			String resultMessage;
			if (checkerboard.pieceList[x][y].getCamp()) {
				resultMessage = "红方胜利";
			} else {
				resultMessage = "黑方胜利";
			}
			JOptionPane.showMessageDialog(this, resultMessage, "游戏结束",
					JOptionPane.PLAIN_MESSAGE); // 弹出消息框

			if(opponentName != null && userCamp){
				checkerboard.initPiece(false);
				isCanMove = true;//初始化走棋标记
			}else if(opponentName != null && userCamp == false){
				checkerboard.initPiece(true);
				isCanMove = false;//初始化走棋标记
			}else{
				checkerboard.initPiece(false);
			}
		}
		
	}
	
	/**
	 * 走棋
	 * @return
	 */
	public void pieceMove(Piece piece,int x,int y){
		
		checkerboard.pieceList[x][y] = piece;
		checkerboard.pieceList[piece.getX()][piece.getY()] = null;
		checkerboard.pieceList[x][y].setX(x);
		checkerboard.pieceList[x][y].setY(y);
		checkerboard.pieceList[x][y].setSelection(false);
	}
	

	//判断和玩家相同阵营的棋子有没有被选中的
	public Piece isStep(){
		Piece item;
		for (int i = 0; i < checkerboard.pieceList.length; i++) {
			
			for (int j = 0; j < checkerboard.pieceList[i].length; j++) {
				item = checkerboard.pieceList[i][j];
				if(item != null){
					if(item.getSelection() && item.getCamp() == userCamp) return item;
				}
			}
		}
		return null;
	}
	
	
	//判断鼠标点击的地方有没有棋子
	public Piece isEmtry(int x , int y){
		Piece item;
		for (int i = 0; i < checkerboard.pieceList.length; i++) {
			for (int j = 0; j < checkerboard.pieceList[i].length; j++) {
				item = checkerboard.pieceList[i][j];
				if(checkerboard.pieceList[i][j] != null){
					if(item.getX() == x && item.getY() == y) return item;
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClientView view = new ClientView();
	}

	
}
