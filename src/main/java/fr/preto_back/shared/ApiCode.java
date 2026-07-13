package fr.preto_back.shared;

import lombok.Getter;

@Getter
public enum ApiCode {
    TEST("Test response message");

    private final String message;

    ApiCode(String message) { this.message = message; }
}
