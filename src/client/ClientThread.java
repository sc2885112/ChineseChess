package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import enu.ClientMenu;

public class ClientThread extends Thread{
	
	ClientView clientView;
	Socket socket;
	DataInputStream in;//����������
	DataOutputStream out;//���������
	boolean sign = true;
	
	public ClientThread(Socket socket , ClientView clientView){
		this.socket = socket;
		this.clientView = clientView;
		try {
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			
			String name=clientView.jtfNickName.getText().trim();//����ǳ�
			out.writeUTF(ClientMenu.REQUEST_CONNECTION+name);//�����ǳƵ�������
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(sign){
			try {
				String message=in.readUTF().trim();
				if(message.startsWith(ClientMenu.RE_USERS_LIST)) reUsersLiset(message);//�յ��б�ˢ����Ϣ
				if(message.startsWith(ClientMenu.REPEAT_NAME)) repeatName(message);//�յ���������Ϣ
				if(message.startsWith(ClientMenu.LAUNCH_CHALLENGE))receiveChallenge(message);//�յ�������ս��Ϣ
				if(message.startsWith(ClientMenu.ACCEPT_CHALLENGE))acceptChallenge(message);//�յ�������ս����Ϣ
				if(message.startsWith(ClientMenu.REFUSE_CHALLENGE))refuseChallenge(message);//�յ��ܾ���ս����Ϣ
				if(message.startsWith(ClientMenu.ADMIT_DEFEAT)) admitDefeat(); //�յ��������Ϣ
				if(message.startsWith(ClientMenu.MOVE))pieceMove(message);//����
				if(message.startsWith(ClientMenu.OPPONENT_EXIT))opponentExit();//��������
				if(message.startsWith(ClientMenu.CHAT))chatMessage(message);//�յ���������Ϣ
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void reUsersLiset(String msg){
		String s = msg.substring(12);// �ֽⲢ�õ�������Ϣ
		String[] na = s.split(",");
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < na.length; i++) {
			if (na[i].trim().length() != 0 && 
					(!na[i].trim().equals(clientView.jtfNickName.getText().trim()))) { //����ַ�����λ�ղ��Ҳ����ڱ����ǳ�
				v.add(na[i]);
			}
		}
		clientView.jcbNickList.setModel(new DefaultComboBoxModel(v));// ���������б��ֵ
	}
	
	public void receiveChallenge(String message) {
		try {
			String name = message.substring(16);
			Object[] options = { "����", "�ܾ�" };
			
			int m = JOptionPane.showOptionDialog(clientView, name + "����������ս����", "�µ���ս��", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (m == JOptionPane.YES_OPTION) {
				//��ʼ������
				clientView.checkerboard.initPiece(true);
				
				//������Ӫ������ս��Ϊ�ڷ�
				clientView.userCamp = false;
				
				//�����Ƿ��Ѿ�����ҿ�ʼ���ĵı��Ϊtrue
				clientView.opponentName = name;
				
				clientView.isCanMove = false;
				clientView.jbChallenge.setEnabled(false);// ��"��ս"��ť��Ϊ������
				clientView.jbAdmitDefeat.setEnabled(true);// ��"����"��ť��Ϊ����
				clientView.jbSendMessage.setEnabled(true);//�����Ͱ�ť����
				// ����������ͽ�����ս����Ϣ
				out.writeUTF(ClientMenu.ACCEPT_CHALLENGE + name);
				
			} else {
				// ������;ܾ���ս����Ϣ
				out.writeUTF(ClientMenu.REFUSE_CHALLENGE + name);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void acceptChallenge(String message){
		try {
			String name = message.substring(16);
			//��ʼ������
			clientView.checkerboard.initPiece(false);
			//������Ӫ������ս��Ϊ�췽
			clientView.userCamp = true;
			//�����Ƿ��Ѿ�����ҿ�ʼ���ĵı��Ϊtrue
			clientView.opponentName = name;
			
			clientView.isCanMove = true;
			
			JOptionPane.showMessageDialog(clientView, name+"���ܵ�������ս����\n��ʼ��ս��!", "������ս����",JOptionPane.PLAIN_MESSAGE);
			clientView.jbChallenge.setEnabled(false);// ��"��ս"��ť��Ϊ������
			clientView.jbAdmitDefeat.setEnabled(true);// ��"����"��ť��Ϊ����
			clientView.jbSendMessage.setEnabled(true);//�����Ͱ�ť����
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void pieceMove(String message){
		String[] msg = message.split(",");
		
		int startX =  8 - Integer.parseInt(msg[1]);
		int startY = 9 - Integer.parseInt(msg[2]);
		int endX = 8 - Integer.parseInt(msg[3]);
		int endY = 9 - Integer.parseInt(msg[4]);
		
		Piece target = clientView.checkerboard.pieceList[endX][endY];
		
		clientView.pieceMove(clientView.checkerboard.pieceList[startX][startY], endX, endY); //����
		
		clientView.isCanMove = true; //�޸Ŀ����ƶ��ı��
		clientView.checkerboard.repaint();
		
		if (target != null)// �ж��Ƿ�ֳ�ʤ��
			clientView.isWinning(target, endX, endY);
	}
	
	
	public void repeatName(String message){
		try {
			JOptionPane.showMessageDialog(clientView, "�����ǳ��ѱ�ռ��!");  
			in.close();
			out.close();
			socket.close();
			sign = false;
			clientView.jtfHost.setEnabled(true);// �������������������ı�����Ϊ����
			clientView.jtfPort.setEnabled(true);// ����������˿ںŵ��ı�����Ϊ����
			clientView.jtfNickName.setEnabled(true);// �����������ǳƵ��ı�����Ϊ����
			clientView.jbConnection.setEnabled(true);// ��"����"��ť��Ϊ����
			clientView.jbExtConnection.setEnabled(false);// ��"�Ͽ�"��ť��Ϊ������
			clientView.jbChallenge.setEnabled(false);// ��"��ս"��ť��Ϊ������
			clientView.jbAdmitDefeat.setEnabled(false);// ��"����"��ť��Ϊ������
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void refuseChallenge(String message){
		String name = message.substring(16);
		JOptionPane.showMessageDialog(clientView, name + "�ܾ���������ս!!" , "�ܾ���ս", JOptionPane.PLAIN_MESSAGE);  
	}
	
	
	public void admitDefeat(){
		JOptionPane.showMessageDialog(clientView, "���Ķ��������ˣ�" , "ʤ��", JOptionPane.PLAIN_MESSAGE);
		if(clientView.userCamp){
			clientView.checkerboard.initPiece(false);//��ʼ������
		}else{
			clientView.checkerboard.initPiece(true);//��ʼ������
		}
		clientView.opponentName = null; //�������ÿ�
		clientView.isCanMove = true;//��ʼ��������
		clientView.jbChallenge.setEnabled(true);// ��"��ս"��ť�����
		clientView.jbAdmitDefeat.setEnabled(false);// ��"����"��ť��Ϊ������
		clientView.jbSendMessage.setEnabled(false);//�����Ͱ�ť������
		clientView.textArea.setText(null);//��������
	}
	
	public void opponentExit(){
		JOptionPane.showMessageDialog(clientView, "���Ķ����˳���,��Ӯ�ˣ�" , "ʤ��", JOptionPane.PLAIN_MESSAGE);  
		if(clientView.userCamp){
			clientView.checkerboard.initPiece(false);//��ʼ������
		}else{
			clientView.checkerboard.initPiece(true);//��ʼ������
		}
		clientView.opponentName = null; //�������ÿ�
		clientView.isCanMove = true;//��ʼ��������
		clientView.jbChallenge.setEnabled(true);// ��"��ս"��ť�����
		clientView.jbAdmitDefeat.setEnabled(false);// ��"����"��ť��Ϊ������
		clientView.jbSendMessage.setEnabled(false);//�����Ͱ�ť������
		clientView.textArea.setText(null);//��������
	}
	
	public void chatMessage(String message){
		String name = message.substring(message.lastIndexOf("-") + 1 , message.length());
		message = message.substring(5,message.lastIndexOf("-"));
		
		clientView.textArea.append(name + "����˵:\n    " + message + "\n\n");
	}
}
