package dev.rubasace.radarr.downloader;

import dev.rubasace.radarr.downloader.movie.MoviesService;
import dev.rubasace.radarr.downloader.movie.RadarrData;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class RadarrDownloaderApplication implements ApplicationRunner {

    private static final String DEFAULT_RADARR_URL = "http://localhost:7878";
    private static final int DEFAULT_MISSING_MOVIES_AMOUNT = 15;
    private static final int DEFAULT_CUTOFF_AMOUNT = 10;
    private static final int DEFAULT_CUSTOM_FORMAT_WANTED_SCORE = 0;

    private static final String HELP_ARGUMENT = "help";
    private static final String TOKEN_ARGUMENT = "token";
    private static final String RADARR_URL_ARGUMENT = "url";
    private static final String MISSING_ARGUMENT = "missing";
    private static final String CUTOFF_NOT_MET_ARGUMENT = "cutoff";
    private static final String CUSTOM_FORMAT_WANTED_SCORE_ARGUMENT = "score";


    private final MoviesService moviesService;
    private final RadarrData radarrData;

    public RadarrDownloaderApplication(final MoviesService moviesService, final RadarrData radarrData) {
        this.moviesService = moviesService;
        this.radarrData = radarrData;
    }

    public static void main(String[] args) {
        SpringApplication.run(RadarrDownloaderApplication.class, args);
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (args.containsOption(HELP_ARGUMENT)) {
            printUsage();
            return;
        }
        if (!args.containsOption(TOKEN_ARGUMENT)) {
            throw new RuntimeException("Radarr API token is mandatory");
        }

        String token = args.getOptionValues(TOKEN_ARGUMENT).get(0);

        String radarrUrl = Optional.ofNullable(args.getOptionValues(RADARR_URL_ARGUMENT))
                                   .map(values -> values.get(0))
                                   .map(url -> url.endsWith("/") ? url.substring(0, url.length() - 1) : url)
                                   .orElse(DEFAULT_RADARR_URL);

        radarrData.setToken(token);
        radarrData.setUrl(radarrUrl);

        int missingMoviesAmount = Optional.ofNullable(args.getOptionValues(MISSING_ARGUMENT))
                                          .map(values -> Integer.parseInt(values.get(0)))
                                          .orElse(DEFAULT_MISSING_MOVIES_AMOUNT);
        int cutoffNotMetAmount = Optional.ofNullable(args.getOptionValues(CUTOFF_NOT_MET_ARGUMENT))
                                         .map(values -> Integer.parseInt(values.get(0)))
                                         .orElse(DEFAULT_CUTOFF_AMOUNT);
        int customFormatWantedScore = Optional.ofNullable(args.getOptionValues(CUSTOM_FORMAT_WANTED_SCORE_ARGUMENT))
                                              .map(values -> Integer.parseInt(values.get(0)))
                                              .orElse(DEFAULT_CUSTOM_FORMAT_WANTED_SCORE);
        moviesService.searchWanted(missingMoviesAmount, cutoffNotMetAmount, customFormatWantedScore);
    }

    private static void printUsage() {
        System.out.println("Usage: radarr-downloader [options]");
        System.out.println("Options:");
        System.out.println(toHelpCommand(HELP_ARGUMENT) + "\tShow this help message and exit");
        System.out.println(toHelpCommand(TOKEN_ARGUMENT) + "\tRadarr API token (mandatory)");
        System.out.println(toHelpCommand(RADARR_URL_ARGUMENT) + "\tRadarr URL, defaults to " + DEFAULT_RADARR_URL);
        System.out.println(toHelpCommand(MISSING_ARGUMENT) + "\tAmount of missing movies to download, defaults to " + DEFAULT_MISSING_MOVIES_AMOUNT);
        System.out.println(toHelpCommand(CUTOFF_NOT_MET_ARGUMENT) + "\tAmount of movies that not meet the custom format cutoff to download, defaults to " + DEFAULT_CUTOFF_AMOUNT);
        System.out.println(toHelpCommand(CUSTOM_FORMAT_WANTED_SCORE_ARGUMENT) + "\tWanted minimum custom format score, defaults to " + DEFAULT_CUSTOM_FORMAT_WANTED_SCORE);
    }

    private static String toHelpCommand(final String argument) {
        String posfix = argument.length() > 5 ? "" : "\t";
        return "\t--" + argument + posfix;
    }
}
