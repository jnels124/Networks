package com.company;
import java.util.ArrayList;
import java.util.HashSet;
/**
 * Created by jessenelson on 4/18/14.
 */
public class Router implements Comparable<Router>{
    public String identifier;
    public int intIdentifier;
    public ArrayList<Link> links;
    public Router predecessor;
    public int minDistance = Integer.MAX_VALUE -100;

    public Router (String identifier, ArrayList<Link> links, int intIdentifier) {
        this.identifier = identifier;
        this.intIdentifier = intIdentifier;
        this.links = links;
        this.predecessor = null;
    }

    public Router (String identifier, int intIdentifier) {
        this.identifier = identifier;
        this.intIdentifier = intIdentifier;
        this.links = new ArrayList<Link>();
        this.predecessor = null;
    }

    public boolean equals (Object that) {
        if (that == null) return false;
        if (this == that) return true;
        if (!(that instanceof Router)) return false;
        Router temp = (Router)that;
        return this.identifier == temp.identifier;
    }

    public int compareTo(Router that) {
        Router router  = (Router) that;
        return Integer.compare(this.minDistance, router.minDistance);
    }

    public String toString () {
        return this.identifier;
    }
}
