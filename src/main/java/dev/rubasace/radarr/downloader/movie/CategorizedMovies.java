package dev.rubasace.radarr.downloader.movie;

import dev.rubasace.radarr.downloader.movie.api.movie.Movie;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategorizedMovies {

    private List<Movie> missingMovies = new ArrayList<>();
    private List<Movie> notCutoffMetMovies = new ArrayList<>();
    private List<Movie> readyMovies = new ArrayList<>();

}
