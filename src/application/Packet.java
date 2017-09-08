package application;

import java.io.Serializable;
import java.util.*;

public class Packet implements Serializable{
   public String operation=null;
   public String string1=null;
   public String string2=null;
   public String string3=null;
   public List <Message> list;

   public Packet(){
      list=new ArrayList<>();
   }

}
