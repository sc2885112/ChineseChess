package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;

import enu.ClientMenu;

/**
 * �����߳� Ϊÿһλ���ӵ��˿ڵĿͻ�����
 * @author lenovo
 */
public class ServerThread extends Thread {
	ServerView serverView;
	Socket socket;
	DataInputStream in;//����������
	DataOutputStream out;//���������
	boolean sign = true;

	public ServerThread(Socket socket , ServerView serverView) {
		try {
			this.socket = socket;
			this.serverView = serverView;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		while(sign){
			try {
				String message=in.readUTF().trim();//���տͻ��˴�������Ϣ
				
				if(message.startsWith(ClientMenu.REQUEST_CONNECTION)) requestConnection(message);//��������
				if(message.startsWith(ClientMenu.LAUNCH_CHALLENGE))launchChallenge(message);//������ս
				if(message.startsWith(ClientMenu.ACCEPT_CHALLENGE))acceptChallenge(message);//������ս
				if(message.startsWith(ClientMenu.REFUSE_CHALLENGE))refuseChallenge(message);//�ܾ���ս
				if(message.startsWith(ClientMenu.ADMIT_DEFEAT))admitDefeat(message);//����
				if(message.startsWith(ClientMenu.MOVE))pieceMove(message);//����
				if(message.startsWith(ClientMenu.USER_EXIT))userExit(message);//�Ͽ�����
				if(message.startsWith(ClientMenu.CHAT))sendMessage(message);//������Ϣ
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public void requestConnection(String message) {
		try {
			String name = message.substring(18);// ��ȡ�û��ǳ�
			this.setName(name);// �ø��ǳƸ����߳�ȡ��
			
			if (serverView.onlineList.get(name) != null) {// �������
				out.writeUTF(ClientMenu.REPEAT_NAME);// ��������Ϣ���͸��ͻ���
				in.close();
				out.close();
				socket.close();
				sign = false;
			} else {
				serverView.onlineList.put(name,this);// �����߳���ӵ������б�
				serverView.reUsersList();// ˢ�·�����������Ϣ�б�

				String nickListMsg = "";
				
				Enumeration<String> keys = serverView.onlineList.keys();
				while(keys.hasMoreElements()){
					nickListMsg = nickListMsg + "," + keys.nextElement();// �������б�����ס��֯���ַ���
				}
				
				nickListMsg = ClientMenu.RE_USERS_LIST + nickListMsg;
				
				keys = serverView.onlineList.keys();
				ServerThread satTemp;
				while(keys.hasMoreElements()){
					satTemp = serverView.onlineList.get(keys.nextElement());
					satTemp.out.writeUTF(nickListMsg);// �����µ��б���Ϣ���͵������ͻ���
					//satTemp.out.writeUTF("<#MSG#>" + name + "������..."); //��������Ϣ���͸������ͻ���
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void launchChallenge(String message) {
		try {
			String name = message.substring(16);// ��ȡ�û��ǳ�
			ServerThread serverThread = serverView.onlineList.get(name);
			if(serverThread != null){//�����Ϊ�յĻ�������ս��Ϣ
				serverThread.out.writeUTF(ClientMenu.LAUNCH_CHALLENGE + this.getName());
			}else{//���Ϊ�յĻ��ط�������Ϣ
				out.writeUTF(ClientMenu.USER_EXIT + name);
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void acceptChallenge(String message){
		
		try {
			String name = message.substring(16);
			
			ServerThread serverThread = serverView.onlineList.get(name);
			//��ͻ��˷��ͽ�����ս����Ϣ
			if(serverThread != null){
				serverThread.out.writeUTF(ClientMenu.ACCEPT_CHALLENGE + this.getName());
			}else{//���Ϊ�յĻ��ط�������Ϣ
				out.writeUTF(ClientMenu.USER_EXIT + name);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pieceMove(String message){
		try {
			String name = message.substring(5).split(",")[0];
			ServerThread serverThread = serverView.onlineList.get(name);
			serverThread.out.writeUTF(ClientMenu.MOVE + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refuseChallenge(String message){
		try {
			String name = message.substring(16);
			ServerThread serverThread = serverView.onlineList.get(name);
			serverThread.out.writeUTF(ClientMenu.REFUSE_CHALLENGE + this.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void userExit(String message){
		try {
			String name = message.substring(9);
			if(!"".equals(name)){//���name��Ϊ�յĻ�����ַ���������Ϣ
				serverView.onlineList.get(name).out.writeUTF(ClientMenu.OPPONENT_EXIT);
			}
			
			serverView.onlineList.remove(this.getName());
			serverView.reUsersList();
			sign = false; //�ر��߳�
			
			String nickListMsg = "";
			
			Enumeration<String> keys = serverView.onlineList.keys();
			while(keys.hasMoreElements()){
				nickListMsg = nickListMsg + "," + keys.nextElement();// �������б�����ס��֯���ַ���
			}
			
			nickListMsg = ClientMenu.RE_USERS_LIST + nickListMsg;
			
			keys = serverView.onlineList.keys();
			ServerThread satTemp;
			while(keys.hasMoreElements()){
				satTemp = serverView.onlineList.get(keys.nextElement());
				satTemp.out.writeUTF(nickListMsg);// �����µ��б���Ϣ���͵������ͻ���
				//satTemp.out.writeUTF("<#MSG#>" + this.getName() + "������..."); //��������Ϣ���͸������ͻ���
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void admitDefeat(String message){
		try {
			String name = message.substring(12);
			ServerThread serverThread = serverView.onlineList.get(name);
			serverThread.out.writeUTF(ClientMenu.ADMIT_DEFEAT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(String message){
		try {
			String name = message.substring(message.lastIndexOf("-") + 1 , message.length());
			ServerThread serverThread = serverView.onlineList.get(name);
			serverThread.out.writeUTF(message.substring(0,message.lastIndexOf("-")) + "-" + this.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
