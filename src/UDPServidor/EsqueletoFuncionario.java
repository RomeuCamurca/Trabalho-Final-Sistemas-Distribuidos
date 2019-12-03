package UDPServidor;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import proto.Dados.Atualizar;
import proto.Dados.Funcionario;
import proto.Dados.Funcionarios;

public class EsqueletoFuncionario {

	FuncionarioJDBCDAO funcionarios;

	public EsqueletoFuncionario() {
		funcionarios = new FuncionarioJDBCDAO();
	}

	public byte[] adicionar(ByteString msg) {
		Funcionario func = null;
		try {
			func = Funcionario.parseFrom(msg);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		String resposta = "";
		
		try {
			resposta = funcionarios.adicionar(func);
		} catch(DAOException e) {
			ByteString erro = ByteString.copyFromUtf8(e.getMessage());
			return erro.toByteArray();
		}
		ByteString res = ByteString.copyFromUtf8(resposta);
		return res.toByteArray();
	}

	public byte[] DeletarPorCpf(ByteString msg) {
		Funcionario fun = null;

		try {
			fun = Funcionario.parseFrom(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String resposta = "";
		try {
			resposta = funcionarios.DeletarPorCpf(fun.getCpf());
		} catch(DAOException e) {
			ByteString erro = ByteString.copyFromUtf8(e.getMessage());
			return erro.toByteArray();
		}
		
		ByteString res = ByteString.copyFromUtf8(resposta);
		return res.toByteArray();
	}

	public byte[] listarTodos(ByteString msg) throws InvalidProtocolBufferException {
		Funcionarios funcs = null;
		try {
			funcs = funcionarios.listarTodos();
		} catch(DAOException e) {
			ByteString erro = ByteString.copyFromUtf8(e.getMessage());
			return erro.toByteArray();
		}
		return funcs.toByteArray();
	}

	public byte[] listarPorCpf(ByteString msg) {
		Funcionario func = null;
		try {
			func = Funcionario.parseFrom(msg);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		Funcionario res = null;
		try {
			res = funcionarios.listarPorCpf(func.getCpf());
		} catch(DAOException e) {
			ByteString erro = ByteString.copyFromUtf8(e.getMessage());
			return erro.toByteArray();
		}
		
		return res.toByteArray();
	}

	public byte[] atualizar(ByteString msg) {
		Atualizar dados = null;
		try {
			dados = Atualizar.parseFrom(msg);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
		String resposta = "";
		
		try {
			resposta = funcionarios.atualizar(dados.getCpf(), dados.getFuncionario());
		} catch(DAOException e) {
			ByteString erro = ByteString.copyFromUtf8(e.getMessage());
			return erro.toByteArray();
		}
		
		ByteString res = ByteString.copyFromUtf8(resposta);
		return res.toByteArray();
	}

}
