package com.tozil.chessclock;

public class TimerItem {

    private String timerName;
    private long timerTopMilliSeconds;
    private long timerTopMilliSecondsDelay;
    private long timerTopMilliSecondsIncrement;
    private long timerBottomMilliSeconds;
    private long timerBottomMilliSecondsDelay;
    private long timerBottomMilliSecondsIncrement;

    public TimerItem(String timerName, long timerTopMilliSeconds, long timerTopMilliSecondsDelay, long timerTopMilliSecondsIncrement, long timerBottomMilliSeconds, long timerBottomMilliSecondsDelay, long timerBottomMilliSecondsIncrement) {
        this.timerName = timerName;
        this.timerTopMilliSeconds = timerTopMilliSeconds;
        this.timerTopMilliSecondsDelay = timerTopMilliSecondsDelay;
        this.timerTopMilliSecondsIncrement = timerTopMilliSecondsIncrement;
        this.timerBottomMilliSeconds = timerBottomMilliSeconds;
        this.timerBottomMilliSecondsDelay = timerBottomMilliSecondsDelay;
        this.timerBottomMilliSecondsIncrement = timerBottomMilliSecondsIncrement;
    }

    public String getTimerName() {
        return timerName;
    }

    public long getTimerTopMilliSeconds() {
        return timerTopMilliSeconds;
    }

    public long getTimerTopMilliSecondsDelay() {
        return timerTopMilliSecondsDelay;
    }

    public long getTimerTopMilliSecondsIncrement() {
        return timerTopMilliSecondsIncrement;
    }

    public long getTimerBottomMilliSeconds() {
        return timerBottomMilliSeconds;
    }

    public long getTimerBottomMilliSecondsDelay() {
        return timerBottomMilliSecondsDelay;
    }

    public long getTimerBottomMilliSecondsIncrement() {
        return timerBottomMilliSecondsIncrement;
    }
}
