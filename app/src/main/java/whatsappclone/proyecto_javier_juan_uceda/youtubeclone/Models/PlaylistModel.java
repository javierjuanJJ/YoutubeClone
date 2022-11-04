package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Models;

public class PlaylistModel {
   private String playlist_name;
   private String uid;
   private int videos;

   public PlaylistModel(String playListName, String uid, int videos) {
      this.playlist_name = playListName;
      this.uid = uid;
      this.videos = videos;
   }

   public PlaylistModel() {
      this.playlist_name = "";
      this.uid = "";
      this.videos = 0;
   }

   public String getPlaylist_name() {
      return playlist_name;
   }

   public void setPlaylist_name(String playlist_name) {
      this.playlist_name = playlist_name;
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
