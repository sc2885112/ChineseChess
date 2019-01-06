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
	JLabel jlHost=new JLabel("������");//������ʾ�����������ı�ǩ
	JLabel jlPort=new JLabel("�˿ں�");////������ʾ����˿ںű�ǩ
	JLabel jlNickName=new JLabel("��    ��");//������ʾ�����ǳƵı�ǩ
	JTextField jtfHost=new JTextField("localhost");//�����������������ı���Ĭ��ֵ��"127.0.0.1"
	JTextField jtfPort=new JTextField("9999");//��������˿ںŵ��ı���Ĭ��ֵ��9999
	JTextField jtfNickName=new JTextField("Play1");//���������ǳƵ��ı���Ĭ��ֵ��Play1
	JButton jbConnection=new JButton("��  ��");//����"����"��ť
	JButton jbExtConnection=new JButton("��  ��");//����"�Ͽ�"��ť
	JButton jbAdmitDefeat=new JButton("��  ��");//����"����"��ť
	JButton jbChallenge=new JButton("��  ս");//����"��ս"��ť
	JComboBox jcbNickList=new JComboBox();//������ŵ�ǰ�û��������б��
	JTextArea textArea = new JTextArea();//���������	
	JScrollPane jscrollPane = new JScrollPane(textArea);
	JTextArea sendMessageText=new JTextArea();//����������Ϣ���ı���
	JButton jbSendMessage=new JButton("����");//����"����"��ť
	Checkerboard checkerboard = new Checkerboard(); //��������;
	Rule rule  = new Rule(checkerboard); //����������
	JPanel rightJPanel = new JPanel();//�����ұ߲˵���
	public String opponentName = null; //���ڶ��ĵĶ���
	public boolean isCanMove = true; //�Ƿ��ֵ�������ı��
	public boolean userCamp = true; //�ж��û���Ӫ
	private ClientThread clientThread ; //�ͻ����߳�
	ClientView clientView = this;
	Socket socket; //���ӵ�
	int preX,preY;
	
	public ClientView(){
		initRightJPanl();//��ʼ���ұ߿�
		initButton();
		initCheckerboard();//��ʼ������
	}
	
	/**
	 * ��ʼ���ұ߿�
	 * @return
	 */
	public void initRightJPanl(){
		rightJPanel.setPreferredSize(new Dimension(200, 700));
		rightJPanel.setLayout(null);//��Ϊ�ղ���
		
		jlHost.setBounds(10,10,50,20);
		rightJPanel.add(jlHost);//���"������"��ǩ
		
		jtfHost.setBounds(100,10,80,20);
		rightJPanel.add(jtfHost);//��������������������ı���
		
		jlPort.setBounds(10,40,50,20);
		rightJPanel.add(jlPort);//���"�˿ں�"��ǩ
		
		jtfPort.setBounds(100,40,80,20);
		rightJPanel.add(jtfPort);//�����������˿ںŵ��ı���
		
		jlNickName.setBounds(10,70,50,20);
		rightJPanel.add(jlNickName);//���"�ǳ�"��ǩ
		
		jtfNickName.setBounds(100,70,80,20);
		rightJPanel.add(jtfNickName);//������������ǳƵ��ı���
		
		jbConnection.setBounds(10,100,80,20);
		rightJPanel.add(jbConnection);//���"����"��ť
		
		jbExtConnection.setBounds(110,100,80,20);
		rightJPanel.add(jbExtConnection);//���"�Ͽ�"��ť
		
		jcbNickList.setBounds(10,130,180,20);
		rightJPanel.add(jcbNickList);//���������ʾ��ǰ�û��������б��
		
		jbChallenge.setBounds(10,160,80,20);
		rightJPanel.add(jbChallenge);//���"��ս"��ť
		
		jbAdmitDefeat.setBounds(110,160,80,20);
		rightJPanel.add(jbAdmitDefeat);//���"����"��ť
		
		jscrollPane.setBounds(10, 190, 180, 400);
		textArea.setEditable(false);//����l����򲻿ɱ༭
		textArea.setBackground(new Color(200, 200, 200));//���ñ���ɫ
		jscrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //���ù�������ֱ�Զ�����
		jscrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//���ù�����ˮƽ�Զ�����
		rightJPanel.add(jscrollPane);//��������
		
		sendMessageText.setBounds(10, 600, 180, 50);
		rightJPanel.add(sendMessageText);//��ӷ��͵���Ϣ��
		
		jbSendMessage.setBounds(130, 660, 60, 20);
		rightJPanel.add(jbSendMessage);//��ӷ��Ͱ�ť
		rightJPanel.setBackground(new Color(128, 128, 128));
	}
	
	public void initButton(){
		//Ϊ��ťע�����
		jbConnection.addActionListener(this);
		jbExtConnection.addActionListener(this);
		jbChallenge.addActionListener(this);
		jbAdmitDefeat.addActionListener(this);
		jbSendMessage.addActionListener(this);
		
		//���ð�ť״̬Ϊ������
		jbExtConnection.setEnabled(false);
		jbChallenge.setEnabled(false);
		jbAdmitDefeat.setEnabled(false);
		jbSendMessage.setEnabled(false);
	}
	
	/**
	 * Ϊ������ťע���¼�
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == jbConnection) openConnection(); //��������
		if (e.getSource() == jbChallenge) launchChallenge();//������ս
		if (e.getSource() == jbExtConnection) extConnection();//�Ͽ�����
		if (e.getSource() == jbAdmitDefeat) admitDefeat(); //����
		if (e.getSource() == jbSendMessage) sendMessage(); //������Ϣ
	}
	
	public void openConnection() {
		int port = 0;
		try {// ����û�����ĶϿںŲ�ת��Ϊ����
			port = Integer.parseInt(jtfPort.getText().trim());
		} catch (Exception e) {// ��������������������ʾ
			JOptionPane.showMessageDialog(this, "�˿ں�ֻ��������", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (port > 65535 || port < 0) {// �˿ںŲ��Ϸ�������������ʾ
			JOptionPane.showMessageDialog(this, "�˿ں�ֻ����0-65535������", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = jtfNickName.getText().trim();// ����ǳ�
		if (name.length() == 0) {// �ǳ�Ϊ�գ�����������ʾ��Ϣ
			JOptionPane.showMessageDialog(this, "�����������Ϊ��", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			socket = new Socket(jtfHost.getText().trim(), port);
			clientThread = new ClientThread(socket, this);// �����ͻ��˴����߳�
			clientThread.start();// �����ͻ��˴����߳�
			jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
			jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
			jbConnection.setEnabled(false);// ��"����"��ť��Ϊ������
			jbExtConnection.setEnabled(true);// ��"�Ͽ�"��ť��Ϊ����
			jbChallenge.setEnabled(true);// ��"��ս"��ť��Ϊ����
			JOptionPane.showMessageDialog(this, "�����ӵ�������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);// ���ӳɹ���������ʾ��Ϣ
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "���ӷ�����ʧ��", "����", JOptionPane.ERROR_MESSAGE);// ����ʧ�ܣ�������ʾ��Ϣ
			return;
		}
	}
	
	
	/**
	 * ������ս
	 */
	public void launchChallenge() {
		try {
			String name = (String)jcbNickList.getSelectedItem();

			if (name == null ||  name.equals("")) {
				JOptionPane.showMessageDialog(this, "��ѡ��Է�����", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}

			clientThread.out.writeUTF(ClientMenu.LAUNCH_CHALLENGE + name);	// �������������ս��Ϣ
			JOptionPane.showMessageDialog(this, "�������ս,��ȴ��ָ�...", "��ʾ", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	//�Ͽ�����
	public void extConnection(){
		try {
			if(clientThread != null){
				if(opponentName != null){//��������ڶ��ĵĶ�������϶��ֵ����ַ���������ط���Ϣ
					clientThread.out.writeUTF(ClientMenu.USER_EXIT + opponentName);//����������ͶϿ����ӵ�����
				}else{
					clientThread.out.writeUTF(ClientMenu.USER_EXIT);//����������ͶϿ����ӵ�����
				}
				
				clientThread.sign = false; //��ֹ�߳�
				clientThread = null;
			}
			userCamp = true;//��ʼ����Ӫ
			opponentName = null;//�����ÿ�
			isCanMove = true;//��ʼ��������
			textArea.setText(null);//��ʼ�������
			//��ʼ�����ְ�ť
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
	
	// ����
	public void admitDefeat() {

		try {
			if (JOptionPane.showConfirmDialog(this, "��ȷ��Ҫ������", "���䣡",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				clientThread.out.writeUTF(ClientMenu.ADMIT_DEFEAT + opponentName);// ��������������������
				jbChallenge.setEnabled(true);// ��"��ս"��ť�����
				jbAdmitDefeat.setEnabled(false);// ��"����"��ť��Ϊ������
				userCamp = true;//��ʼ����Ӫ
				opponentName = null;//�����ÿ�
				isCanMove = true;//��ʼ��������
				checkerboard.initPiece(false);// ��ʼ������
				jbSendMessage.setEnabled(false);//�����Ͱ�ť������
				textArea.setText(null);//��ʼ�������
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * ������Ϣ
	 */
	public void sendMessage() {

		try {
			String message = sendMessageText.getText().trim();
			if (opponentName == null)
				return; // ���û�����ڶ�ս�Ķ���ֱ�ӷ���

			if (message != null && message.length() > 0) {

				clientThread.out.writeUTF(ClientMenu.CHAT + message + "-" + opponentName);// �������������Ϣ
				textArea.append("���"+opponentName+"˵:" + "\n    " + message + "\n\n");
				sendMessageText.setText(null);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	private void initCheckerboard() {
		
		//���������¼�
		checkerboard.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int x = e.getX() / Checkerboard.DIAMETER;
				int y = e.getY() / Checkerboard.DIAMETER;

				boolean isMove = false;

				Piece piece = isStep();
				if (piece != null) {// �ж��ҷ���Ӫ��û�����ӱ�ѡ��
					if(isCanMove){ //�ж��Ƿ��ֵ�������
						if (rule.getRule(piece, x, y)) {// �ж��Ƿ��������
							
							if (checkerboard.pieceList[x][y] == null
									|| checkerboard.pieceList[x][y].getCamp() != piece.getCamp()) {
								
								Piece target = checkerboard.pieceList[x][y];
								int startX = piece.getX();
								int startY = piece.getY();
								pieceMove(piece , x ,y); //����
								checkerboard.repaint();//�ػ�
								
								if (opponentName == null) {
									if (userCamp) {
										userCamp = false;
									} else {
										userCamp = true;
									}
								} else {
									//�����������Ϣ
									try {
										clientThread.out.writeUTF(
												ClientMenu.MOVE + opponentName + "," + startX + "," + startY + "," + x + "," + y);
										
										isCanMove = false;
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
								
								if (target != null)// �ж��Ƿ�ֳ�ʤ��
									isWinning(target, x, y);
								
								isMove = true;
								
							}
						}
					}
					
				}

				// �������ӵ�ѡ��״̬
				if (!isMove) {// �ж��Ƿ�����
					if (isEmtry(x, y) != null) { // �жϵ���ĵط��Ƿ�Ϊ��
						if (checkerboard.pieceList[x][y].getCamp() == userCamp) {// �жϸ�������Ӫ������Ƿ���ͬ
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
		
		//���һ���ر��¼�
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String str = "�Ƿ�Ҫ�˳���Ϸ?";
				// �����Ϣ�Ի���
				if (JOptionPane.showConfirmDialog(clientView, str, "�˳���Ϸ",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					extConnection(); //֪ͨ�������Ͽ�����
					System.exit(0); // �˳�
				}
			}
		});
		
		
		JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,checkerboard,rightJPanel);//����һ��JSplitPane
		jsp.setEnabled(false); //���÷ָ�����ֹ�϶�
		add(jsp);
		setTitle("�й�����");
		setResizable(false);// ���ô˴����Ƿ�����û�������С
		setVisible(true); //���ô�����ʾ״̬
		pack();//���ݴ�������Ĳ��ּ������preferredSize��ȷ��frame����Ѵ�С��
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * �ж��Ƿ�ֳ�ʤ��
	 * @return
	 */
	public void isWinning(Piece target ,int x, int y ){
		
		if (target.getName() == "��" || target.getName() == "��") {
			String resultMessage;
			if (checkerboard.pieceList[x][y].getCamp()) {
				resultMessage = "�췽ʤ��";
			} else {
				resultMessage = "�ڷ�ʤ��";
			}
			JOptionPane.showMessageDialog(this, resultMessage, "��Ϸ����",
					JOptionPane.PLAIN_MESSAGE); // ������Ϣ��

			if(opponentName != null && userCamp){
				checkerboard.initPiece(false);
				isCanMove = true;//��ʼ��������
			}else if(opponentName != null && userCamp == false){
				checkerboard.initPiece(true);
				isCanMove = false;//��ʼ��������
			}else{
				checkerboard.initPiece(false);
			}
		}
		
	}
	
	/**
	 * ����
	 * @return
	 */
	public void pieceMove(Piece piece,int x,int y){
		
		checkerboard.pieceList[x][y] = piece;
		checkerboard.pieceList[piece.getX()][piece.getY()] = null;
		checkerboard.pieceList[x][y].setX(x);
		checkerboard.pieceList[x][y].setY(y);
		checkerboard.pieceList[x][y].setSelection(false);
	}
	

	//�жϺ������ͬ��Ӫ��������û�б�ѡ�е�
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
	
	
	//�ж�������ĵط���û������
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
