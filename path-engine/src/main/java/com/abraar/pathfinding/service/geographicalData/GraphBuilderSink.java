package com.abraar.pathfinding.service.geographicalData;

import com.abraar.pathfinding.dto.Graph;
import com.abraar.pathfinding.dto.GraphEdge;
import com.abraar.pathfinding.dto.GraphNode;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphBuilderSink implements Sink {
    private Graph graph;
    private Map<Long, Node> temporaryNodes; // Temporary storage for nodes to build ways

    public GraphBuilderSink(Graph graph) {
        this.graph = graph;
        this.temporaryNodes = new HashMap<>();
    }

    @Override
    public void initialize(Map<String, Object> metaData) {
        // This is called before the data starts streaming
    }

    @Override
    public void process(EntityContainer entityContainer) {
        Entity entity = entityContainer.getEntity();
        if (entity instanceof Node) {
            Node node = (Node) entity;
            // Store all nodes temporarily
            temporaryNodes.put(node.getId(), node);
        } else if (entity instanceof Way) {
            Way way = (Way) entity;
            // You should filter for relevant ways here (e.g., highway=* tag)
            if (way.getTags().stream().anyMatch(t -> t.getKey().equals("highway"))) {
                // Process the way to add edges to your graph
                List<WayNode> wayNodes = way.getWayNodes();
                for (int i = 0; i < wayNodes.size() - 1; i++) {
                    WayNode fromWayNode = wayNodes.get(i);
                    WayNode toWayNode = wayNodes.get(i + 1);
                    
                    // You'll need to retrieve the actual Node objects from temporary storage
                    Node fromNode = temporaryNodes.get(fromWayNode.getNodeId());
                    Node toNode = temporaryNodes.get(toWayNode.getNodeId());

                    if (fromNode != null && toNode != null) {
                        // Create and add your custom GraphNode and GraphEdge objects
                        graph.addNode(new GraphNode(fromNode.getId(), fromNode.getLatitude(), fromNode.getLongitude()));
                        graph.addNode(new GraphNode(toNode.getId(), toNode.getLatitude(), toNode.getLongitude()));
                        graph.addEdge(new GraphEdge(way.getId(), fromNode.getId(), toNode.getId()));
                    }
                }
            }
        }
    }

    @Override
    public void complete() {
        // This is called after all data has been processed.
        // The temporary nodes can now be discarded.
        temporaryNodes.clear();
        System.out.println("Graph building complete.");
    }

    @Override
    public void close() {
        // Clean up resources if necessary.
    }
}