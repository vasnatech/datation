package com.vasnatech.datation.ui.design.schema;

public record Insets(int left, int top, int right, int bottom) {

    @Override
    public String toString() {
        return left + "," + top + "," + right + "," + bottom;
    }

    public static Insets valueOf(String valueAsText) {
        if (valueAsText == null) {
            return null;
        }
        String[] arr = valueAsText.split(",");
        if (arr.length != 4) {
            throw new IllegalArgumentException("Invalid margin format \"" + valueAsText +"\"");
        }
        try {
            return new Insets(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid margin format \"" + valueAsText +"\"", e);
        }
    }
}
