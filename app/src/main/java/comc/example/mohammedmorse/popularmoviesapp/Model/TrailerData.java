package comc.example.mohammedmorse.popularmoviesapp.Model;

/**
 * Created by Mohammed Morse on 29/06/2018.
 */

public class TrailerData {
    public String Key;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String Name;
    public String Size;
    public TrailerData(){}
    public TrailerData(String k,String n,String s){
        this.Name=n;
        this.Size=s;
        this.Key=k;
    }
}
