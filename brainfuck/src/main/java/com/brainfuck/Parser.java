package com.brainfuck;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    public static Optional<Stream<String>> readFileContent(File file) {
        try {
            Scanner s = new Scanner(file);
            List<String> lines = new ArrayList<>();
            s.tokens().forEach(lines::add);
            s.close();
            return Optional.of(lines.stream());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<String> parseFileContent(Stream<String> content) {
        String legal_tokens = "+-<>[].,";
        String result = content.filter(s -> s.chars()
                .allMatch(c -> legal_tokens.contains(String.valueOf((char) c))))
                .collect(Collectors.joining());
        if (!areParenthesesBalanced(result))
            return Optional.empty();
        return Optional.of(result);
    }

    public static Optional<String> parseFile(File file) {
        return readFileContent(file).flatMap(Parser::parseFileContent);
    }

    private static boolean areParenthesesBalanced(String content) {
        String parens = content.chars()
                .mapToObj(Character::toString)
                .filter(c -> "[]".contains(c))
                .collect(Collectors.joining(content));
        Stack<Character> counter = new Stack<>();
        for (Character c : parens.toCharArray()) {
            switch (c) {
                case '[':
                    counter.add(c);
                    break;
                case ']':
                    if (!counter.isEmpty()) {
                        counter.pop();
                    } else {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return counter.isEmpty();
    }
}