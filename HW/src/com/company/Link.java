package com.company;

/**
 * Created by jessenelson on 4/18/14.
 */
public class Link {
    Router start;
    Router end;
    int cost;

    public Link (Router start, Router end, int cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    public boolean equals (Object that) {
        if (that == null) return false;
        if (this == that) return true;
        if (!(that instanceof Link)) return false;
        Link link = (Link) that;

        return this.start == link.start && this.end == link.end;
    }

}
