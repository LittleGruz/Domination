package littlegruz.domination.permissions;

public enum PermissionNode {
   CAPTUREPOINTS(".capturepoints"),
   PARTIES(".parties"),
   WORLDS(".worlds"),
   TOWNSHIPS(".townships"),
   SCORES(".scores"),
   SPAWNS(".spawns"),
   /**
    * Admin nodes
    */
   ADMIN_RELOAD(".admin.reload");

   private static final String prefix = "domination";
   private String node;

   private PermissionNode(String node){
      this.node = prefix + node;
   }

   public String getNode(){
      return node;
   }

}