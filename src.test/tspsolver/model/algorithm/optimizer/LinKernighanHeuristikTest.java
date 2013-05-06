package tspsolver.model.algorithm.optimizer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import tspsolver.model.Scenario;
import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.grid.Edge;
import tspsolver.model.grid.Grid;
import tspsolver.model.grid.GridFactory;
import tspsolver.model.grid.Node;
import tspsolver.model.path.Path;
import tspsolver.model.path.PathUpdater;

public class LinKernighanHeuristikTest {

	private Scenario scenario;
	private Grid grid;
	private Path path;

	private Node nodeNorth;
	private Node nodeEast;
	private Node nodeSouth;
	private Node nodeWest;

	private Edge edgeNorthEast;
	private Edge edgeNorthSouth;
	private Edge edgeNorthWest;
	private Edge edgeEastSouth;
	private Edge edgeEastWest;
	private Edge edgeSouthWest;

	private OptimizerAlgorithm algorithm;

	@Before
	public void setUp() {
		this.scenario = new Scenario("LinKernighanHeuristikTest");
		this.grid = scenario.getGrid();
		this.path = scenario.getPath();

		this.nodeNorth = GridFactory.createNode(3, 1);
		this.nodeEast = GridFactory.createNode(4, 2);
		this.nodeSouth = GridFactory.createNode(1, 3);
		this.nodeWest = GridFactory.createNode(2, 4);

		GridFactory.addNode(this.grid, this.nodeNorth);
		GridFactory.addNode(this.grid, this.nodeEast);
		GridFactory.addNode(this.grid, this.nodeSouth);
		GridFactory.addNode(this.grid, this.nodeWest);

		this.scenario.setStartingNode(nodeNorth);

		this.edgeNorthEast = GridFactory.getEdge(this.nodeNorth, this.nodeEast);
		this.edgeNorthSouth = GridFactory.getEdge(this.nodeNorth, this.nodeSouth);
		this.edgeNorthWest = GridFactory.getEdge(this.nodeNorth, this.nodeWest);
		this.edgeEastSouth = GridFactory.getEdge(this.nodeEast, this.nodeSouth);
		this.edgeEastWest = GridFactory.getEdge(this.nodeEast, this.nodeWest);
		this.edgeSouthWest = GridFactory.getEdge(this.nodeSouth, this.nodeWest);

		this.algorithm = new LinKernighanHeuristik();
	}

	@Test
	public void test() {
		// Create a valid path that is too heavy
		PathUpdater pathUpdater = new PathUpdater(this.path);
		pathUpdater.addEdge(this.edgeNorthSouth);
		pathUpdater.addEdge(this.edgeSouthWest);
		pathUpdater.addEdge(this.edgeEastWest);
		pathUpdater.addEdge(this.edgeNorthEast);
		pathUpdater.updatePath();

		// Make sure that we can not take a step yet
		Assert.assertFalse(this.algorithm.step());

		// Initialize the algorithm
		this.algorithm.initialize(this.scenario);
		Assert.assertTrue(this.algorithm.hasValidArguments());

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have four edges
		Assert.assertEquals(4, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(this.edgeNorthEast));
		Assert.assertTrue(this.path.containsEdge(this.edgeEastSouth));
		Assert.assertTrue(this.path.containsEdge(this.edgeSouthWest));
		Assert.assertTrue(this.path.containsEdge(this.edgeNorthWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(this.edgeNorthSouth));
		Assert.assertFalse(this.path.containsEdge(this.edgeEastWest));
	}
}
