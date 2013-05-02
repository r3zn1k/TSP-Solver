package tspsolver.model.algorithm.start;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.algorithm.Path;
import tspsolver.model.algorithm.start.AStartAlgorithm;
import tspsolver.model.algorithm.start.RandomAlgorithm;
import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.GridFactory;
import tspsolver.model.grid.Node;

public class RandomAlgorithmTest {

	private Grid grid;

	private Node nodeNorth;
	private Node nodeEast;
	private Node nodeSouth;
	private Node nodeWest;

	private Set<Node> nodes;

	private AStartAlgorithm algorithm;

	@Before
	public void setUp() {
		this.grid = new Grid();

		this.nodeNorth = GridFactory.createNode(0, 5);
		this.nodeEast = GridFactory.createNode(5, 0);
		this.nodeSouth = GridFactory.createNode(0, -5);
		this.nodeWest = GridFactory.createNode(-5, 0);

		this.nodes = new HashSet<Node>();
		this.nodes.add(this.nodeNorth);
		this.nodes.add(this.nodeEast);
		this.nodes.add(this.nodeSouth);
		this.nodes.add(this.nodeWest);

		this.grid.addNode(nodeNorth);
		this.grid.addNode(nodeEast);
		this.grid.addNode(nodeSouth);
		this.grid.addNode(nodeWest);
		this.grid.setStartingNode(nodeNorth);

		this.algorithm = new RandomAlgorithm(this.grid);
	}

	@Test
	public void test() {
		Path currentPath = this.algorithm.getCurrentPath();

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());

		// Validate the arguments
		this.algorithm.validateArguments();
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessful());

		// DEBUG OUTPUT
		System.out.println("Path: ");
		for (Edge edge : currentPath.getEdges()) {
			Node n1 = edge.getFirstNode();
			Node n2 = edge.getSecondNode();
			System.out.println("(" + n1.getX() + "," + n1.getY() + ")" + " -> " + "(" + n2.getX() + "," + n2.getY() + ")");
		}
		System.out.println();

		// Check if we have four edges
		Assert.assertEquals(4, currentPath.getNumberOfEdges());
	}
}