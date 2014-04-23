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
    private ArrayList<Link> yPrime;
    private ArrayList<Router> p;
    private ArrayList<Link> links;
    private ArrayList<Router> routers;
    private int [][] costMatrix;

    public Dijkstra (int src, ArrayList<Router> nodes) {
        this.links = links;
        this.routers = nodes;//generateRouters(numRouters);
        this.nPrime = new HashSet<Router>();
        this.yPrime = new ArrayList<Link>();
        //this.nPrime.add(this.routers.get(0));
        this.p = new ArrayList<Router>(nodes);
        //this.costMatrix = new int[this.routers.size()][this.routers.size()];
//        for (int i = 0; i < routers.size(); i++){
//            for (int j = 0; j < routers.size(); j++) {
//                if (i == j) {
//                    this.costMatrix[i][j] = 0;
//                }
//                else {
//                    this.costMatrix[i][j] = Integer.MAX_VALUE - 100;
//                }
//            }
//        }
        //initialize();
        getPaths(this.routers.get(0));
//        getPaths(this.routers.get(1));
//        getPaths(this.routers.get(2));
//        getPaths(this.routers.get(3));
//        getPaths(this.routers.get(4));
//        getPaths(this.routers.get(5));


        //loop();
//        getPaths(this.routers.get(1));
//        getPaths(this.routers.get(2));
//        getPaths(this.routers.get(3));
//        getPaths(this.routers.get(4));
//        getPaths(this.routers.get(5));
        //shortestPathToDest(this.routers.get(3));
        printShortestPathTree();
        //loop();
    }

    private ArrayList<Router> findPath(Router router, ArrayList<Router> path) {
        if (router == null) {
            Collections.reverse(path);
            return path;
        }
        path.add(router);
        return findPath(router.predecessor, path);
    }

    private Link findMinLink (Router router, ArrayList<Link> links) {
        Link temp = links.get(0);
        for (Link link : links) {
            if (temp.cost < link.cost) {
                temp = link;
            }
            //System.out.println("The link is " + link.start + " Dest: " + link.end + "Cost: " + link.cost);
            //System.out.println("The destination router is " + dest.identifier + " and the min is " + dest.minDistance);
            //int cost = link.cost;
            //int distanceToInit = router.minDistance + cost;
//            if (distanceToInit < dest.minDistance) {
//                routerQ.remove(dest);
//                this.yPrime.add(link);
//                dest.minDistance = distanceToInit;
//                //System.out.println("The dest new min distance is " + dest.minDistance);
//                this.costMatrix[router.intIdentifier][dest.intIdentifier] = dest.minDistance;
//                this.costMatrix[dest.intIdentifier][router.intIdentifier] = dest.minDistance;
//                //System.out.println("CostMatrix[" + initial.intIdentifier +"][" + dest.intIdentifier +"] is" + this.costMatrix[initial.intIdentifier][dest.intIdentifier] );
//                //System.out.println("CostMatrix[" + dest.intIdentifier +"][" + initial.intIdentifier +"] is" + this.costMatrix[dest.intIdentifier][initial.intIdentifier] );
//                dest.predecessor = router;
//                routerQ.add(dest);
//                //System.out.println("Router " + dest + " was added to the Q" );
//                temp = dest;
//            }
        }
        return temp;
    }
    private void loop () {
//        while (this.nPrime.size() != this.routers.size()) {
//            ArrayList<Router> notInNPrime = notInNprime();
//            Link k = findMinimumLink(notInNPrime);
//            this.nPrime.add(k.);
//            this.yPrime.add(k);
//
//        }
//        for (int i = 0; i < routers.size(); i++) {
////            ArrayList<Router> notInNPrime = notInNprime();
////            Link kLink = findMinimumLink(notInNPrime, i);
////            Router k = this.routers.get(kLink.start.intIdentifier);
////            System.out.println("K is " + k);
////            this.nPrime.add(k);
////            this.yPrime.add(kLink);
//            ArrayList<Router> update = adjacentRouters(k);
//            for (Link link : k.links) { //update costs
//                int cost = link.cost;
//                int distanceToK = k.minDistance + cost;
//                Router dest = link.end;
//                if(distanceToK < dest.minDistance) {
//                    dest.minDistance  = distanceToK;
//                    dest.predecessor = k;
//                    this.costMatrix[link.start.intIdentifier][link.end.intIdentifier] = distanceToK;
//                    this.costMatrix[link.end.intIdentifier][link.start.intIdentifier] = distanceToK;
//                }
//
//            }
//        }
        /*for (Router router : this.routers) {

        }*/
    }

    private Link findLink(Router one, Router two) {
        Link dd = new Link();
        if (two != null) {
            for (Link link : two.links) {
                if (link.end.intIdentifier == one.intIdentifier) return link;
                dd = link;
            }
        } return dd;
    }
    //private Router()
    private void getPaths (Router source) {
        source.minDistance =0;
        PriorityQueue<Router> routerQ = new PriorityQueue<Router>();
        routerQ.add(source);
        while (!routerQ.isEmpty()) { // poll routers based on minDistance
            Router initial = routerQ.poll();
            this.nPrime.add(initial);
            //Router dest;
            for (Link link : initial.links) {
                Router dest = link.end;
                //System.out.println("The link is " + link.start + " Dest: " + link.end + "Cost: " + link.cost);
                //System.out.println("The destination router is " + dest.identifier + " and the min is " + dest.minDistance);
                int cost = link.cost;
                int distanceToInit = initial.minDistance + cost;
                if (distanceToInit < dest.minDistance) {
                    routerQ.remove(dest);
                    dest.minDistance = distanceToInit;
                    //System.out.println("The dest new min distance is " + dest.minDistance);
                    this.costMatrix[link.start.intIdentifier][dest.intIdentifier] = dest.minDistance;
                    this.costMatrix[dest.intIdentifier][link.start.intIdentifier] = dest.minDistance;
                    //System.out.println("CostMatrix[" + initial.intIdentifier +"][" + dest.intIdentifier +"] is" + this.costMatrix[initial.intIdentifier][dest.intIdentifier] );
                    //System.out.println("CostMatrix[" + dest.intIdentifier +"][" + initial.intIdentifier +"] is" + this.costMatrix[dest.intIdentifier][initial.intIdentifier] );
                    dest.predecessor = initial;
                    this.yPrime.add(link);
                    routerQ.add(dest);
                    //System.out.println("Router " + dest + " was added to the Q" );
                    //smallest = link;
                }
            }


            this.p.set(initial.intIdentifier, initial.predecessor == null ? initial : initial.predecessor);
            //this.yPrime.add(findLink(initial, initial.predecessor));
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
            System.out.println("P is");
            for (Router router : this.p) {
                System.out.print(router + " ");
            }

            System.out.println("D is");
            for (Router router : this.routers) {
                System.out.print(router.minDistance + " ");
            }
            System.out.println("The cost matrix is\n");

            for (int i = 0; i < this.routers.size(); i++) {
                for (int j = 0; j < this.routers.size(); j ++) {
                    System.out.print(this.costMatrix[i][j] + " ");
                }
                System.out.println();
            }

            System.out.println();
            for (Router router : this.routers) {
                for (Router router2 : this.routers) {
                    System.out.print(router.minDistance);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("N prime is ");
            for(Router router : this.nPrime) {
                System.out.print(router.identifier + " ");
            }
            System.out.println();
        }
//        for (Router router : this.routers) {
//            System.out.println(router.minDistance + " ");
//        }
//        for (int i = 0; i < this.routers.size(); i++) {
//            for (int j = 0; j < this.routers.size(); j ++) {
//                System.out.print(this.costMatrix[i][j] + " ");
//            }
//            System.out.println();
//        }
        /*source.minDistance =0;
        PriorityQueue<Router> routerQ = new PriorityQueue<Router>();
        routerQ.add(source);
        while (!routerQ.isEmpty()) {
            Router initial = routerQ.poll();
            Router smallest = initial;
        for (Link link : initial.links) {
            Router dest = link.end;
            int cost = link.cost;
            int distanceToInit = initial.minDistance + cost;
            if (distanceToInit < dest.minDistance) {
                routerQ.remove(dest);
                dest.minDistance = distanceToInit;
                //this.costMatrix[link.start.intIdentifier][link.start.intIdentifier] = dest.minDistance;
                //this.costMatrix[link.end.intIdentifier][link.start.intIdentifier] = dest.minDistance;
                dest.predecessor = initial;
                routerQ.add(dest);
                smallest = dest;
            }
        }
            this.nPrime.add(initial);
            this.p.set(initial.intIdentifier, initial.predecessor == null ? initial : initial.predecessor);
            System.out.println("P is ");
            for (Router router : p) {
                System.out.print(router.identifier + " ");
            }
            System.out.println();
            for (int i = 0; i < this.routers.size(); i++) {
                for (int j = 0; j < this.routers.size(); j ++) {
                    System.out.print(this.costMatrix[i][j] + " ");
                }
                System.out.println();
            }

            System.out.println();
            for (Router router : this.routers) {
                for (Router router2 : this.routers) {
                    System.out.print(router.minDistance);
                }
                System.out.println();
             }
            System.out.println();
            System.out.println("N prime is ");
            for(Router router : this.nPrime) {
                System.out.print(router.identifier + " ");
            }
            System.out.println();
        }*/
    }

    private void printShortestPathTree() {
        for(Router router : this.routers) {
            System.out.println("Distance to " + router + ": " + router.minDistance);
            ArrayList<Router> path = findPath(router, new ArrayList<Router>());//shortestPathToDest(router);
            System.out.println("Path: " + path);
        }
    }
    private final void initialize () {
        this.nPrime = new HashSet<Router>();
        this.yPrime = new ArrayList<Link>();
        this.nPrime.add(this.routers.get(0));
        for(Router router : this.routers) {
            for (Link link : this.routers.get(0).links) {
                this.costMatrix[link.start.intIdentifier][link.end.intIdentifier] = link.cost;
                this.costMatrix[link.end.intIdentifier][link.start.intIdentifier] = link.cost;
                this.routers.get(link.end.intIdentifier).predecessor = this.routers.get(0);
            }
        }
        //investigator();
    }

    /*private final ArrayList<Router> generateRouters (final int numRouters) {
        ArrayList<Router> temp = new ArrayList<Router>(numRouters);
        for (int i = 0; i < numRouters; i++) {
            temp.add(i,  new Router ("V" + i, determineAdjacentLinks(i), i));
        }
        return temp;
    }*/

    /*private final HashSet<Link> determineAdjacentLinks(int index) {
        HashSet<Link> temp = new HashSet<Link>();
        for (Link link : this.links) {
            if (link.start == index) {
                temp.add(link);
            }
        }
        return temp;
    }*/

    private ArrayList<Router> adjacentRouters (Router r) {
        ArrayList<Router> temp = new ArrayList<Router>();
        for (Link link : r.links) {
            temp.add(this.routers.get(link.end.intIdentifier));
        }
        return temp;
    }

    private final ArrayList<Router> notInNprime(final int srcNode) {
        ArrayList<Router> validKs = new ArrayList<Router>();
        for (Router router : this.routers) {
            if (!this.nPrime.contains(router)) {
                validKs.add(router);
            }
        }
        return validKs;
    }

    private final Link findMinimumLink (final ArrayList<Router> possibleKs) {
        Link temp = new Link();
        for (Router router : possibleKs) {
            for (Link link : router.links) {
                if (link.cost <= temp.cost) {
                    temp = link;
                }
            }
        }
        //System.out.println("The link from min link is " + temp.start + " end: " +temp.end + " cost " + temp.cost);
        return temp;
    }


    /*private final Dijkstra loop () {
        //System.out.println("Inside loop");
        Link minLink;
        Router destRouter;
        for (Router router : this.routers) {
            //System.out.println("Inside loop2");
            ArrayList<Router> candidates = findPossibleKs(router.intIdentifier);
            if (!candidates.isEmpty()) {
                minLink = findMinimumLink(candidates);
                destRouter = this.routers.get(minLink.end);
                destRouter.predecessor = this.routers.get(minLink.start);
                System.out.println("The predecessor of destrouter" + destRouter.identifier + "is " + destRouter.predecessor.identifier);
                this.nPrime.add(destRouter);
                this.yPrime.add(minLink);
                update(destRouter);
                //this.costMatrix[minLink.start][minLink.end] = minLink.cost;
                //this.costMatrix[minLink.end][minLink.start] = minLink.cost;
                investigator();
            }
        }
        return this;
    } */
    /*private void update (Router k) {
        System.out.println("Inside update and k is " +k.identifier);
        HashSet<Link> updating = determineAdjacentLinks(k.intIdentifier);
        for (Link link : updating) {
            System.out.println("Inside update for");
            if (!this.nPrime.contains(this.routers.get(link.end))) {
                System.out.println("Inside update if1");
                int newCost = this.costMatrix[0][k.intIdentifier] + this.costMatrix[link.start][link.end];
                System.out.println("The new cost is " + newCost);
                if (newCost < this.costMatrix[0][link.start]) {
                    System.out.println("Inside update if2");
                    this.costMatrix[0][link.start] = newCost;
                    this.routers.get(link.start).predecessor = k;
                }
            }
        }
    } */

    /*private void investigator () {
        String data = "";
        data += "N' is ";
        for (Router router : this.nPrime) {
            data += router.identifier + " ";
        }

        data += "\nY' is ";
        for (Link link : this.yPrime) {
            data += "Source: " + this.routers.get(link.start).identifier + " Dest: " + this.routers.get(link.end).identifier + ", ";
        }

        data += "\nD(i)'s:\n";
        for (int i = 0; i < routers.size(); i++){
            for (int j = 0; j < routers.size(); j++) {
                data += this.costMatrix[i][j] + " ";
            }
            data += "\n";
        }

        data += "\nP(i)'s:\n";
        for (int i=0; i< this.routers.size(); i++) {
            data += this.routers.get(i).predecessor.identifier + " ";
        }
        /*for (Router router : this.nPrime){
            if (!(router.intIdentifier == 0)) {
                data += this.routers.get(router.intIdentifier).identifier + " ";
            }

        }*/
       /* System.out.println(data);
    } */

    public String toString () {
        String object = "";
        for (Router router : this.routers) {
            object +=
                    router.identifier + " (V0" + " " + router.identifier + ")\n";
        }

        return object;
    }
}
