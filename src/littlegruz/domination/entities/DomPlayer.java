package littlegruz.domination.entities;

public class DomPlayer{
   private String name;
   private String party;
   private String region;
   
   public DomPlayer(String name){
      this.name = name;
      region = "";
   }
   
   public DomPlayer(String name, String region){
      this.name = name;
      this.region = region;
   }
   
   public String getRegion(){
      return region;
   }
   
   public void setRegion(String region){
      this.region = region;
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
