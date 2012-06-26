package littlegruz.domination.entities;

public class CapturePoint{
   private String name;
   private String owner;
   private String attacker;
   private int health;
   
   public CapturePoint(String name){
      this.name = name;
      owner = "";
      attacker = "";
      health = 0;
   }
   
   public CapturePoint(String name, String owner, String attacker, int health){
      this.name = name;
      this.owner = owner;
      this.attacker = attacker;
      this.health = health;
   }
   
   public String getName(){
      return name;
   }
   
   public void setName(String name){
      this.name = name;
   }
   
   public String getOwner(){
      return owner;
   }
   
   public void setOwner(String owner){
      this.owner = owner;
   }
   
   public String getAttacker(){
      return attacker;
   }
   
   public void setAttacker(String attacker){
      this.attacker = attacker;
   }
   
   public int getHealth(){
      return health;
   }
   
   public void setHealth(int health){
      this.health = health;
   }
}
