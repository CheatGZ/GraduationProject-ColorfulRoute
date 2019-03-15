package com.bupt.colorfulroute.runningapp.struct;

public class HistoryItem {
    private String StartTime;
    private String RouteLengthText;


    public HistoryItem(String StartTime, String RouteLengthText) {
        this.RouteLengthText = RouteLengthText;
        this.StartTime = StartTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getRouteLengthText() {
        return RouteLengthText;
    }

    public void setRouteLengthText(String routeLengthText) {
        RouteLengthText = routeLengthText;
    }
}
