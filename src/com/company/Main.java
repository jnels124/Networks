package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
	    try {
            int numRouters =
                    Integer.parseInt(messageResponse("Please enter the number of routers you wish to use "));
            String fileName = messageResponse("Please enter the path of the file");
            ArrayList<Router> routers = generateRouters(numRouters);
            routers = readLinkInfo(fileName, routers);
            System.out.println("The forwarding table is " + new Dijkstra (numRouters, routers).toString());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String messageResponse(String msg) throws IOException{
        System.out.println(msg);
        return (reader.readLine());
    }

    private static final ArrayList<Router> readLinkInfo (String file, ArrayList<Router> routers) {
        String line;
        StringTokenizer st;
        //ArrayList<Router> temp = new ArrayList <Router> ();
        int start=0, end=0, cost=0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line);
                start = Integer.parseInt(st.nextToken());
                end = Integer.parseInt(st.nextToken());
                cost = Integer.parseInt(st.nextToken());
                Link newLink = new Link (routers.get(start),
                        routers.get(end), cost);
                //temp.add(newLink);
                routers.get(start).links.add(newLink);
                routers.get(end).links.add(newLink);
                System.out.println ("The start is " + start + " The end is " + end + "The cost is " + cost);
            }

        }

        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        return routers;
    }

    private static final ArrayList<Router> generateRouters (final int numRouters) {
        ArrayList<Router> temp = new ArrayList<Router>(numRouters);
        for (int i = 0; i < numRouters; i++) {
            temp.add(i,  new Router ("V" + i, i));
        }
        return temp;
    }
}
