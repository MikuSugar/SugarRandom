package me.mikusugar.random.core.sugarenum;

/**
 * @author mikusugar
 */
public enum RANDOM_MODE {
    JSON("JSON"),CSV("CSV");

    private final String data;

    RANDOM_MODE(String value){
        this.data =value;
    }

    public String getData() {
        return data;
    }

    public static RANDOM_MODE getMODE(String data){
        if(data.equals(JSON.data))return JSON;
        return CSV;
    }
}
