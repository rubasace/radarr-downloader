package dev.rubasace.radarr.downloader.movie;

import dev.rubasace.radarr.downloader.movie.api.movie.Movie;
import dev.rubasace.radarr.downloader.movie.api.movie.MovieFile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class MoviesCategorizer {

    private final RadarrService radarrService;

    MoviesCategorizer(final RadarrService radarrService) {
        this.radarrService = radarrService;
    }

    CategorizedMovies categorize(Movie[] movies, final int customFormatWantedScore) {
        CategorizedMovies categorizedMovies = new CategorizedMovies();
        Map<Long, Movie> availableMovies = new HashMap<>();
        for (Movie movie : movies) {
            if (movie.getMovieFile() == null) {
                categorizedMovies.getMissingMovies().add(movie);
            } else {
                availableMovies.put(movie.getId(), movie);
            }
        }
        List<MovieFile> movieFiles = radarrService.getMovieFiles(availableMovies.keySet().stream().toList());
        for (MovieFile movieFile : movieFiles) {
            if (movieFile.getCustomFormatScore() < customFormatWantedScore) {
                categorizedMovies.getNotCutoffMetMovies().add(availableMovies.get(movieFile.getMovieId()));
            } else {
                categorizedMovies.getReadyMovies().add(availableMovies.get(movieFile.getMovieId()));
            }
        }
        return categorizedMovies;
    }

}
