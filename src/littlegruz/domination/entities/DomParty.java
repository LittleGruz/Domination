package littlegruz.domination.entities;

import java.util.HashMap;

public class DomParty{
   private String name;
   private HashMap<String, String> members;
   
   public DomParty(String name){
      this.name = name;
      members = new HashMap<String, String>();
   }

   public void setName(String name){
      this.name = name;
   }

   public String getName(){
      return name;
   }
   
   public void addMember(String name){
      members.put(name, name);
   }
   
   public boolean isMember(String name){
      if(members.get(name) == null)
         return true;
      else
         return false;
   }
   
   public void removeMember(String name){
      members.remove(name);
   }
}
