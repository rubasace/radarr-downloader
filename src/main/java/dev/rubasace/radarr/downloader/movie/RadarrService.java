package dev.rubasace.radarr.downloader.movie;

import dev.rubasace.radarr.downloader.movie.api.command.MovieSearchCommand;
import dev.rubasace.radarr.downloader.movie.api.movie.Movie;
import dev.rubasace.radarr.downloader.movie.api.movie.MovieFile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
class RadarrService {

    private static final String API_PATH = "/api/v3/";
    private static final String GET_MOVIES_URI = "movie";
    private static final String GET_MOVIE_FILES_URI = "moviefile";
    private static final String COMMAND_URI = "command";
    private static final int MOVIE_FILES_BATCH_SIZE = 150;

    private final RestClient restClient;
    private final RadarrData radarrData;

    RadarrService(final RestClient.Builder builder, final RadarrData radarrData) {
        this.radarrData = radarrData;
        this.restClient = builder
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    Movie[] getMovies() {
        return restClient
                .get()
                .uri(radarrData.getUrl() + API_PATH + GET_MOVIES_URI)
                .header("X-Api-Key", radarrData.getToken())
                .retrieve()
                .body(Movie[].class);
    }

    void downloadMovies(final List<Long> ids) {
        MovieSearchCommand movieSearchCommand = new MovieSearchCommand(ids);
        restClient
                .post()
                .uri(radarrData.getUrl() + API_PATH + COMMAND_URI)
                .header("X-Api-Key", radarrData.getToken())
                .body(movieSearchCommand)
                .retrieve()
                .toBodilessEntity();

    }

    List<MovieFile> getMovieFiles(final List<Long> ids) {
        int index = 0;
        List<MovieFile[]> batches = new ArrayList<>();
        while (index < ids.size()) {
            batches.add(getMovieFileBatch(ids.subList(index, Math.min(index + MOVIE_FILES_BATCH_SIZE, ids.size()))));
            index += MOVIE_FILES_BATCH_SIZE;
        }
        return batches.stream()
                      .flatMap(Arrays::stream)
                      .toList();
    }

    private MovieFile[] getMovieFileBatch(final List<Long> ids) {
        String movieIdParam = ids.stream()
                                 .map(id -> "movieId=" + id)
                                 .collect(Collectors.joining("&"));
        return restClient
                .get()
                .uri(radarrData.getUrl() + API_PATH + GET_MOVIE_FILES_URI + "?" + movieIdParam)
                .header("X-Api-Key", radarrData.getToken())
                .retrieve()
                .body(MovieFile[].class);
    }
}
