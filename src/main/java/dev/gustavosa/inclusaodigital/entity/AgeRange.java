package dev.gustavosa.inclusaodigital.entity;

public enum AgeRange {

    SIXTY_TO_SIXTY_NINE("60 a 69 anos"),
    SEVENTY_TO_SEVENTY_NINE("70 a 79 anos"),
    EIGHTY_OR_MORE("80 anos ou mais");

    private final String label;

    AgeRange(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
