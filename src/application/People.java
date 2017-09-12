package application;

import java.io.Serializable;

public class People implements Serializable{
    public String name;
    public String userName;
    public String email;
    public boolean isActive;
    public int counter;

    public People(String name,String userName,String email){
        this.name=name;
        this.userName=userName;
        this.email=email;
        isActive=false;
        counter=0;
    }

    public String toString(){
        return name;
    }
}
