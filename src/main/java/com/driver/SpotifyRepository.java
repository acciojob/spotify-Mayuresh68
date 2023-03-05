package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {

        for(User user : users){
            if(user.getMobile().equals(mobile)){
                return user;
            }
        }
            User user = new User();
            user.setName(name);
            user.setMobile(mobile);
            users.add(user);
            return user;

    }

    public Artist createArtist(String name) {
        for(Artist artist : artists){
            if(artist.getName().equals(name)){
                return artist;
            }
        }

        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
//        if(artists.contains(artistName)){
//            Album album = new Album();
//            album.setTitle(title);
//            albums.add(album);
//        }
//        artists.add(createArtist(artistName));
//        Album album = new Album();
//        album.setTitle(title);
//        albums.add(album);

        // public HashMap<Artist, List<Album>> artistAlbumMap;

        //create a new artist
        Artist artist = createArtist(artistName);
        for(Album album : albums){
            if(album.getTitle().equals(title)){
                return album; // if already exit
            }
        }
        //create new album
        Album album = new Album(title);
        //adding album to ListDb
        albums.add(album);

        //putting artist nd album in Db
        List<Album> alb_List = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            artistAlbumMap.get(artist).add(album);
        }
        artistAlbumMap.put(artist,alb_List);// if not exist in Db
        return  album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean AlbumPresent = false;
        Album album = new Album();
        for(Album CurAlbum : albums){
            if(CurAlbum.getTitle().equals(albumName)){
                album=CurAlbum;
                AlbumPresent=true;
                break;
            }
        }
        if(AlbumPresent==false){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        //adding song to list songs
        songs.add(song);

        //adding album n its song to albumsongsMap
        List<Song> songslist= new ArrayList<>();
        if(albumSongMap.containsKey(album)){
            songslist=albumSongMap.get(album);
        }
        songslist.add(song);
        albumSongMap.put(album,songslist);

        return song;

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
      for(Playlist playlist : playlists){
          if(playlist.getTitle().equals(title)){
              return playlist;
          }
      }
      Playlist playlist = new Playlist(title);
      //addidng playlist to playlist List
        playlists.add(playlist);

        List<Song> temp = new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User curUser = new User();
        boolean flag = false;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                curUser = user;
                flag = true;
                break;
            }
        }

        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User curUser= new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag =false;
        Playlist playlist = new Playlist();
        for(Playlist curplaylist: playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                playlist=curplaylist;
                flag=true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("Playlist does not exist");
        }

        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }
//        public HashMap<Playlist, List<User>> playlistListenerMap;
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(curUser))
            userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);
//        public HashMap<User, Playlist> creatorPlaylistMap;
        if(creatorPlaylistMap.get(curUser)!=playlist)
            creatorPlaylistMap.put(curUser,playlist);
//        public HashMap<User, List<Playlist>> userPlaylistMap;
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);


        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User curuser = new User();
        boolean flag = false;
        for (User user : users) {
            if (user.getMobile().equals(mobile)) {
                curuser = user;
                flag = true;
                break;
            }
        }
        if (flag == false) {
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean check = false;
        for (Song cursong : songs) {
            if (cursong.getTitle().equals(songTitle)) {
                song = cursong;
                check = true;
                break;
            }
        }

        if (check == false) {
            throw new Exception("Song does not exist");
        }
        //HashMap<Song, List<User>> songLikeMap
        List<User> user = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            user = songLikeMap.get(song);
        }

        if(user.contains(curuser)==false) {
            user.add(curuser);
            songLikeMap.put(song, user);
            song.setLikes(song.getLikes() + 1);

            //HashMap<Album, List<Song>> albumSongmap Db
            //update the album-song Db
            Album album = new Album();
            for (Album curAlbum : albumSongMap.keySet()) {
                List<Song> list = albumSongMap.get(curAlbum);
                if (list.contains(song)) {
                    album = curAlbum;
                    break;
                }
            }

            //update the artistAlbumMap;
            //<Artist, List<Album>> artistAlbumMap
            Artist artist = new Artist();
            for (Artist curArtist : artistAlbumMap.keySet()) {
                List<Album> temp = artistAlbumMap.get(curArtist);
                if (temp.contains(album)) {
                    artist = curArtist;
                    break;
                }
            }
            artist.setLikes(artist.getLikes() + 1);
        }
        return song;

    }

    public String mostPopularArtist() {
        String artistName="";
        int maxLike = Integer.MIN_VALUE;
        for(Artist artist : artists){
            maxLike= Math.max(maxLike,artist.getLikes());
        }
        for(Artist artist : artists){
            if(maxLike==artist.getLikes()){
                artistName=artist.getName();
            }
        }
        return artistName;
    }

    public String mostPopularSong() {
        String songname="";

        int maxLike = Integer.MIN_VALUE;
        for(Song song : songs){
            maxLike = Math.min(maxLike,song.getLikes());
        }

        for( Song song:songs){
            if(maxLike == song.getLikes()){
                songname = song.getTitle();
            }
        }
        return songname;
    }
}
