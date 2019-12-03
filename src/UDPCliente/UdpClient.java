package UDPCliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.naming.TimeLimitExceededException;

public class UdpClient {
	DatagramSocket aSocket = null;
	byte[] buffer = null;
	InetAddress aHost = null;
	int serverPort = 0;

	public UdpClient(String Ip, int Porta) {
		try {
			aSocket = new DatagramSocket();
			buffer = new byte[1024];
			aHost = InetAddress.getByName(Ip);
			serverPort = Porta;
		} catch (Exception e) {
		}

	}

	public void sendRequest(byte[] requisicao) {
		try {
			DatagramPacket request = new DatagramPacket(requisicao, requisicao.length, aHost, serverPort);
			aSocket.send(request);
			aSocket.setSoTimeout(3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getReplay() throws TimeLimitExceededException, IOException {
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		aSocket.receive(reply);
		return reply.getData();
	}

	public void finaliza() {
		try {
			aSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}