package TCP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientWindow extends JFrame {
	public JPanel m_registerPanel = new JPanel();  
	public JPanel m_loginPanel = new JPanel();   

	public static void main(String[] args) {
		// ���� JFrame ʵ��
        JFrame frame = new JFrame("Login Example");
        // Setting the width and height of frame
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* ������壬��������� HTML �� div ��ǩ
         * ���ǿ��Դ��������岢�� JFrame ��ָ��λ��
         * ��������ǿ�������ı��ֶΣ���ť�����������
         */
        //JPanel panel = new JPanel();    
        // ������
        ClientWindow clientWindow = new ClientWindow(); 
        frame.add(clientWindow.m_loginPanel);
        /* 
         * �����û�����ķ����������������
         */
        
        clientWindow.InitLoginPanel();

        // ���ý���ɼ�
        frame.setVisible(true);
	}
	
	private class RegisterActionListener implements ActionListener{  
        public void actionPerformed(ActionEvent e) {  
            System.out.println("ע��");     
            m_loginPanel.setVisible(false);
        }     
    }  
      
    private class LoginActionListener implements ActionListener{  
        public void actionPerformed(ActionEvent e) {  
            System.out.println("��½");     
        }     
    }  
	
	private  void InitLoginPanel() {

        /* ���ֲ���������߲���������
         * ������ò���Ϊ null
         */
        m_loginPanel.setLayout(null);

        // ���� JLabel
        JLabel userLabel = new JLabel("User:");
        /* ������������������λ�á�
         * setBounds(x, y, width, height)
         * x �� y ָ�����Ͻǵ���λ�ã��� width �� height ָ���µĴ�С��
         */
        userLabel.setBounds(10,20,80,25);
        m_loginPanel.add(userLabel);

        /* 
         * �����ı��������û�����
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        m_loginPanel.add(userText);

        // ����������ı���
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10,50,80,25);
        m_loginPanel.add(passwordLabel);

        /* 
         *�����������������ı���
         * �����������Ϣ���Ե�Ŵ��棬���ڰ�������İ�ȫ��
         */
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        m_loginPanel.add(passwordText);

        // ������¼��ť
        JButton loginButton = new JButton("login");
        loginButton.setBounds(200, 80, 80, 25);
        loginButton.addActionListener(new LoginActionListener());
        m_loginPanel.add(loginButton);
        
        // ����ע�ᰴť
        JButton registerButton = new JButton("register");
        registerButton.setBounds(10, 80, 80, 25);
        registerButton.addActionListener(new RegisterActionListener());
        m_loginPanel.add(registerButton);
    }

}
