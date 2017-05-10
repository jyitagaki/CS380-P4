//Joshua Itagaki
//CS 380

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Ipv6Client {
	public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("codebank.xyz", 38004)){
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();	
			byte[] packet;
			short totalL;
			int dataL;
			for(int i = 0; i < 12; i++){
				dataL = (int)(Math.pow(2, i+1));
				totalL = (short)(dataL + 40);
				packet = new byte[totalL];
				packet[0] = 0b01100000;	//version 6 and traffic flow not implemented
				
				packet[1] = 0;	//flow label not implemented
				packet[2] = 0;
				packet[3] = 0;
				
				packet[4] = (byte)((dataL >> 8) & 0xFF);	//Payload first byte
				packet[5] = (byte)(dataL & 0xFF);	//Payload second byte
				
				packet[6] = (byte)17;	//next header UDP
				
				packet[7] = (byte)20;	//hop limit set 20
				
				for(int j = 8; j < 18; j++){	//packets 8 - 17
					packet[j] = 0;	//src address
				}
				packet[18] = (byte)0xFF;	
				packet[19] = (byte)0xFF;	
				packet[20] = 127;			
				packet[21] = 0;				
				packet[22] = 0;				
				packet[23] = 1;				
				
				for(int k = 24; k < 34; k++){	//packets 24 - 33
					packet[k] = 0;	//dest address
				}
				packet[34] = (byte)0xFF;	
				packet[35] = (byte)0xFF;	
				byte[] destination = socket.getInetAddress().getAddress(); //IP address of server
				for(int j = 0; j <= 3; j++){	//packets 36 - 39
					packet[j + 36] = destination[j];
				}			
				System.out.println("Data length: " + dataL);
				out.write(packet);
				System.out.print("Response: 0x");
				for(int j = 0; j < 4; j++){
					System.out.printf("%02X", in.read());
				}
			}
		}
	}
}
