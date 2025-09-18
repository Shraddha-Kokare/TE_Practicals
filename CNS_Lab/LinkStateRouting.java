import java.util.*;

class Network {
    private Map<String, Map<String, Integer>> graph = new HashMap<>();

    // Add bidirectional link between routers
    public void addLink(String u, String v, int cost) {
        graph.putIfAbsent(u, new HashMap<>());
        graph.putIfAbsent(v, new HashMap<>());
        graph.get(u).put(v, cost);
        graph.get(v).put(u, cost);
    }

    // Dijkstra’s algorithm
    public Map<String, Integer> dijkstra(String source, Map<String, String> prev) {
        Map<String, Integer> dist = new HashMap<>();
        for (String node : graph.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }
        dist.put(source, 0);

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                        return Integer.compare(e1.getValue(), e2.getValue());
                    }
                });

        pq.add(new AbstractMap.SimpleEntry<>(source, 0));

        while (!pq.isEmpty()) {
            String u = pq.poll().getKey();
            int d = dist.get(u);

            for (Map.Entry<String, Integer> neighbor : graph.get(u).entrySet()) {
                String v = neighbor.getKey();
                int cost = neighbor.getValue();

                if (d + cost < dist.get(v)) {
                    dist.put(v, d + cost);
                    prev.put(v, u);
                    pq.add(new AbstractMap.SimpleEntry<>(v, dist.get(v)));
                }
            }
        }
        return dist;
    }

    // Reconstruct shortest path from src to dest
    public List<String> shortestPath(String src, String dest) {
        Map<String, String> prev = new HashMap<>();
        Map<String, Integer> dist = dijkstra(src, prev);

        List<String> path = new ArrayList<>();
        String node = dest;
        while (node != null) {
            path.add(node);
            node = prev.get(node);
        }
        Collections.reverse(path);

        System.out.println("Shortest path from " + src + " to " + dest +
                ": " + path + " with cost " + dist.get(dest));
        return path;
    }

    public void printAllShortestPaths(String source) {
        Map<String, String> prev = new HashMap<>();
        Map<String, Integer> dist = dijkstra(source, prev);

        System.out.println("\nShortest distances from " + source + ":");
        for (String node : dist.keySet()) {
            System.out.println(source + " -> " + node + " = " + dist.get(node));
        }
    }
}

public class LinkStateRouting {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Network net = new Network();

        System.out.print("Enter number of links in the network: ");
        int links = sc.nextInt();

        System.out.println("Enter links in format: <Router1> <Router2> <Cost>");
        for (int i = 0; i < links; i++) {
            String u = sc.next();
            String v = sc.next();
            int cost = sc.nextInt();
            net.addLink(u, v, cost);
        }

        System.out.print("\nEnter source router: ");
        String source = sc.next();

        net.printAllShortestPaths(source);

        System.out.print("\nEnter destination router to find path: ");
        String dest = sc.next();

        net.shortestPath(source, dest);
        sc.close();
    }
}
