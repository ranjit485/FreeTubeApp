package com.rdev.freetube.helpers;

public class TimeUtilities {

    public static String formatTime(float timeInSeconds) {
        int hours = (int) (timeInSeconds / 3600);
        int minutes = (int) ((timeInSeconds % 3600) / 60);
        int seconds = (int) (timeInSeconds % 60);

        StringBuilder formattedTime = new StringBuilder();

        if (hours > 0) {
            formattedTime.append(String.format("%02d:", hours));
        }

        formattedTime.append(String.format("%02d:%02d", minutes, seconds));

        return formattedTime.toString();
    }
}
