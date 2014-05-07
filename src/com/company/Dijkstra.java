package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Collections;
/**
 * Created by jessenelson on 4/18/14.
 */
public class Dijkstra {
    private HashSet<Router> nPrime;
    private HashSet<Link> yPrime;
    private ArrayList<Router> p;
    private ArrayList<Router> routers;

    public Dijkstra (int src, ArrayList<Router> nodes) {
        this.routers = nodes;
        this.nPrime = new HashSet<Router>();
        this.yPrime = new HashSet<Link>();
        this.nPrime = new HashSet<Router>();
        this.nPrime.add(this.routers.get(src));
        this.p = new ArrayList<Router>(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            this.p.add(nodes.get(src)); // initialize p to srcnode
        }

        buildUpTree(this.routers.get(src));
    }

    private ArrayList<Router> findPath(Router router, ArrayList<Router> path) {
        if (router == null) {
            Collections.reverse(path);
            return path;
        }
        path.add(router);
        return findPath(router.predecessor, path);
    }

    private void buildUpTree (Router source) {
        source.minDistance =0;
        PriorityQueue<Router> routerQ = new PriorityQueue<Router>();
        routerQ.add(source);
        while (!routerQ.isEmpty() && this.routers.size() != this.nPrime.size()) { // poll routers based on minDistance
            Router initial = routerQ.poll();
            this.nPrime.add(initial);
            this.yPrime.add(new Link (initial.predecessor, initial, Integer.MAX_VALUE));//horrible idea!...but it was quick
            this.p.set(initial.intIdentifier, initial.predecessor == null ? initial : initial.predecessor);
            for (int i = 0; i < initial.links.size(); i++) {
                Link link = initial.links.get(i);
                Router dest = link.end;
                int cost = link.cost;
                int distanceToInit = initial.minDistance + cost;
                if (distanceToInit < dest.minDistance) {
                    routerQ.remove(dest);
                    dest.minDistance = distanceToInit;
                    dest.predecessor = initial;
                    routerQ.add(dest);
                }
            }

            System.out.println();
            System.out.println("Y prime is");
            for (Link link : this.yPrime) {
                System.out.println("(" + link.start + ", " + link.end + ")");
            }

            System.out.println();

            System.out.println("N prime is");
            for(Router router : this.nPrime) {
                System.out.print(router.identifier + " ");
            }

            System.out.println();
            System.out.println();
            System.out.println("P is");
            for (Router router : this.p) {
                System.out.print(router + " ");
            }

            System.out.println();
            System.out.println("D is");
            for (Router router : this.routers) {
                System.out.print(router.minDistance + " ");
            }
            System.out.println();
        }
    }

    public String toString () {
        String object = "\n";
        for (int i = 0; i < this.routers.size(); i++) {
            Router r = this.routers.get(i);
            ArrayList<Router> path = findPath(r, new ArrayList<Router>());
            object +=
                    path.size() >= 2 ?
                            new String (r.identifier + " (" + path.get(0) + " " + path.get(1) + ")---> " + r.minDistance + "\n") : "\n";
        }

        return object;
    }
}
