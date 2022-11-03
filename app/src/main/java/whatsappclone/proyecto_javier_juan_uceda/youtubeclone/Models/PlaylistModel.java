package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Models;

public class PlaylistModel {
   private String playListName;
   private String uid;
   private int videos;

   public PlaylistModel(String playListName, String uid, int videos) {
      this.playListName = playListName;
      this.uid = uid;
      this.videos = videos;
   }

   public PlaylistModel() {
      this.playListName = "";
      this.uid = "";
      this.videos = 0;
   }

   public String getPlayListName() {
      return playListName;
   }

   public void setPlayListName(String playListName) {
      this.playListName = playListName;
   }

   public String getUid() {
      return uid;
   }

   public void setUid(String uid) {
      this.uid = uid;
   }

   public int getVideos() {
      return videos;
   }

   public void setVideos(int videos) {
      this.videos = videos;
   }
}
