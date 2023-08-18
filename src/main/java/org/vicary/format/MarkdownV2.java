package org.vicary.format;

import java.util.Arrays;
import java.util.List;

public class MarkdownV2 {
    private StringBuilder sb;
    private static final List<Character> markdownV2Characters = Arrays.asList('_', '*', '[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}', '.', '!');

    private MarkdownV2(StringBuilder sb) {
        this.sb = sb;
    }

    public static MarkdownV2 apply(String text) {
        for (char c : markdownV2Characters)
            text = text.replace(String.valueOf(c), "\\" + c);
        return new MarkdownV2(new StringBuilder().append(text));
    }

    public MarkdownV2 toBold() {
        sb.insert(0, "*").insert(sb.length(), "*");
        return this;
    }

    public MarkdownV2 toItalic() {
        sb.insert(0, "_").insert(sb.length(), "_");
        return this;
    }

    public MarkdownV2 newlineBefore() {
        sb.insert(0, "\n");
        return this;
    }

    public MarkdownV2 newlineBefore(int amountOfLines) {
        for (int i = 0; i < amountOfLines; i++)
            sb.insert(0, "\n");
        return this;
    }

    public MarkdownV2 newlineAfter() {
        sb.insert(sb.length(), "\n");
        return this;
    }

    public MarkdownV2 newlineAfter(int amountOfLines) {
        for (int i = 0; i < amountOfLines; i++)
            sb.insert(sb.length(), "\n");
        return this;
    }

    public String get() {
        return sb.toString();
    }
}
