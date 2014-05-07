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
            int srcRouter =
                    Integer.parseInt(messageResponse("Please provide the src node (0 - (n-1))"));

            while (numRouters < 2)
                numRouters = Integer.parseInt(messageResponse("Please enter the number of routers you wish to use "));
            String fileName = messageResponse("Please enter the path of the file");
            ArrayList<Router> routers = generateRouters(numRouters);
            routers = readLinkInfo(fileName, routers);
            System.out.println("The forwarding table is " + new Dijkstra (srcRouter, routers).toString());
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
        int start=0, end=0, cost=0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int lineNum =0;
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line);
                start = Integer.parseInt(st.nextToken());
                end = Integer.parseInt(st.nextToken());
                cost = Integer.parseInt(st.nextToken());

                boolean validLink = (start != end && start >= 0 && start <= routers.size());
                validLink = (validLink && end >= 0 && end <= routers.size());
                if(!validLink) {
                    System.out.println("Line " + lineNum + " has invalid put " + start + " " + end + " " + cost);
                    reader.close();
                    System.exit(0);
                }

                Link newLink = new Link (routers.get(start), routers.get(end), cost);
                routers.get(start).links.add(newLink);
                Link swap = new Link(newLink.end, newLink.start, newLink.cost);
                routers.get(end).links.add(swap);
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
