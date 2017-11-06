package TCP;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerThread extends Thread
{
	public Socket m_socket = null;
	public BufferedWriter m_bufferWriter = null;
	public Operation m_operationObj = null;
	public ObjectInputStream m_objInputStream = null;
	public ObjectOutputStream m_objOutputStream = null;
	public boolean m_continue = true;
	public static HashMap<String, UserInfo> m_userInfoMap = new HashMap<String, UserInfo>();
	public static HashMap m_userOnlineMap = new HashMap<String, OnlineUserInfo>();
	public HashMap m_offlineMsgMap = new HashMap<String, List<String>>();
	
	private class OnlineUserInfo{
		public String m_ip = null;
		public String m_udpIp = null;
		public int m_port = 0;
		public int m_udpPort = 0;
		
		OnlineUserInfo(String ip, int port, String udpIp, int udpPort)
		{
			m_ip = ip;
			m_port = port;
			m_udpIp = udpIp;
			m_udpPort = udpPort;
		}
	}
	
	public ServerThread(Socket socket)
	{
		m_socket = socket;
	}
	
	public void DealRegister()
	{
		System.out.println("register�ظ�");
		if(m_userInfoMap.containsKey(m_operationObj.m_user))
		{
			System.out.println("register�ظ�");
			Operation operation = new Operation();
			operation.m_operationName = "registerFail";
			operation.m_detail = "�Ѵ��ڵ��û�";
			try {
				m_objOutputStream.writeObject(operation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("register�ظ�2");
			m_userInfoMap.put(m_operationObj.m_user, m_operationObj.m_userInfo);
//			m_userInfoMap.put(m_operationObj.m_user, m_operationObj.m_password);
			Operation operation = new Operation();
			operation.m_operationName = "registerSuccess";
			try {
				m_objOutputStream.writeObject(operation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void DealLogin()
	{
		System.out.println("login�ظ�");
		UserInfo correctUserInfo = (UserInfo) m_userInfoMap.get(m_operationObj.m_user);
		if(m_userInfoMap.containsKey(m_operationObj.m_user) &&
				correctUserInfo.m_password.equals(m_operationObj.m_password))
		{
			m_userOnlineMap.put(m_operationObj.m_user, new OnlineUserInfo(m_operationObj.m_ip, m_operationObj.m_port,
					m_operationObj.m_udpIp, m_operationObj.m_udpPort));
			Operation operation = new Operation();
			operation.m_operationName = "loginSuccess";
			try {
				m_objOutputStream.writeObject(operation);
				//�û��б�
				Operation operation2 = new Operation();
				operation2.m_operationName = "userListRsp";
				operation2.m_users = "";
				operation2.m_userStates = "";
				for (String key: m_userInfoMap.keySet()) { 
					operation2.m_users += key;
					operation2.m_users += "\n";
					if(m_userOnlineMap.containsKey(key))
					{
						operation2.m_userStates += "online";
					}
					else
					{
						operation2.m_userStates += "offline";
					}
					operation2.m_userStates += "\n";
				}  
				m_objOutputStream.writeObject(operation2);
				//������Ϣ
				Operation operation3 = new Operation();
				operation3.m_operationName = "offlineMsgRsp";
				operation3.m_msg = "";
				if(m_offlineMsgMap.containsKey(m_operationObj.m_user))
				{
					for(String msg: (ArrayList<String>)m_offlineMsgMap.get(m_operationObj.m_user))
					{
						operation3.m_msg += msg;
						operation3.m_msg += "\n";
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Operation operation = new Operation();
			operation.m_operationName = "loginFail";
			operation.m_detail = "����ȷ���˺Ż�����";
			try {
				m_objOutputStream.writeObject(operation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void DealLogoff()
	{
		if(m_userOnlineMap.containsKey(m_operationObj.m_user))
			m_userOnlineMap.remove(m_operationObj.m_user);
		try {
			m_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DealGetOtherClientAddr()
	{
		Operation operation = new Operation();
		operation.m_operationName = "getOtherClientAddrRsp";
		OnlineUserInfo onlineUserInfo = (OnlineUserInfo) m_userOnlineMap.get(m_operationObj.m_dstUser);
		operation.m_ip = onlineUserInfo.m_ip;
		operation.m_port = onlineUserInfo.m_port;
		try {
			m_objOutputStream.writeObject(operation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DealSendOfflineMsg()
	{
		if(!m_offlineMsgMap.containsKey(m_operationObj.m_dstUser))
		{
			m_offlineMsgMap.put(m_operationObj.m_dstUser, new ArrayList<String>());
		}
		ArrayList<String> list = (ArrayList<String>) m_offlineMsgMap.get(m_operationObj.m_dstUser);
		list.add(m_operationObj.m_msg);
	}
	
	public void DealFindPassWord()
	{
		System.out.println("findpassword�ظ�");
		if(m_userInfoMap.containsKey(m_operationObj.m_user))
		{
			UserInfo correctUserInfo = (UserInfo) m_userInfoMap.get(m_operationObj.m_user);
			UserInfo newUserInfo = m_operationObj.m_userInfo;
			if(correctUserInfo.m_quessionAnswer.equals(newUserInfo.m_quessionAnswer))
			{
				m_userInfoMap.replace(m_operationObj.m_user, newUserInfo);
				Operation operation = new Operation();
				operation.m_operationName = "findpasswordSuccess";
				operation.m_detail = "����ȷ���˺Ż�����";
				try {
					m_objOutputStream.writeObject(operation);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Operation operation = new Operation();
				operation.m_operationName = "findpasswordFail";
				operation.m_detail = "�ܱ��𰸴���";
				try {
					m_objOutputStream.writeObject(operation);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		else
		{
			Operation operation = new Operation();
			operation.m_operationName = "findpasswordFail";
			operation.m_detail = "�����ڵ��û�";
			try {
				m_objOutputStream.writeObject(operation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void Init()
	{
		try {
			m_objInputStream = new ObjectInputStream(m_socket.getInputStream());
			m_objOutputStream = new ObjectOutputStream(m_socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		Init();
		System.out.println("�ͻ��������ӣ�");
		while(m_continue)
		{
			try {
				m_operationObj = (Operation) m_objInputStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			System.out.println(m_operationObj.m_operationName);
			if(m_operationObj.m_operationName.equals("register"))
			{
				DealRegister();
			}
			else if(m_operationObj.m_operationName.equals("login"))
			{
				DealLogin();
			}
			else if(m_operationObj.m_operationName.equals("findpassword"))
			{
				DealFindPassWord();
			}
			else if(m_operationObj.m_operationName.equals("getOtherClientAddr"))
			{
				DealGetOtherClientAddr();
			}
			else if(m_operationObj.m_operationName.equals("sendOfflineMsg"))
			{
				DealSendOfflineMsg();
			}
			else if(m_operationObj.m_operationName.equals("logoff"))
			{
				DealLogoff();
				break;
			}
		}
	}
	
}