package com.vasnatech.datation.ui.design.schema;

public record Rectangle(int x, int y, int width, int height) {

    @Override
    public String toString() {
        return x + "," + y + "," + width + "," + height;
    }

    public static Rectangle valueOf(String valueAsText) {
        if (valueAsText == null) {
            return null;
        }
        String[] arr = valueAsText.split(",");
        if (arr.length != 4) {
            throw new IllegalArgumentException("Invalid rectangle format \"" + valueAsText +"\"");
        }
        try {
            return new Rectangle(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rectangle format \"" + valueAsText +"\"", e);
        }
    }
}
