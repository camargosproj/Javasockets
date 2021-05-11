// Java implementation for a client
// Save file as Client.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente1 implements Serializable 
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			Scanner lerEntrada = new Scanner(System.in);
			
			// Usa localhost como ip padrão
			InetAddress ip = InetAddress.getByName("localhost");
	
			// Conexão com o servidor
			Socket socket = new Socket(ip, 2525);
	
			// Entrada e saida de dados
			DataInputStream entrada = new DataInputStream(socket.getInputStream());
			DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
	
			// loop para troca de informações entre o servidor e clinte, onde caso o cliente 
			// digite Sair a conexão será encerrada
			while (true)
			{
				System.out.println(entrada.readUTF());
				String enviado = lerEntrada.nextLine();
				saida.writeUTF(enviado);
				
				
				// se cliente digitar Sair a conexão será encerrada
				if(enviado.equals("Sair"))
				{
					System.out.println("Fechando Conexão : " + socket);
					socket.close();
					System.out.println("Conexão Fechada!");
					break;
				}
				
				// Recebendo o retorno do servidor com base no que foi digitado pelo cliente
				String recebido = entrada.readUTF();
				System.out.println(recebido);
			}
			
			lerEntrada.close();
			entrada.close();
			saida.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
