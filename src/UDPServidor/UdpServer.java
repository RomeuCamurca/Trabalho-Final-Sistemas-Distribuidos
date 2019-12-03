package UDPServidor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.google.protobuf.ByteString;

import proto.MensagemOuterClass.Mensagem;

public class UdpServer {
	static DatagramSocket aSocket = null;
	static byte[] buffer;
	static Mensagem requisicao;
	static DatagramPacket reply;
	static DatagramPacket request;
	static int cont = 0;
	static int newId = 0;
	static int oldId = 0;
	static byte[] pacoteRep = null;
	static byte[] resultado = null;
	static InetAddress ip;
	static int porta;
	static Despachante despachante = new Despachante();
	
	public static void main(String[] args) {
		
		try {
			aSocket = new DatagramSocket(6000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		buffer = new byte[1024];
		while (true) {

			requisicao = desempacotaRequisicao(getRequest());
			if(duplicada()==false) {
				resultado = despachante.selecionaEsqueleto(requisicao);
				pacoteRep = empacotaResposta(resultado, requisicao.getId());
				sendReply(pacoteRep);
				ip = request.getAddress();
				porta = request.getPort();
			}
		}
	}
	
	public static boolean duplicada() {
			newId = requisicao.getId();
			InetAddress IP = request.getAddress();
			if (newId == oldId  && ip.equals(IP) && porta == request.getPort()) {
				System.out.println("ouve repetição");
				sendReply(pacoteRep);
			} else {
				oldId = newId;
				return false;
			}
		return true;
	}

	public static Mensagem desempacotaRequisicao(byte[] requisicao) {
		Mensagem msgRecebida = null;
		ByteArrayInputStream entrada = new ByteArrayInputStream(requisicao);
		try {

			msgRecebida = Mensagem.parseDelimitedFrom(entrada);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgRecebida;
	}

	public static byte[] empacotaResposta(byte[] msg, int requestID) {
		ByteArrayOutputStream mensagem = new ByteArrayOutputStream(1024);
		try {
			Mensagem.newBuilder().setType(1).setArguments(ByteString.copyFrom(msg)).setId(requestID).build()
					.writeDelimitedTo(mensagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mensagem.toByteArray();
	}

	public static byte[] getRequest() {
		request = new DatagramPacket(buffer, buffer.length);
		try {
			aSocket.receive(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;

	}

	public static void sendReply(byte[] pacote) {
		reply = new DatagramPacket(pacote, pacote.length, request.getAddress(), request.getPort());
		cont += 1;
		if (cont != 4) {
			try {
				aSocket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			cont = 0;
		}
	}

}