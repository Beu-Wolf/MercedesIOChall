
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;

public class Services implements Serializable {
    static final long serialVersionUID =  201810051539L;
    final ArrayList<Service> _services = new ArrayList<>();

    public ArrayList<Service> getServices(){
        return _services;
    }


    public void poll(ArrayList<String> included, ArrayList<String> excluded){
        for (Service s : getServices()){
            if( (included.size() == 0 && excluded.size() == 0 ) || (included.size() > 0 && included.contains(s.getName())) || (excluded.size() > 0 && !excluded.contains(s.getName()))){
                s.servicePoll();
            } 
        }
    }

    public void merge(Services newServices){
        boolean found = false;
        for (Service s : newServices.getServices()){
            for (Service myS : getServices()){
                if(myS.getName().equals(s.getName())){
                    myS.merge(s);
                    found = true;
                    break;
                }
            }
            if(!found){
                _services.add(s);
            }
            found = false;

        }

        
    }

    public void history(ArrayList<String> included){
        for(Service s : getServices()){
            if (included.size() == 0 || (included.contains(s.getName()))){
                System.out.println("======================" + s.getName() + "====================");
                s.showHistory();
            }
        }
    }

    public void showAll(){
        for(Service s : getServices()){
            System.out.println(s);
        }
    }
}