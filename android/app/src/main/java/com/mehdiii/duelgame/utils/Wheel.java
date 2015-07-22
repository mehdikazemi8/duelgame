package com.mehdiii.duelgame.utils;

/**
 * Created by Omid on 7/22/2015.
 */
public class Wheel {
    private int start, end, pointer;

    public Wheel(int start, int end, int current) {
        if (end < start)
            throw new IllegalStateException("end must be greater than start.");
        this.start = start;
        this.end = end;
        this.pointer = end;

    }

    public Wheel(int start, int end) {
        this(start, end, start);
    }

    public int move() {
        this.pointer--;
        if (pointer < start)
            pointer = end;

        return pointer;
    }

    public int peek() {
        return pointer;
    }

    public void reset() {
        this.pointer = end;
    }
}
