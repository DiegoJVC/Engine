package com.cobelpvp.engine.chat;

public class ContainsFilter extends ChatFilter {

    private final String phrase;

    public ContainsFilter(String phrase, String command) {
        super(command);

        this.phrase = phrase;
    }

    @Override
    public boolean isFiltered(String message, String[] words) {
        for (String word : words) {
            if (word.contains(this.phrase)) {
                return true;
            }
        }

        return false;
    }

}
