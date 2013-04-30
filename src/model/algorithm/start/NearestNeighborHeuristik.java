package model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import model.grid.Edge;
import model.grid.Node;

public class NearestNeighborHeuristik extends AStartAlgorithm {

	private final Set<Node> nodesToVisit;

	public NearestNeighborHeuristik(Set<Node> nodes, Node startingNode) {
		super(nodes, startingNode);

		this.nodesToVisit = new HashSet<Node>(this.getNodes());

		this.reset();
	}

	@Override
	protected boolean doStep() {
		boolean successfulStep = true;

		if (this.nodesToVisit.size() > 1) {
			Edge shortestEdge = null;

			// Remove the current node from the set of nodes to visit
			this.nodesToVisit.remove(this.getCurrentNode());

			// Get all available edges from the current node
			Set<Edge> edges = this.getCurrentNode().getAccessibleEdges();

			// Get the shortest edge to a node that we still have to visit
			for (Edge edge : edges) {

				// Does this edge lead to a node that we still have to visit?
				boolean validEdge = false;
				for (Node nodeToVisit : this.nodesToVisit) {
					if (edge.getFirstNode() == nodeToVisit || edge.getSecondNode() == nodeToVisit) {
						validEdge = true;
						break;
					}
				}

				// If this edge does not lead to a node we want to visit we will continue with the next one
				if (!validEdge) {
					continue;
				}

				// Check if this edge is the shortest
				if (shortestEdge == null || shortestEdge.getWeight() > edge.getWeight()) {
					shortestEdge = edge;
				}

			}

			if (shortestEdge != null) {
				// Add the new edge to the path
				this.getCurrentPath().addEdge(shortestEdge);

				// Set the new current node
				if (shortestEdge.getFirstNode() == this.getCurrentNode()) {
					this.setCurrentNode(shortestEdge.getSecondNode());
				}
				else {
					this.setCurrentNode(shortestEdge.getFirstNode());
				}
			}
			else {
				System.err.println("No edge found, we stop here...");
				successfulStep = false;
			}
		}
		else {
			// Link the last node with the starting node
			Edge lastEdge = this.getCurrentNode().getEdgeToNode(this.getStartingNode());
			if (lastEdge != null) {
				this.getCurrentPath().addEdge(lastEdge);
				this.setCurrentNode(this.getStartingNode());

				this.setFinishedSuccessful(true);
			}
			else {
				// FIXME: If the last node has no accessible edge to the starting node we fail here
				System.err.println("Jetzt hämmer es Problem...");

				this.setFinishedSuccessful(false);
				successfulStep = false;
			}
		}

		return successfulStep;
	}

	@Override
	protected void reset() {
		super.reset();

		this.nodesToVisit.clear();
		this.nodesToVisit.addAll(this.getNodes());
		this.setCurrentNode(this.getStartingNode());
	}
}
