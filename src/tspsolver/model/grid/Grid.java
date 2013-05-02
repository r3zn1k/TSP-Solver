package tspsolver.model.grid;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import tspsolver.model.grid.updates.NodeUpdate;
import tspsolver.model.grid.updates.StartingNodeUpdate;
import tspsolver.model.grid.updates.UpdateAction;

public class Grid extends Observable implements Observer {

	public static final boolean LINK_ADDED_NODE = true;

	private final Set<Node> nodes;
	private Node startingNode;

	public Grid() {
		this.nodes = new HashSet<Node>();
	}

	@Override
	public void update(Observable observable, Object argument) {
		this.setChanged();
		this.notifyObservers(argument);
	}

	public Set<Node> getNodes() {
		return this.nodes;
	}

	public boolean containsNode(Node node) {
		return this.nodes.contains(node);
	}

	public synchronized Node addNode(Node node) {
		return this.addNode(node, Grid.LINK_ADDED_NODE);
	}

	public synchronized Node addNode(Node node, boolean link) {
		if (!this.containsNode(node)) {
			if (link) {
				for (Node n : this.getNodes()) {
					n.addEdgeToNode(node);
				}
			}
			node.addObserver(this);

			this.nodes.add(node);
			this.fireNodeUpdate(node, UpdateAction.ADD);
		}

		return node;
	}

	public synchronized void removeNode(Node node) {
		if (this.containsNode(node)) {
			for (Node n : this.getNodes()) {
				n.removeEdgeToNode(node);
			}
			node.deleteObserver(this);

			this.nodes.remove(node);
			this.fireNodeUpdate(node, UpdateAction.REMOVE);
		}
	}

	public synchronized void clearNodes() {
		for (Node node : this.getNodes()) {
			this.removeNode(node);
		}
	}

	public Node getStartingNode() {
		return this.startingNode;
	}

	public void setStartingNode(Node startingNode) {
		if (this.startingNode != null) {
			this.fireStartingNodeUpdate(this.startingNode, UpdateAction.REMOVE);
		}

		this.startingNode = startingNode;
		this.fireStartingNodeUpdate(this.startingNode, UpdateAction.ADD);
	}

	private void fireNodeUpdate(Node node, UpdateAction action) {
		NodeUpdate update = new NodeUpdate(node, action);

		this.setChanged();
		this.notifyObservers(update);
	}

	private void fireStartingNodeUpdate(Node node, UpdateAction action) {
		NodeUpdate update = new StartingNodeUpdate(node, action);

		this.setChanged();
		this.notifyObservers(update);
	}

}
