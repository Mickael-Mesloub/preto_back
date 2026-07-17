package fr.preto_back.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StringUtils {
    public static String trimOrNull(String string) {
        return string == null ? null : string.trim();
    }
}
