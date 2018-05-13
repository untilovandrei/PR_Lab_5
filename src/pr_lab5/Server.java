/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr_lab5;

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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.util.Pair;

/**
 *
 * @author andrei
 */
public class Server{
    
    private static ServerSocket server;
    private static int port = 9876;
    private static String recievedCommand;
    private static String recievedArgument;
    
    static String[] commandsList={"/hello","/day","/time","/random","/exit"};
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
                if(recievedCommand.equals("/exit")){
                    break;
                }
                processCommand(oos,socket);
            } else {
                sendNegativeResponse(oos,socket);
            }
            
            
            
            
        }
        ois.close();
        oos.close();
        socket.close();
        server.close();
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
            
        }else if(recievedCommand.indexOf(" ") != -1 && recievedCommand.contains("/random")){
            
            recievedArgument = recievedCommand.substring(recievedCommand.indexOf(" ")+1);
            int integer=getRandomNumber(Integer.valueOf(recievedArgument));
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(integer);
            oos.writeObject(String.valueOf(integer));
            
        }else{
            
            String response="I don't know such a command";
            String suggestion=tryToGetSuggestion();
            if(suggestion!=null){
                response=suggestion;
            }
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(response);
                
            
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
    
    private static int getRandomNumber(int max){
        return new Random().nextInt(max + 1);
    }
    
    
    static int calculate(String x, String y) {
    int[][] dp = new int[x.length() + 1][y.length() + 1];
 
    for (int i = 0; i <= x.length(); i++) {
        for (int j = 0; j <= y.length(); j++) {
            if (i == 0) {
                dp[i][j] = j;
            }
            else if (j == 0) {
                dp[i][j] = i;
            }
            else {
                dp[i][j] = min(dp[i - 1][j - 1] 
                 + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                  dp[i - 1][j] + 1, 
                  dp[i][j - 1] + 1);
            }
        }
    }
 
    return dp[x.length()][y.length()];
    }
    
    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
    
    public static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
    
    public static Pair<Integer,Integer> getMinElement(int[] numbers){
        int minValue = numbers[0];
        int index = 0;
        for(int i=1;i<numbers.length;i++){
          if(numbers[i] < minValue){
                minValue = numbers[i];
                index=i;
              }
        }
    return new Pair(index,minValue);
}
    
    private static String tryToGetSuggestion(){
        int[] distances=new int[commandsList.length];
            for(int i = 0 ; i < distances.length ; i++){
                distances[i]=calculate(recievedCommand, commandsList[i]);
            }
            
            int minimumDistance=getMinElement(distances).getValue();
            String suggestion=commandsList[getMinElement(distances).getKey()];
            if(minimumDistance<3){
                return "Did you mean ' "+suggestion+" ' command ?";
            }
        return null;
    }

  

   
    
}
