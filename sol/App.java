package sol;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {  
    

    public static void main(String[] args) throws FileNotFoundException, IOException{
        Services services;
        String localFile;

        readConfig(args, services);
       
        Scanner reader = new Scanner(System.in);


        System.out.println("#> ");
        String command = reader.next();
        while(!command.equals("exit")){
            switch(command){
                case "poll":
                poll(services, reader);

                break;
            case "fetch":
                fetch(services, reader);
                break;
            case "history":
                history(services, reader);
                break;

            case "backup":
                backup(services, reader);
            break;

            case "restore":
                restore(reader, services);
                break;

            case "services":
                services(services, reader);
                break;

            case "help":
                help();
                 break;

            /*case "status":
                status(services, reader);
                break;*/

            default:
                System.out.println("Invalid Command");
                break;
            }
            System.out.println("#> ");
            command = reader.next();
        }

        reader.close();
    } 
  

    
    public static String[] tokenize(Scanner reader){
        String[] tokens = null;
        while (reader.hasNextLine()){
             tokens = reader.nextLine().split("\\s|=");
        }
        return tokens;
    }

    public static void readConfig(String[] args, Services services) throws FileNotFoundException, IOException{
        String configFile = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String line;
        String[] fields;

        line = reader.readLine();
        while(line != null){
            fields = line.split("\\|");
            services._services.add(new Service(fields[0], fields[1]));
        }
        reader.close();
    }

   public static void poll(Services services, Scanner reader){
        String[] options = tokenize(reader);

        ArrayList<String> included = new ArrayList<>();
        ArrayList<String> excluded = new ArrayList<>();

        for (int i = 0; i < options.length; i++){
            if(options[i].equals("--only")){
                i++;
                String[] webSites = options[i].split(",");
                for (String webSite : webSites){
                    included.add(webSite);
                }
            } else if (options[i].equals("--exclude")){
                i++;
                String[] webSites = options[i].split(",");
                for (String webSite: webSites){
                    excluded.add(webSite);
                }
            }
        }

        services.poll(included, excluded);


    }

    public static void restore(Scanner reader, Services services){
       
        String[] options = tokenize(reader);

        boolean merge = false;

        if (options == null){
            System.out.println("Please specify a file");
            return;
        } else if (options.length == 2){
            for (String s : options){
                if (s.equals("--merge")){
                    merge = true;
                    break;
                }
            }
        } 

        String dataFile = options[options.length-1];

        try {
            open(dataFile, services, merge);
        } catch (InvalidOperationException | IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void open(String dataFile, Services services, boolean merge) throws InvalidOperationException, IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile));
        Services newServices;

        newServices = (Services) in.readObject();

        in.close();
        if (merge){

        } else {
            services = newServices;
        }
    }

    public static void backup(Services services, Scanner reader) throws IOException{
        boolean txt = false;
        boolean csv = false;

        String[] options = tokenize(reader);
        String dataFile;

        if (options == null){
            System.out.println("Please specify a file");
            return;
        } else if (options.length >= 3) {
            for (String s : options){
                if (s.equals("txt")){
                    txt = true;
                
                } else if (s.equals("csv")){
                    csv = true;
                }
            }
        }
        
        dataFile = options[options.length -1];
        if(txt){

        } else if(csv) {
            
        }else {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile));
            out.writeObject(services);
            out.close();
        }

    }


    public static void help(){
        System.out.println("command [args]");
        System.out.println("command: ");
        System.out.println("poll - Retrieves the status from of all configured services");
        System.out.println("fetch - Retrieves the status from of all configured services");
        System.out.println("services - Lists all known services");
        System.out.println("backup - backups the current internal state to a file");
        System.out.println("restore - Imports the internal state from another run or app");
        System.out.println("history - Outputs all the data from the local storage");
        System.out.println("status - Summarizes data and displays it in a table-like fashion");
        System.out.println("help - This screen");
        System.out.println("exit - terminates application");


    }


}