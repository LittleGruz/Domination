package littlegruz.domination.entities;

public class DomPlayer{
   private String name;
   private String party;
   
   public DomPlayer(String name){
      this.name = name;
      party = "";
   }
   
   public DomPlayer(String name, String region, String party){
      this.name = name;
      this.party = party;
   }
   
   public String getName(){
      return name;
   }
   
   public void setName(String name){
      this.name = name;
   }
   
   public String getParty(){
      return party;
   }
   
   public void setParty(String party){
      this.party = party;
   }
}
