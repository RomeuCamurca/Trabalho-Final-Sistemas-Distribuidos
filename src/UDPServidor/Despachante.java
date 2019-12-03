package UDPServidor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.google.protobuf.ByteString;

import proto.Dados.Funcionario;
import proto.MensagemOuterClass.Mensagem;

@SuppressWarnings("unused")
public class Despachante {
	public byte[] selecionaEsqueleto(Mensagem request) {

		Class<?> objRef = null;
		Method method = null;
		byte[] resposta = null;
		try {
			objRef = Class.forName("UDPServidor.Esqueleto" + request.getObfReference());
			String methodName = request.getMethodId();
			method = objRef.getMethod(methodName, ByteString.class);
			resposta = (byte[]) (method.invoke(objRef.newInstance(), request.getArguments()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			ByteString erro = ByteString.copyFromUtf8("Erro");
			return erro.toByteArray();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return resposta;
	}	
}