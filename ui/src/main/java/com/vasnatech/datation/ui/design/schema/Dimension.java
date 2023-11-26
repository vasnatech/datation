package com.vasnatech.datation.ui.design.schema;

public record Dimension(int width, int height) {

    @Override
    public String toString() {
        return width + "," + height;
    }

    public static Dimension valueOf(String valueAsText) {
        if (valueAsText == null) {
            return null;
        }
        String[] arr = valueAsText.split(",");
        if (arr.length != 2) {
            throw new IllegalArgumentException("Invalid dimension format \"" + valueAsText +"\"");
        }
        try {
            return new Dimension(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid dimension format \"" + valueAsText +"\"", e);
        }
    }
}
