package TCP;

import TCP.Operation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//������ ����
public class Server {

	private ServerSocket serverSocket;
	
	public Server(){
		try{
			serverSocket = new ServerSocket(8088);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void start(){
		try{
			System.out.println("�ȴ��ͻ������ӡ�����");
			//���������������ֱ��ĳ��Socket���ӣ������������ӵ�Socket
			Socket socket = serverSocket.accept();
			System.out.println("�ͻ��������ӣ�");
			InputStream in = socket.getInputStream();
			ObjectInputStream isr = new ObjectInputStream(in);
//			BufferedReader br = new BufferedReader(isr);
			
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter  pw = new BufferedWriter (osw);
			//System.out.println("�ͻ���˵��" + br.readLine());
			//���϶�ȡ�ͻ�������
			HashMap userMap = new HashMap();
			int index = 0;
			while(true){
				//System.out.println("�ͻ���˵��" + br.readLine());
				Operation operationObj = null;
				try
				{
					operationObj = (Operation) isr.readObject();
				}
				catch(Exception ex)
                {
                    //ex.printStackTrace();
                    socket.close();
                    break;
                }
				System.out.println(operationObj.m_operationName);
				index++;
				if(operationObj.m_operationName.equals("register"))
				{
					System.out.println("register�ظ�");
					if(userMap.containsKey(operationObj.m_user))
					{
						System.out.println("register�ظ�");
						pw.write("ע��ʧ�ܣ��Ѵ��ڵ��˺�");
						pw.newLine();
						pw.flush();
					}
					else
					{
						System.out.println("register�ظ�2");
						userMap.put(operationObj.m_user, operationObj.m_password);
						pw.write("ע��ɹ������½");
						pw.newLine();
						pw.flush();
					}
				}
				else if(operationObj.m_operationName.equals("login"))
				{
					System.out.println("login�ظ�");
					if(userMap.containsKey(operationObj.m_user) &&
							userMap.get(operationObj.m_user).equals(operationObj.m_password))
					{
						pw.write("��½�ɹ�");
						pw.newLine();
						pw.flush();
					}
					else
					{
						pw.write("������û���������");
						pw.newLine();
						pw.flush();
					}
				}
				else if(operationObj.m_operationName.equals("findpassword"))
				{
					System.out.println("findpassword�ظ�");
					if(userMap.containsKey(operationObj.m_user)){
					pw.write("�����һسɹ���Ϊ�������˻���ȫ����������������");
					pw.newLine();
					pw.flush();
					}
					else
					{
						pw.write("������û����������һ�ʧ��");
						pw.newLine();
						pw.flush();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		server.start();
	}

}

