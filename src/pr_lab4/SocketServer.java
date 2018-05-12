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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    
    //static ArrayList<String> commandsList=new ArrayList<>(Arrays.asList("/hello","/time","/flip","/random"));
    static String[] commandsList={"/hello","/day","/time","/random"};
    static String[] daysOfWeek={ "Monday","Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"};
    
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
        } else if(recievedCommand.equals("/help")){
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(convertListToString(commandsList));
        }else if(recievedCommand.equals("/day")){
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(getCurrentDay());
        }else if(recievedCommand.equals("/time")){
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(getCurrentTime());
        } else{
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("I don't know such a command");
        }
    }

    private static void sendNegativeResponse(ObjectOutputStream oos, Socket socket) throws IOException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject("The ' "+recievedCommand+" ' command is invalid");
    }

    private static String convertListToString(String[] commandsList) {
        String allCommands="\n";
        for(String command : commandsList){
            allCommands+=command+";\n";
        }
        return allCommands;
    }

    private static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK); 

        return daysOfWeek[day-1];
    }
    
    private static String getCurrentTime(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
    

  

   
    
}
