package com.nimatnemat.nine.domain.tastePlaylist;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "taste_playlist_table")
@Getter
@Setter
@NoArgsConstructor
public class TastePlaylist {
    @Id
    private ObjectId _id;
    private String userId;
    private String tastePlaylistName;
    private String tastePlaylistDesc;
    private Date createdAt;
    private int publicOrPrivate;
    private List<Long> playlistDetail;

    public TastePlaylist(String userId, String tasteplaylistName, String tastePlaylistDesc, Date createdAt, int publicOrPrivate, List<Long> playlistDetail){
        this.userId = userId;
        this.tastePlaylistName = tasteplaylistName;
        this.tastePlaylistDesc = tastePlaylistDesc;
        this.createdAt = createdAt;
        this.publicOrPrivate = publicOrPrivate;
        this.playlistDetail = playlistDetail;
    }
}