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
	//����һ���ı���
	JLabel jlPort=new JLabel("�� �� ��");//������ʾ����˿ںű�ǩ
	JTextField jtfPort=new JTextField("9999");//��������˿ںŵ��ı���
	JButton jbStart=new JButton("����");//����"����"��ť
	JButton jbStop=new JButton("�ر�");//����"�ر�"��ť
	JTextArea textArea = new JTextArea();//��������ı���
	JScrollPane jscrollPane = new JScrollPane(textArea);//����������
	JPanel jps=new JPanel();//����һ��JPanel����
	ConcurrentHashMap<String,ServerThread> onlineList=new ConcurrentHashMap<String,ServerThread>();//��ǰ�����û����߳��б�
	ServerSocket ss;//����ServerSocket����
	ServerView serverView;
	public ServerView(){
		//��ʼ���ұ߿�
		initRightJPanel();
		
		//��ʼ������
		initUi();
		
		serverView = this;
	}
	
	
	public void initUi(){
		
		jscrollPane.setPreferredSize(new Dimension(200, 300));
		textArea.setEditable(false);
		
		jscrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //���ù�������ֱ�Զ�����
		jscrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//���ù�����ˮƽ�Զ�����
		
		JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jscrollPane,jps);//����һ��JSplitPane
		this.add(jsp);
		this.setTitle("������");
		this.setResizable(false);// ���ô˴����Ƿ� �����û�������С
		this.setVisible(true); //���ô�����ʾ״̬
		this.pack();//���ݴ�������Ĳ��ּ������preferredSize��ȷ��frame����Ѵ�С��
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void initRightJPanel(){
		jps.setPreferredSize(new Dimension(200, 300));
		jps.setLayout(null);//��Ϊ�ղ���
		
		//���ÿؼ�����ʹ�С
		jlPort.setBounds(20,20,50,20);
		jtfPort.setBounds(85,20,60,20);
		jbStop.setBounds(85,50,60,20);
		jbStart.setBounds(18,50,60,20);
		
		jps.add(jlPort);
		jps.add(jtfPort);
		jps.add(jbStop);
		jps.add(jbStart);
		
		//����ťע���¼�����
		jbStop.addActionListener(this);
		jbStart.addActionListener(this);
		//�رհ�ť��ʼ����
		jbStop.setEnabled(false);
	}
	
	
	/**
	 * Ϊ�����͹ر�ע���¼�
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbStart) StartEvent();// ������"����"��ťʱ
		
		if (e.getSource() == jbStop) {// ����"�ر�"��ť��
			
		}

	}
	
	/**
	 * ������ť���¼�
	 * @param args
	 */
	public void StartEvent(){
		int port = 0;
		
		try {
			port=Integer.parseInt(this.jtfPort.getText().trim());//����û�����Ķ˿ںţ���ת��Ϊ����
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ��������","����",JOptionPane.ERROR_MESSAGE);
			return;
		}
	
		if (port > 65535 || port < 0) {// �ϿںŲ��Ϸ���������ʾ��Ϣ
			JOptionPane.showMessageDialog(this, "�˿ں�ֻ����0-65535������", "��Ϣ", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			
			ss=new ServerSocket(port);//����ServerSocket��������˿�
			
			/**
			 * �������������߳�
			 * ʵʱ�����˿ڣ����������������ʱ�򴴽��������߳�
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
						System.err.println("�����˿�ʱ���ִ���");
						e.printStackTrace();
					}
					
				}
			}).start();
			
			
			//���������ɹ�����Ϣ
			JOptionPane.showMessageDialog(this,"�����ɹ�","��Ϣ",JOptionPane.INFORMATION_MESSAGE);
			jbStart.setEnabled(false);//����������ť
			jbStop.setEnabled(true);//�����رհ�ť
			
		} catch (IOException e) {
			System.err.println("���������ťʱ���ִ���");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �����û������б�
	 * @param args
	 */
	public void reUsersList() {
		textArea.setText(null);
		ServerThread serverThread;
		
		Enumeration <String> keys = onlineList.keys();
		
		while(keys.hasMoreElements()){
			serverThread = onlineList.get(keys.nextElement());
			String msg = "";
			msg += "IP:" + serverThread.socket.getInetAddress().toString() + "\n"; //ƴ��IP
			msg += "Port:" + serverThread.socket.getPort() + "\n"; //ƴ���ǳ�
			msg += "name:" + serverThread.getName() + "\n\n"; //ƴ���ǳ�
			textArea.append(msg);
		}
		
	}
	
	public static void main(String[] args) {
		
		ServerView sv = new ServerView();
		
		//ServerView sv2 = new ServerView();
	}


	
}
