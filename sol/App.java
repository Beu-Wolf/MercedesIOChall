
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;



public class App {  
    

    public static void main(String[] args){
        Services services = new Services();

        try {
            readConfig(args, services);
        } catch( IOException e) {
            e.printStackTrace();
        }
       
        Scanner reader = new Scanner(System.in);


        System.out.print("#> ");
        String command = reader.next();
        while(!command.equals("exit")){
            switch(command){
            case "poll":
                String[] options = tokenize(reader);
                poll(services, options);    
                break;
            
            case "fetch":
                fetch(services, reader);
                break;
            
            case "history":
                history(services, reader);
                break;

            case "backup":
                try {
                    backup(services, reader);
                } catch (IOException e){
                    e.printStackTrace();
                }
            break;

            case "restore":
                services = restore(services, reader);
                break;

            case "services":
                services(services);
                break; 

            case "help":
                help();
                 break;

            case "status":
                status(services);
                break;

            default:
                System.out.println("Invalid Command");
                break;
            }
            System.out.print("#> ");
            command = reader.next();
        }

        reader.close();
    } 
  

    
    public static String[] tokenize(Scanner reader){
        /*separate input by tokens for process*/ 
        String[] tokens = null;
        if (reader.hasNextLine()){
            String token = reader.nextLine();   
            if (!token.equals(""))
                tokens = token.trim().split("\\s|=");

        }   


        return tokens;
    }

    public static void readConfig(String[] args, Services services) throws IOException{
        /*reads services from config file and initiates the Object */
        String configFile = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String line;
        String[] fields;

        line = reader.readLine();
        while(line != null){
            fields = line.split("\\|");
            services._services.add(new Service(fields[0], fields[1], fields[2], fields[3], fields[4]));
            line = reader.readLine();
        }
        reader.close();
    }

   public static void poll(Services services, String[] options){
       /*checks if website is up or down */

        ArrayList<String> included = new ArrayList<>();
        ArrayList<String> excluded = new ArrayList<>();

        if(options != null){
            
            for (int i = 0; i < options.length; i++){
                /*checks for flags */
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
        }

        services.poll(included, excluded);


    }

    public static void fetch(Services services, Scanner reader){
        /*executes poll with given interval 20 times */
        String[] options = tokenize(reader);
        int time = 5000;
        int counter = 0;

        if (options != null){
            for (int i = 0; i < options.length; i++){
                if(options[i].equals("--refresh")){
                    i++;
                    time = Integer.parseInt(options[i])*1000;
                } else {
                    System.out.println("Invalid Arguments");
                    return;
                }
            }
        }
        while (counter < 20){ //decided to go with 20, could be any number, even infinite
            try {
                poll(services, options);
                Thread.sleep(time);
                counter++;
            } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
            }
        }
    }

    public static void history(Services services, Scanner reader){
        /*prints history of all polls done */
        String[] options = tokenize(reader);
        ArrayList<String> included = new ArrayList<>();
        
        if (options != null){
            for (int i = 0; i < options.length; i++){
                /*checks for flags */
                if(options[i].equals("--only")){
                    i++;
                    String[] webSites = options[i].split(",");
                    for (String webSite : webSites){
                        included.add(webSite);
                    }
                } else {
                    System.out.println("Invalid Arguments");
                    return;
                }
            }
        }

        services.history(included);
    }

    public static void backup(Services services, Scanner reader) throws IOException{
        /*writes the current State to a txt, csv or binary file */
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
            PrintWriter writer = new PrintWriter(dataFile, "UTF-8");
            writer.print(services);
            writer.close();

        } else if(csv) {
            PrintWriter writer = new PrintWriter(dataFile);
            writer.print(services.toCSV());
            writer.close();
            
        }else {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile));
            out.writeObject(services);
            out.close();
        }

    }

    public static Services restore(Services services, Scanner reader){
        /*only restores binary files */
       
        String[] options = tokenize(reader);
        boolean merge = false;

        if (options == null){
            System.out.println("Please specify a file");
            return services;
        } else if (options.length <= 2){
            for (String s : options){
                if (s.equals("--merge")){
                    merge = true;
                    break; 
                }                    
                
            }
        } else {
            System.out.println("Invalid Arguments");
        }

        String dataFile = options[options.length-1];

        try {
            return open(dataFile, services, merge);
        }catch(FileNotFoundException e) {
            System.out.println("Please enter an existing file");
            return services;
        } catch (InvalidOperationException | IOException | ClassNotFoundException e){
            System.out.println("Please enter a valid file");
            return services;    
        }

    }

    public static Services open(String dataFile, Services services, boolean merge) throws InvalidOperationException, IOException, ClassNotFoundException{
        /*sets app curent state to the one in the dataFile */
        System.out.println("Restoring state");
        
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile));
        Services newServices = (Services) in.readObject();

        in.close();
        if (merge){
            services.merge(newServices);
            return services;
        } else {
            return newServices;
        }
    }

    public static void services(Services services){
        /*prints all webservices currently being tested */
        System.out.print(services);
    }

    public static void status(Services services){
        System.out.println("|   service   |  uptime  |");
        System.out.println("+-------------+----------+");
        services.status();
    }



    public static void help(){
        /*basic help screen */
        System.out.println("command [optional args] obligatory args\n");
        System.out.println("command: ");
        System.out.println("poll [--only= --exclude= ] - Retrieves the status from of all configured services");
        System.out.println("fetch [--only= --exclude= ]- Retrieves the status from of all configured services");
        System.out.println("services - Lists all known services");
        System.out.println("backup [--format=txt or csv] file - backups the current internal state to a file");
        System.out.println("restore [--merge] file - Imports the internal state from another run or app");
        System.out.println("history - Outputs all the data from the local storage");
        System.out.println("status - Summarizes data and displays it in a table-like fashion");
        System.out.println("help - This screen");
        System.out.println("exit - terminates application");    


    }


}