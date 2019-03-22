package sol;

import java.io.Serializable;
import java.util.ArrayList;

public class Service implements Serializable{ 
    static final long serialVersionUID =  201810051539L;
    private String _serviceName;
    private String _serviceWebSite;
    private String _state;
    private ArrayList<String> _history;

    Service(String name, String webSite) {
        _serviceName = name;
        _serviceWebSite = webSite;
        _state = "up";
        _history = new ArrayList<String>();
    }

    public String getName(){
        return _serviceName;
    }

    public String getWebSite(){
        return _serviceWebSite;
    }


}