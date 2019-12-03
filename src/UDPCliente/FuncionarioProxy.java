package UDPCliente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.naming.TimeLimitExceededException;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import UDPServidor.DAOException;
import proto.Dados.*;
import proto.MensagemOuterClass.Mensagem;

public class FuncionarioProxy {
	int requestiId = 0;

	UdpClient udpclient = new UdpClient("127.0.0.1", 6000);

	public String adicionar(Funcionario mensagem) {
		try {
			return new String(doOperation("Funcionario", "adicionar", mensagem.toByteString()));
		} catch (RetransmissaoException e) {
			return e.getMessage();
		} catch (DAOException e) {
			return e.getMessage();
		}
	}

	public Funcionarios listarTodos() throws RetransmissaoException, DAOException{
		Funcionario.Builder fun = Funcionario.newBuilder();
		ByteString msg = fun.build().toByteString();

		Funcionarios funcionarios = null;

		try {
			byte[] response = doOperation("Funcionario", "listarTodos", msg);
			funcionarios = Funcionarios.parseFrom(response);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (RetransmissaoException e) {
			throw new RetransmissaoException(e.getMessage()); 
		} catch (DAOException e) {
			throw new DAOException(e.getMessage());
		}

		return funcionarios;
	}

	public Funcionario listarPorCpf(String cpf) throws RetransmissaoException, DAOException, InvalidProtocolBufferException{
		Funcionario.Builder fun = Funcionario.newBuilder();
		fun.setCpf(cpf);
		ByteString msg = fun.build().toByteString();
		Funcionario funcionario = null;
		
		try {
		byte[] response = doOperation("Funcionario", "listarPorCpf", msg);
		funcionario = Funcionario.parseFrom(response);
		} catch (InvalidProtocolBufferException e) {
			throw new InvalidProtocolBufferException("AVISO: CPF não encontrado!\n");
		} catch (DAOException e) {
			throw new DAOException(e.getMessage());
		}

		return funcionario;
	}

	public String atualizar(String cpfupdate, Funcionario func) {
		Atualizar.Builder atualizar = Atualizar.newBuilder();
		atualizar.setCpf(cpfupdate);
		atualizar.setFuncionario(func);
		atualizar.build();
		try {
			return new String(doOperation("Funcionario", "atualizar", atualizar.build().toByteString()));
		} catch (RetransmissaoException e) {
			return e.getMessage();
		} catch (DAOException e) {
			return e.getMessage();
		}
	}

	public String DeletarPorCpf(String cpf) {
		Funcionario.Builder f = Funcionario.newBuilder();
		f.setCpf(cpf);
		f.setNome("remove");
		f.setDatanascimento("remove");
		f.setIdade(0);
		f.setSexo("remove");
		f.setEndereco("remove");
		f.setSalario(0);

		ByteString msg = f.build().toByteString();

		byte[] response;
		try {
			response = doOperation("Funcionario", "DeletarPorCpf", msg);
		} catch (RetransmissaoException e) {
			return e.getMessage();
		} catch (DAOException e) {
			return e.getMessage();
		}

		return new String(response);
	}

	@SuppressWarnings("null")
	public byte[] doOperation(String objectRef, String method, ByteString args) throws RetransmissaoException{
		int cont = 0;
		Mensagem mensagem = null;
		byte[] func = empacotaMensagem(objectRef, method, args);
		udpclient.sendRequest(func);
		while (cont <= 3) {
			try {
				mensagem = desempacotaMensagem(udpclient.getReplay());
				return mensagem.getArguments().toByteArray();
			} catch (SocketTimeoutException | TimeLimitExceededException e) {
				if (cont != 3) {
					//System.out.println("Não recebemos resposta");
					udpclient.sendRequest(func);
					cont++;
				} else {
					throw new RetransmissaoException("Servidor indisponível!!!");
				}
			}catch (IOException e) {
				throw new DAOException("Não foi possível realizar está operação!!!");
			}
		}
		return mensagem.getArguments().toByteArray();
	}

	public void finaliza() {
		udpclient.finaliza();
	}

	private byte[] empacotaMensagem(String objectRef, String method, ByteString args) {
		ByteArrayOutputStream mensagem_em_bytes = new ByteArrayOutputStream(1024); // limite de tamnho da msg
		requestiId ++;
		Mensagem.Builder msg = Mensagem.newBuilder();
		msg.setType(0); // requisição
		msg.setId(requestiId); // identificador da mensagem // Tratamento de falhas
		msg.setObfReference(objectRef); // funcionario
		msg.setMethodId(method); // uma ação
		msg.setArguments(args); // parâmetros
		try {
			msg.build().writeDelimitedTo(mensagem_em_bytes); // serializando e delimitando o valor da msg
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mensagem_em_bytes.toByteArray(); // retorno da msg serializada em byte
	}

	private Mensagem desempacotaMensagem(byte[] resposta) {
		Mensagem msgRecebida = null;
		try {

			msgRecebida = Mensagem.parseDelimitedFrom(new ByteArrayInputStream(resposta));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgRecebida;
	}
}