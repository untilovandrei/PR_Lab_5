/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr_lab4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author andrei
 */
public class SocketClient {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        socket = new Socket(host.getHostName(), 9876);
        Scanner sc=new Scanner(System.in);
        ois=new ObjectInputStream(socket.getInputStream());
        System.out.println(ois.readObject().toString());
        while(true){
            System.out.print("Input command : ");
            String command=sc.nextLine();
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(command);
            //if(command.equals("exit")) break;
            ois = new ObjectInputStream(socket.getInputStream());
            String response = (String) ois.readObject();
            System.out.println("Bot: " + response);
        }

    }
}
