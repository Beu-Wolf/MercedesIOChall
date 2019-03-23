import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;  
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

    Service(String name, String webSite, String tag, String specifier, String isOnline) {
        _serviceName = name;
        _serviceWebSite = webSite;
        _tag = tag;
        _specifier = specifier;
        _isOnline = isOnline;
        _state = "up";
        _history = new ArrayList<String>();

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


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        String message = now + " - " + getState();
        addToHistory(message);
        System.out.println("[" + getName() + "]" + " " + message);
        



    }


}