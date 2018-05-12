/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr_lab4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author andrei
 */
public class SocketServer{
    
    private static ServerSocket server;
    private static int port = 9876;
    private static String recievedCommand;
    private static String recievedArgument;
    
    static ArrayList<String> commandsList=new ArrayList<>(Arrays.asList("/hello","/time","/flip","/random"));
    
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server = new ServerSocket(port);
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        Socket socket=null;
        socket = server.accept();
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject("Input '/help' command to get the list of supported commands.");
        
        while(true){            
            ois = new ObjectInputStream(socket.getInputStream());
            
            recievedCommand=null;
            recievedArgument=null;
            
            recievedCommand = ois.readObject().toString().trim();
            if(validateCommand(recievedCommand)){
                processCommand(oos,socket);
            } else {
                sendNegativeResponse(oos,socket);
            }
            
            
            
            
        }
//        ois.close();
//        oos.close();
//        socket.close();
//        server.close();
    }

    private static boolean validateCommand(String command) {
        if(command.indexOf("/")==-1){
            return false;
        }
        return true;
    }

    private static void processCommand(ObjectOutputStream oos, Socket socket) throws IOException {
        if(recievedCommand.indexOf(" ") != -1 && recievedCommand.contains("/hello")){
            recievedArgument = recievedCommand.substring(recievedCommand.indexOf(" ")+1);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Hello "+recievedArgument+". Can I help you ?");
        }
        if(recievedCommand.equals("/help")){
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(convertListToString(commandsList));
            
        }
    }

    private static void sendNegativeResponse(ObjectOutputStream oos, Socket socket) throws IOException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject("command = ' "+recievedCommand+" ' is not supported");
    }

    private static String convertListToString(ArrayList<String> commandsList) {
        String allCommands="\n";
        for(String command : commandsList){
            allCommands+=command+";\n";
        }
        return allCommands;
    }

  

   
    
}
