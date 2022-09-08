package com.gngsn.jpademo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "movie_genres")
public class MovieGenre {

    @EmbeddedId
    private MovieGenreId id;

    @ManyToOne
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Getter
    @Setter
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class MovieGenreId implements Serializable {

        @Column(name = "movie_id", columnDefinition = "int(4)", nullable = false)
        private String movieId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static public class MovieGenreEssential {

        private String movieId;
        private String genreName;
    }
}