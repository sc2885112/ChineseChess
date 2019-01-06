package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;

import enu.ClientMenu;

/**
 * 服务线程 为每一位连接到端口的客户服务
 * @author lenovo
 */
public class ServerThread extends Thread {
	ServerView serverView;
	Socket socket;
	DataInputStream in;//数据输入流
	DataOutputStream out;//数据输出流
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
				String message=in.readUTF().trim();//接收客户端传来的信息
				
				if(message.startsWith(ClientMenu.REQUEST_CONNECTION)) requestConnection(message);//请求连接
				if(message.startsWith(ClientMenu.LAUNCH_CHALLENGE))launchChallenge(message);//发起挑战
				if(message.startsWith(ClientMenu.ACCEPT_CHALLENGE))acceptChallenge(message);//接受挑战
				if(message.startsWith(ClientMenu.REFUSE_CHALLENGE))refuseChallenge(message);//拒绝挑战
				if(message.startsWith(ClientMenu.ADMIT_DEFEAT))admitDefeat(message);//认输
				if(message.startsWith(ClientMenu.MOVE))pieceMove(message);//走棋
				if(message.startsWith(ClientMenu.USER_EXIT))userExit(message);//断开连接
				if(message.startsWith(ClientMenu.CHAT))sendMessage(message);//聊天信息
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public void requestConnection(String message) {
		try {
			String name = message.substring(18);// 截取用户昵称
			this.setName(name);// 用该昵称给该线程取名
			
			if (serverView.onlineList.get(name) != null) {// 如果重名
				out.writeUTF(ClientMenu.REPEAT_NAME);// 将重名信息发送给客户端
				in.close();
				out.close();
				socket.close();
				sign = false;
			} else {
				serverView.onlineList.put(name,this);// 将该线程添加到在线列表
				serverView.reUsersList();// 刷新服务器在线信息列表

				String nickListMsg = "";
				
				Enumeration<String> keys = serverView.onlineList.keys();
				while(keys.hasMoreElements()){
					nickListMsg = nickListMsg + "," + keys.nextElement();// 将在线列表内容住组织成字符串
				}
				
				nickListMsg = ClientMenu.RE_USERS_LIST + nickListMsg;
				
				keys = serverView.onlineList.keys();
				ServerThread satTemp;
				while(keys.hasMoreElements()){
					satTemp = serverView.onlineList.get(keys.nextElement());
					satTemp.out.writeUTF(nickListMsg);// 将最新的列表信息发送到各个客户端
					//satTemp.out.writeUTF("<#MSG#>" + name + "上线了..."); //将上线信息发送给各个客户端
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void launchChallenge(String message) {
		try {
			String name = message.substring(16);// 截取用户昵称
			ServerThread serverThread = serverView.onlineList.get(name);
			if(serverThread != null){//如果不为空的话发送挑战信息
				serverThread.out.writeUTF(ClientMenu.LAUNCH_CHALLENGE + this.getName());
			}else{//如果为空的话回发离线信息
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
			//向客户端发送接受挑战的信息
			if(serverThread != null){
				serverThread.out.writeUTF(ClientMenu.ACCEPT_CHALLENGE + this.getName());
			}else{//如果为空的话回发离线信息
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
			if(!"".equals(name)){//如果name不为空的话向对手发送离线消息
				serverView.onlineList.get(name).out.writeUTF(ClientMenu.OPPONENT_EXIT);
			}
			
			serverView.onlineList.remove(this.getName());
			serverView.reUsersList();
			sign = false; //关闭线程
			
			String nickListMsg = "";
			
			Enumeration<String> keys = serverView.onlineList.keys();
			while(keys.hasMoreElements()){
				nickListMsg = nickListMsg + "," + keys.nextElement();// 将在线列表内容住组织成字符串
			}
			
			nickListMsg = ClientMenu.RE_USERS_LIST + nickListMsg;
			
			keys = serverView.onlineList.keys();
			ServerThread satTemp;
			while(keys.hasMoreElements()){
				satTemp = serverView.onlineList.get(keys.nextElement());
				satTemp.out.writeUTF(nickListMsg);// 将最新的列表信息发送到各个客户端
				//satTemp.out.writeUTF("<#MSG#>" + this.getName() + "下线了..."); //将上线信息发送给各个客户端
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
