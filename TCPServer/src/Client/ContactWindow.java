package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import TCP.Operation;

//��½���������
public class ContactWindow extends JFrame {
	DefaultListModel m_listModel = new DefaultListModel();
	JList m_userList = new JList(m_listModel);
	JScrollPane m_userScrollPane = new JScrollPane(m_userList);
	JLabel m_userLabel = new JLabel("", JLabel.CENTER);
	int m_contactWidth = 250;
	int m_contactHeight = 600;
	String m_user = null;
	public ClientSocket m_clientSocket = new ClientSocket();
	public HashMap<String, ArrayList<ChatHistory>> m_userChatHistory = new HashMap<String, ArrayList<ChatHistory>>();
	
	ContactWindow(String user, ClientSocket clientSocket)
	{
		m_user = user;
		
		m_clientSocket = clientSocket;
		m_clientSocket.m_contaContactWindow = this;
		
		Font font=new Font("����", Font.PLAIN, 20);
		m_userLabel.setFont(font);
		m_userLabel.setText(user);
		m_userLabel.setBounds(0, 0, m_contactWidth, 50);
		
		setLayout(null);
		
		setSize(m_contactWidth, m_contactHeight);
		setResizable(false);
		setVisible(true);
		WindowUtil.SetLocationRight(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		m_listModel.addElement("Debbie Scott");
		m_listModel.addElement("Debbie Scott");
		m_listModel.addElement("Debbie Scott");
		m_listModel.addElement("Debbie Scott");
		m_userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_userList.setFixedCellHeight(40);
		m_userScrollPane.setBounds(0, 50, m_contactWidth, 550);
		
		//˫���û��������촰��
		m_userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
//                        Object o = theList.getModel().getElementAt(index);
//                        field.setText(o.toString());
//                    	m_userList.setSelectionBackground(new Color(0xFF0000));
//                    	m_userList.
//                    	JList theList2 = (JList) m_userList.getSelectedValue();
//                    	((JList) m_userList.getModel().getElementAt(index)).setSelectionBackground(new Color(0xFF0000));
                    	ChatWindow chatWindow = new ChatWindow("liulurong");
                    }
                }
            }
		});
		
		add(m_userLabel);
		add(m_userScrollPane);
	}
	
	//��һ����Ϣ
	private class ChatHistory{
		String m_sender;
		String m_message;
		ChatHistory(String sender, String message){
			m_sender = sender;
			m_message = message;
		}
	}
	
	//���촰��
	private class ChatWindow extends JFrame implements ActionListener{
		int m_chatWidth = 607;
		int m_chatHeight = 400;
		JTextArea m_chatArea = new JTextArea(10, 15);
		int m_chatFontSize = 20;
		JTextArea m_inputArea = new JTextArea(10, 15);
		JButton m_closeButton = new JButton("close");
		JButton m_sendButton = new JButton("send");
		JPanel m_operationPanel = new JPanel();
		String m_chatTargeUser = null;
		

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		} 
		
		public ChatWindow(String targetUser) {
			m_chatTargeUser = targetUser;
			
			setLayout(null);
			setTitle(targetUser);
			
			setSize(m_chatWidth, m_chatHeight);
			setResizable(false);
			setVisible(true);
			WindowUtil.SetLocationCenter(this);
			
			Font font=new Font("����", Font.PLAIN, m_chatFontSize);
			m_chatArea.setFont(font);
			m_chatArea.setBounds(0, 0, m_chatWidth - 7, 240);
			m_chatArea.setLineWrap(true);
			m_chatArea.setEditable(false);
			m_inputArea.setBounds(0, 242, m_chatWidth, 98);
			m_inputArea.setFont(font);
			m_inputArea.addKeyListener(new KeyAdapter() {   
                public void keyPressed(KeyEvent arg0) {   
                    //Ctrl+Enter��ϼ�����ʱ��Ӧ   
                    if ((arg0.getKeyCode() == KeyEvent.VK_ENTER) && (arg0.isControlDown())) {   
                    	String inputContent = m_inputArea.getText();
    					m_chatArea.append(GetChatRowKeepRight(inputContent) + "\n");
    					m_inputArea.setText("");
                     }   
                 }   
             });   
			
			
			m_operationPanel.setLayout(null);
			m_operationPanel.setBackground(new Color(0xFFFFFF));
			m_operationPanel.setBounds(0, 330, m_chatWidth, 60);
			m_closeButton.setBounds(m_chatWidth - 230, 10, 100, 25);
			m_sendButton.setBounds(m_chatWidth - 120, 10, 100, 25);
			m_closeButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			m_sendButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String inputContent = m_inputArea.getText();
					m_chatArea.append(GetChatRowKeepRight(inputContent) + "\n");
					m_inputArea.setText("");
				}
			});
			m_operationPanel.add(m_closeButton);
			m_operationPanel.add(m_sendButton);
			
			add(m_chatArea);
			add(m_inputArea);
			add(m_operationPanel);
			
			if(!m_userChatHistory.containsKey(m_chatTargeUser))
			{
				m_userChatHistory.put(m_chatTargeUser, new ArrayList<ChatHistory>());
				m_userChatHistory.get(m_chatTargeUser).add(new ChatHistory(m_chatTargeUser, "���,����"+m_chatTargeUser));
				m_userChatHistory.get(m_chatTargeUser).add(new ChatHistory(m_user, "���,����"+m_user));
			}
			for(ChatHistory chatHistory : m_userChatHistory.get(m_chatTargeUser)){
				String messageDisplay = chatHistory.m_message;
				if(m_user.equals(chatHistory.m_sender))
				{
					messageDisplay = GetChatRowKeepRight(messageDisplay);
				}
				m_chatArea.append(messageDisplay + "\n");
			}
		}
		//�Լ�����Ϣ���Ұݷ�
		private String GetChatRowKeepRight(String message)
		{
			int maxChatRowStrLen = m_chatArea.getSize().width * 2 / m_chatFontSize;
			String[] lines = message.split("\n");
			String messageDisplay = "";
			System.out.println(lines.length);
			for(int index = 0; index < lines.length; ++index)
			{
				String str = lines[index];
				System.out.println(str);
				for(int i = GetChineseStrLen(str); i < maxChatRowStrLen; ++i)
				{
					str = " " + str;
				}
				str += '\n';
				messageDisplay += str;
			}
			
			return messageDisplay;
		}
		//��ȡ�����ĵ��ַ���ʵ�ʳ���
		private int GetChineseStrLen(String str){
			int length = 0;  
	        for(int i = 0; i < str.length(); i++)  
	        {  
	            int ascii = Character.codePointAt(str, i);  
	            if(ascii >= 0 && ascii <=255)  
	                length++;  
	            else  
	                length += 2;  

	        }  
	        return length;
		}
	}
	
}
