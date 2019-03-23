
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;

public class Services implements Serializable {
    static final long serialVersionUID =  201810051539L;
    final ArrayList<Service> _services = new ArrayList<>();


    public void poll(ArrayList<String> included, ArrayList<String> excluded){
        for (Service s : _services){
            if( (included.size() == 0 && excluded.size() == 0 ) || (included.size() > 0 && included.contains(s.getName())) || (excluded.size() > 0 && !excluded.contains(s.getName()))){
                s.servicePoll();
            } 
        }
    }
}