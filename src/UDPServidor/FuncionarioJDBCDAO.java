package UDPServidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import proto.Dados.Atualizar;
import proto.Dados.Funcionario;
import proto.Dados.Funcionarios;

public class FuncionarioJDBCDAO {

	public FuncionarioJDBCDAO() {
		super();
	}
	
	//adiciona funcionario
	public String adicionar(Funcionario funcionario) {
		Connection con = null;
		String salvou = "[+] Funcionário Não Adicionado Com Sucesso.\n";
		
		try {
			con = ConnectionFactory.getConnection();
			String insert_sql = "insert into Funcionario (nome, cpf, datanascimento, idade, sexo, endereco, salario) values (?,?,?,?,?,?,?)";
			
			PreparedStatement pst;
			pst = con.prepareStatement(insert_sql);
			pst.setString(1, funcionario.getNome());
			pst.setString(2, funcionario.getCpf());
			pst.setString(3, funcionario.getDatanascimento());
			pst.setInt(4, funcionario.getIdade());
			pst.setString(5, funcionario.getSexo());
			pst.setString(6, funcionario.getEndereco());
			pst.setFloat(7, funcionario.getSalario());
			if (pst.executeUpdate()>0) {
				salvou = "[+] Funcionário Adicionado Com Sucesso\n";
			}
		}catch (SQLException e) {
				throw new DAOException ("Operação não realizada com sucesso.");
		}finally {
			try {
				if (con != null) {
					con.close();
				}
			}catch (SQLException e) {
				throw new DAOException ("Não foi possível fechar a conexão.");
			}
		}
		return salvou;
	}

	//remove funcionario por cpf
	 public String DeletarPorCpf(String cpf) {
		Connection con = null;
		String apagou = "[+] Erro ao Remover o Funcionário.\n";
		try {
		con = ConnectionFactory.getConnection();
		String sql = "delete from funcionario where cpf=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, cpf);
		if(pst.executeUpdate() > 0) {
			apagou = "[+] Funcionário Removido Com Sucesso.\n";
		}
		pst.close();
		
		} catch (SQLException e) {
			throw new DAOException ("Operação não realizada com sucesso.");
		}finally {
			try {
				if (con != null)
					con.close();
			}catch (SQLException e){
				throw new DAOException("Nao foi possivel fechar a conexao.");
			}
		}
		return apagou;
	 }
	 

	//lista todos os funcionarios
	public Funcionarios listarTodos() {
		Connection con = null;
		Funcionarios.Builder funcs = Funcionarios.newBuilder();
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select * from funcionario";
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				String nome = rs.getString("nome");
				String cpf = rs.getString("cpf");
				String data = rs.getString("datanascimento");
				int idade = rs.getInt("idade");
				String sexo = rs.getString("sexo");
				String endereco = rs.getString("endereco");
				float salario = rs.getFloat("salario");
				
				Funcionario.Builder novo = Funcionario.newBuilder();
				novo.setNome(nome);
				novo.setCpf(cpf);
				novo.setDatanascimento(data);
				novo.setIdade(idade);
				novo.setSexo(sexo);
				novo.setEndereco(endereco);
				novo.setSalario(salario);
		
				funcs.addFuncionario(novo.build());
			}
		}catch (SQLException e) {
			throw new DAOException ("Operação não realizada com sucesso.");
		}finally {
			try {
				if (con != null)
					con.close();
			}catch (SQLException e){
				throw new DAOException("Nao foi possivel fechar a conexao.");
			}
		}
		return funcs.build();
		
	}

	//lista funcionario por cpf
	
	public Funcionario listarPorCpf(String cpf) {
		Connection con = null;
		Funcionario.Builder fun = Funcionario.newBuilder();
		try {
			con=ConnectionFactory.getConnection();
			String sql ="select * from funcionario where cpf=?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, cpf);
			ResultSet rs =pst.executeQuery();
			if(rs.next()) {
				String nome = rs.getString("nome");
				fun.setCpf(cpf);
				String data = rs.getString("datanascimento");
				int idade = rs.getInt("idade");
				String sexo = rs.getString("sexo");
				String endereco = rs.getString("endereco");
				float salario = rs.getFloat("salario");
				
				Funcionario.Builder novo = Funcionario.newBuilder();
				novo.setNome(nome);
				novo.setCpf(cpf);
				novo.setDatanascimento(data);
				novo.setIdade(idade);
				novo.setSexo(sexo);
				novo.setEndereco(endereco);
				novo.setSalario(salario);
				
				//pst.close();

				return novo.build();
			}
		
		}catch (SQLException e) {
			throw new DAOException("Operação não realizada com sucesso");
		}finally {
			try {
				if (con != null)
					con.close();
			}catch (SQLException e){
				throw new DAOException("Nao foi possivel fechar a conexao.");
			}
		}
		return null;
		
	}

	//atualiza funcionario por cpf
	public String atualizar(String cpf, Funcionario f) {
		Connection con = null;
		String salvou = "[+] Funcionário Não Atualizado\n";
		
		try {
			con = ConnectionFactory.getConnection();
			String update_sql = "update funcionario set nome=?, cpf=?, datanascimento=?, idade=?, sexo=?, endereco=?, salario=? where cpf = ?";
			
			PreparedStatement pst;
			pst = con.prepareStatement(update_sql);
			pst.setString(1, f.getNome());
			pst.setString(2, f.getCpf());
			pst.setString(3, f.getDatanascimento());
			pst.setInt(4, f.getIdade());
			pst.setString(5, f.getSexo());
			pst.setString(6, f.getEndereco());
			pst.setFloat(7, f.getSalario());
			pst.setString(8, cpf);
			if (pst.executeUpdate()>0) {
				salvou = "[+] Funcionário Atualizado Com Sucesso.";
			}
		}catch (SQLException e) {
				throw new DAOException ("Operação não realizada com sucesso.");
		}finally {
			try {
				if (con != null) {
					con.close();
				}
			}catch (SQLException e) {
				throw new DAOException ("Não foi possível fechar a conexão.");
			}
				
		}
		return salvou;
		
	}
}