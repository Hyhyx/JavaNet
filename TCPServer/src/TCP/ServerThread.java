package TCP;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
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
	public boolean m_continue = true;
	public static HashMap m_userInfoMap = new HashMap();
	public static List m_userOnline = new ArrayList();
	
	
	public ServerThread(Socket socket)
	{
		m_socket = socket;
	}
	
	public void ResponseToClient(String responseStr)
	{
		try {
			m_bufferWriter.write(responseStr);
			m_bufferWriter.newLine();
			m_bufferWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DealRegister()
	{
		System.out.println("register�ظ�");
		if(m_userInfoMap.containsKey(m_operationObj.m_user))
		{
			System.out.println("register�ظ�");
			ResponseToClient("ע��ʧ�ܣ��Ѵ��ڵ��˺�");
		}
		else
		{
			System.out.println("register�ظ�2");
			m_userInfoMap.put(m_operationObj.m_user, m_operationObj.m_userInfo);
//			m_userInfoMap.put(m_operationObj.m_user, m_operationObj.m_password);
			ResponseToClient("ע��ɹ������½");
		}
	}
	
	
	public void DealLogin()
	{
		System.out.println("login�ظ�");
		UserInfo correctUserInfo = (UserInfo) m_userInfoMap.get(m_operationObj.m_user);
		if(m_userInfoMap.containsKey(m_operationObj.m_user) &&
				correctUserInfo.m_password.equals(m_operationObj.m_password))
		{
			m_userOnline.add(m_operationObj.m_user);
			ResponseToClient("��½�ɹ�");
		}
		else
		{
			ResponseToClient("������û���������");
		}
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
				ResponseToClient("�����һسɹ�,���½");
				m_userInfoMap.replace(m_operationObj.m_user, newUserInfo);
			}
			else
			{
				ResponseToClient("����𰸴��������һ�ʧ��");
			}
			
		}
		else
		{
			ResponseToClient("�����ڵ��û��������һ�ʧ��");
		}
	}
	
	public void Init()
	{
		try {
			m_objInputStream = new ObjectInputStream(m_socket.getInputStream());
			m_bufferWriter = new BufferedWriter (new OutputStreamWriter(m_socket.getOutputStream(), "UTF-8"));
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
			else if(m_operationObj.m_operationName.equals("logoff"))
			{
				if(m_userOnline.contains(m_operationObj.m_user))
					m_userOnline.remove(m_operationObj.m_user);
				try {
					m_socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			}
		}
	}
	
}