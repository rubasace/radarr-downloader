package dev.rubasace.radarr.downloader.movie;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RadarrData {

    private String url;
    private String token;
}
