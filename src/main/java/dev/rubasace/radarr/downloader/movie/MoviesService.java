package dev.rubasace.radarr.downloader.movie;

import dev.rubasace.radarr.downloader.movie.api.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class MoviesService {

    private static final Logger LOGGER = LoggerFactory.getLogger("radarr-downloader");

    private final MoviesCategorizer moviesCategorizer;
    private final RadarrService radarrService;
    private final RandomListExtractor randomListExtractor;

    MoviesService(final MoviesCategorizer moviesCategorizer,
                  final RadarrService radarrService,
                  final RandomListExtractor randomListExtractor) {
        this.moviesCategorizer = moviesCategorizer;
        this.radarrService = radarrService;
        this.randomListExtractor = randomListExtractor;
    }


    public void searchWanted(final int missingMoviesAmount, final int cutoffNotMetAmount, final int customFormatWantedScore) {
        LOGGER.info("Searching wanted movies, looking for {} missing and {} not cutoff ({} score) met movies", missingMoviesAmount, cutoffNotMetAmount, customFormatWantedScore);

        Movie[] movies = radarrService.getMovies();
        CategorizedMovies categorizedMovies = moviesCategorizer.categorize(movies, customFormatWantedScore);

        List<Movie> missingMovies = randomListExtractor.getRandomElements(categorizedMovies.getMissingMovies(), missingMoviesAmount);
        List<Movie> notCutoffMetMovies = randomListExtractor.getRandomElements(categorizedMovies.getNotCutoffMetMovies(), cutoffNotMetAmount);

        logSelectedResults(missingMovies, notCutoffMetMovies, categorizedMovies);

        List<Long> movieIdsToDownload = Stream.of(missingMovies, notCutoffMetMovies)
                                              .flatMap(List::stream)
                                              .map(Movie::getId)
                                              .toList();

        radarrService.downloadMovies(movieIdsToDownload);

    }

    private void logSelectedResults(final List<Movie> missingMovies, final List<Movie> notCutoffMetMovies, final CategorizedMovies categorizedMovies) {
        LOGGER.info("Missing movies: {}", categorizedMovies.getMissingMovies().size());
        LOGGER.info("Not cutoff met movies: {}", categorizedMovies.getNotCutoffMetMovies().size());
        LOGGER.info("Movies in desired state: {}", categorizedMovies.getReadyMovies().size());

        if (!missingMovies.isEmpty()) {
            LOGGER.info("Sending missing movies to download: {}", getMovieNames(missingMovies));
        }
        if (!notCutoffMetMovies.isEmpty()) {
            LOGGER.info("Sending not cutoff met movies to download: {}", getMovieNames(notCutoffMetMovies));
        }
    }

    private List<String> getMovieNames(final List<Movie> movies) {
        return movies.stream()
                     .map(Movie::getTitle)
                     .toList();
    }
}
