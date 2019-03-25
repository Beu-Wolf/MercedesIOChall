import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;    

public class Service implements Serializable{ 
    static final long serialVersionUID =  201810051539L;
    private String _serviceName;
    private String _serviceWebSite;
    private String _tag;
    private String _specifier;
    private String _isOnline;
    private String _state;
    private ArrayList<String> _history;
    private ArrayList<LocalDateTime> _times;

    Service(String name, String webSite, String tag, String specifier, String isOnline) {
        _serviceName = name;
        _serviceWebSite = webSite;
        _tag = tag;
        _specifier = specifier;
        _isOnline = isOnline;
        _state = "up";
        _history = new ArrayList<String>();
        _times = new ArrayList<LocalDateTime>();

    }

    public String getName(){
        return _serviceName;
    }

    public String getWebSite(){
        return _serviceWebSite;
    }

    public void setState(String state){
        _state = state;
    }

    public String getState(){
        return _state;
    }

    public void addToHistory(String currState){
        _history.add(currState);
    }

    public String getTag(){
        return _tag;
    }

    public String getSpecifier(){
        return _specifier;
    }

    public String isOnline(){
        return _isOnline;
    }

    public ArrayList<String> getHistory(){
        return _history;
    }

    public void servicePoll(){
        try {
            Document doc = Jsoup.connect(getWebSite()).get();
            String tagClass = getTag() + "." + getSpecifier();
            Elements onlineInfo = doc.select(tagClass);
            setState("down");
            for (Element e : onlineInfo){
                if(e.text().equals(isOnline())){
                    setState("up");
                    break;
                }
            }

        } catch (IOException e){
            setState("down");
        }


          
        LocalDateTime now = LocalDateTime.now();
        _times.add(now);
        String message = now + " - " + getState();
        addToHistory(message);
        System.out.println("[" + getName() + "] " + message);
        
    }

    public void merge(Service s){
        for(String status : s.getHistory()){
            if(!getHistory().contains(s))
                _history.add(status);
        }
    }

    public void showHistory(){
        for(String s : getHistory()){
            System.out.println(s);
        }
    }

    public String status(){
        Duration max = Duration.ZERO;
        LocalDateTime now = LocalDateTime.now();
        for (LocalDateTime d : _times){
            Duration possMax = Duration.between(d,now);
            if (possMax.compareTo(max) > 0){
                max = possMax;
            }
        }


        return "-> " + getName()  +  "           " + max.toMinutes(); 
    }

    @Override
    public String toString(){
        return getName() + " - " + getWebSite() + " - " + getTag() + " - " + getSpecifier() + " - " + isOnline();
    }


}