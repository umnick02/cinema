package com.cinema.core.service.player.subtitle;

import com.cinema.core.config.Lang;
import com.cinema.core.dto.SubtitleFile;
import com.cinema.core.dto.SubtitleFileEntry;
import com.cinema.core.entity.SubtitleHolder;
import com.cinema.core.model.impl.SceneModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cinema.core.service.parser.SubtitleParser.getSubtitleFile;
import static java.lang.Character.isLetter;

public class SubtitleService {

    private static final Pattern tagPattern = Pattern.compile("</?([biu])?((font)[^>]*)?>");

    public static void main(String[] args) throws IOException {
        buildSubtitles(new File("C:\\Users\\umnick\\Downloads\\Cinema\\Guns.Akimbo.2019.HDRip.XviD.AC3-EVO.srt"));
    }

    public static boolean isWord(String element) {
        return element.matches("^[^\\d\\W]+$") || element.matches("^[^\\d\\W]+[-']?[^\\d\\W]+$");
    }

    public static Set<SubtitleFileEntry> buildSubtitles(Lang lang) {
        SubtitleHolder subtitleHolder = SceneModel.INSTANCE.getActiveSubtitleHolder();
        SubtitleFile subtitleFile = getSubtitleWithLang(subtitleHolder, lang);
        if (subtitleFile == null) {
            return null;
        }
        File file = new File(subtitleFile.getFile());
        if (!file.exists()) {
            getSubtitleFile(subtitleHolder, lang);
            if (!file.exists()) {
                System.out.println("file not found");
                return null;
            }
        }
        try {
            return buildSubtitles(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SubtitleFile getSubtitleWithLang(SubtitleHolder subtitleHolder, Lang lang) {
        return subtitleHolder.getSubtitle().getSubtitles().stream()
                .filter(subtitleEntry -> subtitleEntry.getLang() == lang).findFirst().orElse(null);
    }

    private static Set<SubtitleFileEntry> buildSubtitles(File file) throws IOException {
        Set<SubtitleFileEntry> subtitles = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            SubtitleFileEntry subtitle = new SubtitleFileEntry();
            List<Character> chars = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0) {
                    subtitles.add(subtitle);
                } else if (line.contains(" --> ")) {
                    String[] times = line.split(" --> ");
                    subtitle.setFrom(LocalTime.parse(times[0].trim(), DateTimeFormatter.ofPattern("HH:mm:ss,SSS")));
                    subtitle.setTo(LocalTime.parse(times[1].trim().split(" ")[0].trim(), DateTimeFormatter.ofPattern("HH:mm:ss,SSS")));
                } else if (line.trim().matches("^\\d+\\s*$")) {
                    subtitle = new SubtitleFileEntry();
                } else {
                    if (subtitle.getElements().size() > 0) {
                        subtitle.getElements().add("\n");
                    }
                    char[] charArray = line.toCharArray();
                    for (int i = 0; i < charArray.length; i++) {
                        char curr = charArray[i];
                        if (curr == '<') {
                            Matcher matcher = tagPattern.matcher(line.substring(i));
                            if (matcher.find()) {
                                if (chars.size() > 0) {
                                    processChars(subtitle, chars);
                                }
                                subtitle.getElements().add(matcher.group());
                                i += matcher.end() - 1;
                                continue;
                            }
                        }
                        if (!isContinue(chars, curr, i < charArray.length - 1 ? charArray[i + 1] : null)) {
                            processChars(subtitle, chars);
                        }
                        chars.add(curr);
                    }
                    processChars(subtitle, chars);
                }
            }
            subtitles.add(subtitle);
        }
        return subtitles;
    }

    private static boolean isContinue(List<Character> chars, char curr, Character next) {
        return chars.size() == 0 ||
                isSameType(chars.get(chars.size() - 1), curr) ||
                (chars.size() > 1 && isSpecialWord(chars.get(chars.size() - 2), chars.get(chars.size() - 1), curr, next));
    }

    private static boolean isSameType(char prev, char curr) {
        return (isLetter(prev) && isLetter(curr)) || (!isLetter(prev) && !isLetter(curr));
    }

    private static boolean isSpecialWord(Character prevPrev, char prev, char curr, Character next) {
        return (Objects.nonNull(next) && isLetter(prev) && isInWordChar(curr) && isLetter(next)) ||
                (Objects.nonNull(prevPrev) && isLetter(prevPrev) && isInWordChar(prev) && isLetter(curr));
    }

    private static boolean isInWordChar(char c) {
        return c == '\'' || c == '-' || c == '.';
    }

    private static void processChars(SubtitleFileEntry subtitle, List<Character> chars) {
        String element = charsToString(chars);
        if (Objects.nonNull(element)) {
            subtitle.getElements().add(element);
            chars.clear();
        }
    }

    private static String charsToString(List<Character> chars) {
        return chars.stream().map(Object::toString).reduce((s1, s2) -> s1 + s2).orElse(null);
    }
}
