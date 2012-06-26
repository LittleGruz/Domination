package littlegruz.domination.entities;

public class DomPlayer{
   private String name;
   private String region;
   private String lastCaptureRegion;
   
   public DomPlayer(String name){
      this.name = name;
      region = "";
      lastCaptureRegion = "";
   }
   
   public DomPlayer(String name, String region){
      this.name = name;
      this.region = region;
      lastCaptureRegion = "";
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

   public void setLastCaptureRegion(String lastCaptureRegion){
      this.lastCaptureRegion = lastCaptureRegion;
   }

   public String getLastCaptureRegion(){
      return lastCaptureRegion;
   }
}
