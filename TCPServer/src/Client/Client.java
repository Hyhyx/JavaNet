package Client;

public class Client {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientSocket clientSocket = new ClientSocket();
		LoginWindow clientWindow = new LoginWindow(clientSocket); 
	}

}
