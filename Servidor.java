import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;


public class Servidor implements Serializable
{
	public static void main(String[] args) throws IOException
	{
		int clienteCont = 0;
		// Criando o objeto do servidor que recebe a porta como parametro
		ServerSocket servidor = new ServerSocket(2525);
		System.out.println("Aguardando conexão... Porta 2525");

		while (true)
		{
			Socket socket = null;
			
			try
			{
				// Objeto da classe socket para o recebimento e saida de dados
				//servidor.accept aguarda outro clinte se conctar
				socket = servidor.accept();
				clienteCont++;

				System.out.println("Um novo cliente está conectado: " + socket);

				System.out.println("Clientes conectados: " + clienteCont);

				// Entrada e saida de dados
				DataInputStream entrada = new DataInputStream(socket.getInputStream());
				DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
				
				System.out.println("Atribuindo nova Thread para o Cliente");

				// Obejeto da thread criado
				Thread thread = new TratamentoDeCliente(socket, entrada, saida,clienteCont);

				// Iniciando a thread
				thread.start();
				
			}
			catch (Exception e){
				servidor.close();
				socket.close();
				e.printStackTrace();
			}
		}
	}
}

class TratamentoDeCliente extends Thread implements Serializable
{
	DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
	DateFormat hora = new SimpleDateFormat("hh:mm:ss");
	DataInputStream entrada;
	DataOutputStream saida;
	Socket socket;
	int clienteCont;
	

	// Construtor
	public TratamentoDeCliente(Socket socket, DataInputStream entrada, DataOutputStream saida, int clienteCont)
	{
		this.socket = socket;
		this.entrada = entrada;
		this.saida = saida;
		this.clienteCont = clienteCont;
	}	

	@Override
	public void run()
	{
		

		String recebido;
		String enviado;
		while (true)
		{
			try {

				// Envia para o cliente a seguinte mensagem
				saida.writeUTF("Digite o que você precisa [Data | Hora]..\n"+
							"Digite Sair para finalizar a conexão.");
				
				// recebe mensagem do cliente enviado pelo metodo WriteUTF
				recebido = entrada.readUTF();
				if(recebido.equals("Sair"))
				{
					

					System.out.println("Cliente " + clienteCont + " digitou Sair...");
					System.out.println("Fechando a Conexão.");
					this.socket.close();
					System.out.println("Conexão Fechada");
					break;
				}
				
				// Instancia da data
				Date dataObj = new Date();
				
				// Tratando dados envidado pelo cliente 
				switch (recebido) {
				
					case "Data" :
						enviado = data.format(dataObj);
						saida.writeUTF(enviado);
						break;
						
					case "Hora" :
						enviado = hora.format(dataObj);
						saida.writeUTF(enviado);
						break;
						
					default:
						System.out.println("Cliente "+ clienteCont + " disse: " + recebido);
						saida.writeUTF("Cliente "+ clienteCont + " disse: " + recebido);		
					
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			
			this.entrada.close();
			this.saida.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}	
}
