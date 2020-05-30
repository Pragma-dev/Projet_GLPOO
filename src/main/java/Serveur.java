import java.io.*;
import java.net.*;

public class Serveur {

	private boolean isrunning = true;
	private ServerSocket ss;
	
	
	public void connect(String ip, int port) {
		try {
			ss = new ServerSocket (port,10,InetAddress.getByName(ip));
			System.out.println("Server waiting for connection...");
		} catch (IOException ioe) {
			System.out.println (ioe.getMessage());
		}
	}
	
	public void open() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				
				while (isrunning == true) {
					try {
						
						
						
						Socket socket = ss.accept(); //établit la connexion
						System.out.println("Connexion recu");
						OutputStream output = socket.getOutputStream();
						InputStream input = socket.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(input));
						PrintWriter writer = new PrintWriter(output, true);
						
						Thread reception = new Thread(new Runnable() {
							@Override
							public void run() {
								String text = null;
								while(true) {
									try {
									text = reader.readLine();
									}catch(IOException ioe) {System.out.println (ioe.getMessage());}
									//String reverseText = new StringBuilder(text).reverse().toString();
									writer.write("Server response: " + text/*reverseText*/+"\n");
									
									writer.flush();
								}
							}
							
						});
						reception.start();
						
					}catch(IOException ioe) {
						System.out.println (ioe.getMessage());
					}
				}
			}
		});	
		t.start();
	}
	
}