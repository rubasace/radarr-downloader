package dev.rubasace.radarr.downloader.movie.api.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class Movie {

    private final String title;
    private final Long id;
    private Status status;
    private MovieFile movieFile;

    public enum Status {
        TBA("tba"),
        ANNOUNCED("announced"),
        INCINEMAS("in_cinemas"),
        RELEASED("released"),
        DELETED("deleted");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static Status forValue(String value) {
            return Status.valueOf(value.toUpperCase());
        }
    }
}
