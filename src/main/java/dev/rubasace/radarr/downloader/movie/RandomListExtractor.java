package dev.rubasace.radarr.downloader.movie;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomListExtractor {

    <T> List<T> getRandomElements(final List<T> list, final int amount) {
        Collections.shuffle(list, ThreadLocalRandom.current());
        return list.subList(0, amount);
    }
}
