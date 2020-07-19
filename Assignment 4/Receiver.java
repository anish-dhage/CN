import java.net.*;
import java.io.*;

class Receive
{
	DataOutputStream dos;
	DataInputStream dis;
	boolean flag;
	void receive() throws Exception
	{
		Socket client=new Socket("127.0.0.1", 3000);
		dos=new DataOutputStream(client.getOutputStream());
		dis=new DataInputStream(client.getInputStream());
		int errindex =-1;
		int receivedata[];
		flag=true;

		int ack;

		int len=dis.readInt();
		receivedata=new int[len];
		System.out.println("The length of the window to be received is "+ len);
		int notrec=dis.readInt();


		for(int i=0;i<len;i++)
		{
			receivedata[i]=dis.readInt();
			if(receivedata[i]!=-1 && flag==true)
			{
			System.out.println(" Data "+receivedata[i] + " received at " + (i+1));
			System.out.println("Acknowledgement sent for " +(i+1));
//      System.out.print(" frame");
			ack=1;
			dos.writeInt(ack);
			}
			else if(receivedata[i]==-1)
			{
				System.out.println("Error receiving data at "+ (i+1));
				flag=false;
				errindex=i;
				ack=0;
				dos.writeInt(ack);
			}
			else if(flag==false)
			{
				if(errindex!=-1)
				{
					receivedata[i]=-1;
				System.out.println("Waiting for data at frame number "+(i+1));
				ack=2;
				dos.writeInt(ack);
				}
			}

			Thread.sleep(3000);
		}

		for(int i=0;i<len;i++)
		{
			System.out.println(receivedata[i]);

		}

		System.out.println("Requesting server to send data again");

		for(int i=errindex;i<len;i++)
		{
			int num=i+1;
			receivedata[i]=dis.readInt();
			System.out.println("Received data at frame number "+ (i+1));
      System.out.println("Acknowledgement sent for " +(i+1) );
  //    System.out.print(" frame");
			ack=1;
			dos.writeInt(ack);
			Thread.sleep(2000);
		}

		System.out.println("Finally received data");
		for(int i=0;i<len;i++)
		{
			System.out.println(receivedata[i]);

		}


		dis.close();
		dos.close();
		client.close();

	}

}

public class Receiver {


	public static void main (String args[]) throws Exception
	{
		Receive o1=new Receive();
		o1.receive();



	}

}
