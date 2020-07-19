import java.util.*;
import java.net.*;
import java.io.*;


class Sender
{
	Scanner sc=new Scanner(System.in);
	int n,recwindowsize;
	int windowsize;
	DataOutputStream out;
	DataInputStream in;

	int sentdata[];
	int sentdatabckp[];
	Sender()
	{

		recwindowsize=1;
		System.out.println("Enter the number of bits in the input frames: ");
		n=sc.nextInt();
		windowsize= (int)Math.pow(2,n);
		windowsize=windowsize-1;
		sentdata=new int [windowsize];
		sentdatabckp=new int [windowsize];

		for(int i=0;i<windowsize;i++)
		{
			System.out.println("Enter the integers(bit frame) ");
			sentdata[i]=sc.nextInt();
			sentdatabckp[i]=sentdata[i];
		}
	}
	void sending() throws Exception
	{
		try
		{
		ServerSocket serv=new ServerSocket(3000);
		System.out.println("Server Ready... Waiting for connection...");
		Socket sock=serv.accept();

		System.out.println("Connection established!");
		out=new DataOutputStream(sock.getOutputStream());
		in=new DataInputStream(sock.getInputStream());
		System.out.println("The total number of packets to be sent are "+ sentdata.length);
		int len=sentdata.length;
		out.writeInt(len);

		Random rand=new Random();
		int notrec=rand.nextInt(windowsize);

		sentdata[notrec]=-1;

		out.writeInt(notrec);

		for(int i=0;i<windowsize;i++)
		{
			out.writeInt(sentdata[i]);
			int ack=in.readInt();
			if(ack==1)
			{
				System.out.println("Data sent/receive successful for" + i );
			}
			else if(ack==0)
			{
				System.out.println("Data not received successfully for " + i);
			}
			else if(ack==2)
			{
				System.out.println("Waiting for data at " + i);
			}
			Thread.sleep(3000);
		}

		System.out.println("Client requested to send again");

		for(int i=notrec;i<windowsize;i++)
		{
			out.writeInt(sentdatabckp[i]);
			int ack=in.readInt();
      if(ack==1)
			{
				System.out.println("Data sent/receive successful for" + i );
			}
			else if(ack==0)
			{
				System.out.println("Data not received successfully for " + i);
			}
			else if(ack==2)
			{
				System.out.println("Waiting for data at " + i);
			}
			Thread.sleep(2000);
		}




		out.flush();

		sock.close();
		serv.close();



		}
		catch(IOException ex)
		{
			System.out.println(ex);
		}

	}


}


public class Go_back {

	public static void main(String args[]) throws Exception
	{
		Sender o1=new Sender();
		o1.sending();


	}

}
