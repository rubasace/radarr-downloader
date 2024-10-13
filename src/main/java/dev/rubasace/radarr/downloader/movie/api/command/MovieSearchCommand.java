package dev.rubasace.radarr.downloader.movie.api.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MovieSearchCommand {
    private final String name = "MoviesSearch";
    List<Long> movieIds;
}
