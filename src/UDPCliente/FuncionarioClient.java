package UDPCliente;

import java.io.IOException;
import java.util.Scanner;

import com.google.protobuf.InvalidProtocolBufferException;

import proto.Dados.*;
import Tratamento.ValidaCPF;
import Tratamento.ValidaData;
import UDPServidor.DAOException;

public class FuncionarioClient {

	public static Scanner entrada = new Scanner(System.in);
	ValidaData validaData = new ValidaData();

	FuncionarioProxy proxy;
	Funcionario.Builder funcionario = Funcionario.newBuilder();
	Consulta.Builder consulta = Consulta.newBuilder();

	public FuncionarioClient() {
		proxy = new FuncionarioProxy();
	}

	public int selecionaOperacao() throws IOException {
		int opcao = 0;
		String r = "";
		opcao = Integer.parseInt(entrada.nextLine());

		switch (opcao) {
		case 1:
				System.out.println("Nome: ");
				funcionario.setNome(entrada.nextLine());
				System.out.println("CPF: (Apenas Números!)");
				String CPF = entrada.nextLine();
				if (ValidaCPF.isCPF(CPF) == true) {
					String CpfValido = ValidaCPF.imprimeCPF(CPF);
					funcionario.setCpf(CpfValido);
				}
				else {
					System.out.printf("AVISO: Digite um CPF Válido!\n\n");
					break;
				}
				
				System.out.println("Data de Nascimento: (Dia-Mês-Ano)");
				String data = entrada.nextLine();
				if (validaData.data(data) == true) {
					funcionario.setDatanascimento(data);
				}
				else {
					System.out.println("AVISO: Digite Uma Data Válida!\n");
					break;
				}
				try {
					System.out.println("Idade: ");
					funcionario.setIdade(Integer.parseInt(entrada.nextLine()));
				} catch (NumberFormatException e) {
					System.out.println("AVISO: Digite um número inteiro!\n");
					break;
				}
				System.out.println("Sexo: (M/F)");
				String sexo = entrada.nextLine();
				String sexoUpper = sexo.toUpperCase();
				if (sexoUpper.equals("M") || sexoUpper.equals("F") ) {
					funcionario.setSexo(sexoUpper);
				}
				else {
					System.out.println("AVISO: Digite (M) Para Sexo Masculino ou (F) Para Sexo Feminino.\n");
					break;
				}
				System.out.println("Endereço: ");
				funcionario.setEndereco(entrada.nextLine());
				try {
					System.out.println("Salário: ");
					funcionario.setSalario(Float.parseFloat(entrada.nextLine()));
				} catch (NumberFormatException e) {
					System.out.println("AVISO:  Digite um tipo númerico!\n");
					break;
				}

				r = proxy.adicionar(funcionario.build());
					System.out.println("Resultado: " + r);
			break;
			
		case 2:
			Funcionarios funcionarios;
			try {
				funcionarios = proxy.listarTodos();
				for (Funcionario fun : funcionarios.getFuncionarioList()) {
					System.out.println("Nome: " + fun.getNome());
					System.out.println("CPF: " + fun.getCpf());
					System.out.println("Data de Nascimento: " + fun.getDatanascimento());
					System.out.println("Idade: " + fun.getIdade());
					System.out.println("Sexo: " + fun.getSexo());
					System.out.println("Endereço: " + fun.getEndereco());
					System.out.println("Salário: " + fun.getSalario());
					System.out.println("\n********************************\n");
				}
			} catch (RetransmissaoException e) {
				System.out.println(e.getMessage());
			}
			break;

		case 3:
			Funcionarios funsListarPorCpf;
			try {
				funsListarPorCpf = proxy.listarTodos();
				System.out.println("[+] Lista De Funcionários Cadastrados:");

				for (Funcionario funcionario : funsListarPorCpf.getFuncionarioList()) {
					System.out.println("CPF:" + funcionario.getCpf() + " -- " + "Nome:" + funcionario.getNome());
				}
				System.out.println();
			} catch (DAOException e1) {
				e1.printStackTrace();
			} catch (RetransmissaoException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("Digite o CPF do Funcionário Que Deseja Buscar: (Apenas Números!)");
			String buscaCPF = entrada.nextLine();
			if (ValidaCPF.isCPF(buscaCPF) == true) {
				String CpfValido = ValidaCPF.imprimeCPF(buscaCPF);
				try {
					Funcionario func = proxy.listarPorCpf(CpfValido);
					System.out.println("Nome: " + func.getNome());
					System.out.println("CPF: " + func.getCpf());
					System.out.println("Data de Nascimento: " + func.getDatanascimento());
					System.out.println("Idade: " + func.getIdade());
					System.out.println("Sexo: " + func.getSexo());
					System.out.println("Endereço: " + func.getEndereco());
					System.out.println("Salário: " + func.getSalario());
					System.out.println("\n********************************\n");
				} catch (RetransmissaoException e) {
					System.out.println(e.getMessage());
					break;
				} catch (InvalidProtocolBufferException e) {
					System.out.println(e.getMessage());
				}
			}
			else {
				System.out.printf("AVISO: Digite um CPF Válido!\n\n");
				break;
			}
			break;

		case 4:
			System.out.println("Digite o CPF do Respectivo Funcionário: ");
			String atualizaCPF = entrada.nextLine();
			if (ValidaCPF.isCPF(atualizaCPF) == true) {
				String CpfValido = ValidaCPF.imprimeCPF(atualizaCPF);
				System.out.println("[+] Vamos Atualizar o funcionário com o CPF: " + CpfValido + "\n");
				System.out.println("Nome: ");
				funcionario.setNome(entrada.nextLine());
				System.out.println("CPF: (Apenas Números!)");
				String CpfAtualiza = entrada.nextLine();
				if (ValidaCPF.isCPF(CpfAtualiza) == true) {
					String CPFValido = ValidaCPF.imprimeCPF(CpfAtualiza);
					funcionario.setCpf(CPFValido);
				}
				else {
					System.out.printf("AVISO: Digite um CPF Válido!\n\n");
					break;
				}
				
				System.out.println("Data de Nascimento: (Dia-Mês-Ano)");
				String dataAtualiza = entrada.nextLine();
				if (validaData.data(dataAtualiza) == true) {
					funcionario.setDatanascimento(dataAtualiza);
				}
				else {
					System.out.println("AVISO: Digite Uma Data Válida!\n");
					break;
				}
				try {
					System.out.println("Idade: ");
					funcionario.setIdade(Integer.parseInt(entrada.nextLine()));
				} catch (NumberFormatException e) {
					System.out.println("AVISO: Digite um número inteiro!\n");
					break;
				}
				System.out.println("Sexo: (M/F)");
				String sexoAtualiza = entrada.nextLine();
				String sexoUpperAtualiza = sexoAtualiza.toUpperCase();
				if (sexoUpperAtualiza.equals("M") || sexoUpperAtualiza.equals("F") ) {
					funcionario.setSexo(sexoUpperAtualiza);
				}
				else {
					System.out.println("AVISO: Digite (M) Para Sexo Masculino ou (F) Para Sexo Feminino.\n");
					break;
				}
				System.out.println("Endereço: ");
				funcionario.setEndereco(entrada.nextLine());
				try {
					System.out.println("Salário: ");
					funcionario.setSalario(Float.parseFloat(entrada.nextLine()));
				} catch (NumberFormatException e) {
					System.out.println("AVISO: Digite um tipo númerico!\n");
					break;
				}

				r = proxy.atualizar(CpfValido, funcionario.build());
				System.out.println("Resultado: " + r);
			}
			else {
				System.out.printf("AVISO: Digite um CPF Válido!\n\n");
				break;
			}
			break;

		case 5:
			Funcionarios funs;
			try {
				funs = proxy.listarTodos();
				System.out.println("[+] Lista De Funcionários Cadastrados:");

				for (Funcionario funcionario : funs.getFuncionarioList()) {
					System.out.println("CPF:" + funcionario.getCpf() + " -- " + "Nome:" + funcionario.getNome());
				}

				System.out.println("\n[+] Digite o CPF do Funcionário Que Será Deletado: (Apenas Números!)");
				String removecpf = entrada.nextLine();
				if (ValidaCPF.isCPF(removecpf) == true) {
					String CPFValidoRemove = ValidaCPF.imprimeCPF(removecpf);
					
					boolean cpfErrado = true;
					for (Funcionario funcionario : funs.getFuncionarioList()) {
						if (CPFValidoRemove.equals(funcionario.getCpf())) {
							cpfErrado = false;
						}
					}
					
					if (cpfErrado) {
						System.out.println("[+] O CPF Que Você Passou Não Existe\n");
					} else {
						System.out.println(proxy.DeletarPorCpf(CPFValidoRemove));
					}
				}
				else {
					System.out.printf("AVISO: Digite um CPF Válido!\n\n");
					break;
				}
				
			} catch (RetransmissaoException e) {
				System.out.println(e.getMessage());
			}
			break;
			
		case 0:
			System.out.println("Conexão finalizada!!!");
			proxy.finaliza();
			break;

		default:
			System.out.println("Opção Inválida!\n");
			break;

		}
		return opcao;
	}

	public void printMenu() {
		System.out.println("ESCOLHA UMA OPÇÃO:\n" +
						   "1 - Adicionar Funcionário\n" +
						   "2 - Listar Todos os Funcionários\n" +
						   "3 - Listar Funcionário Pelo CPF\n" +
						   "4 - Atualizar Funcionário Pelo CPF\n" +
						   "5 - Deletar Funcionário Pelo CPF\n" +
						   "0 - Sair");
	}

	public static void main(String[] args) {
		FuncionarioClient funcionarioClient = new FuncionarioClient();
		int operacao = -1;

		do {
			funcionarioClient.printMenu();
			try {
				operacao = funcionarioClient.selecionaOperacao();
			} catch (NumberFormatException | IOException e) {
				System.out.println("AVISO: Digite uma das opções abaixo!\n");
			}
			
		} while (operacao != 0);
	}
}