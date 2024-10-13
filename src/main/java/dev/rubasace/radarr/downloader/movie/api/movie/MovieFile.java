package dev.rubasace.radarr.downloader.movie.api.movie;

import lombok.Data;

import java.util.List;

@Data
public class MovieFile {
    private long movieId;
    private boolean qualityCutoffNotMet;
    private Long customFormatScore;
    private List<Language> languages = List.of();

}
