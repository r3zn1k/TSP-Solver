package tspsolver.model.algorithm.start;

import junit.framework.Assert;

import org.junit.Before;

import tspsolver.model.algorithm.StartAlgorithmTest;
import tspsolver.model.scenario.grid.Edge;
import tspsolver.model.scenario.grid.GridFactory;
import tspsolver.model.scenario.grid.Vertex;

public class MinimumSpanningTreeHeuristikTest extends StartAlgorithmTest {

	@Override
	@Before
	public void setUp() {
		this.algorithm = new MinimumSpanningTreeHeuristik();
		super.setUp();
	}

	@Override
	protected void doTestScenarioNorthEastSouthWest() {
		Vertex vertexNorth = GridFactory.getVertex(this.grid, "north");
		Vertex vertexEast = GridFactory.getVertex(this.grid, "east");
		Vertex vertexSouth = GridFactory.getVertex(this.grid, "south");
		Vertex vertexWest = GridFactory.getVertex(this.grid, "west");

		Edge edgeNorthEast = GridFactory.getEdge(vertexNorth, vertexEast);
		Edge edgeNorthSouth = GridFactory.getEdge(vertexNorth, vertexSouth);
		Edge edgeNorthWest = GridFactory.getEdge(vertexNorth, vertexWest);
		Edge edgeEastSouth = GridFactory.getEdge(vertexEast, vertexSouth);
		Edge edgeEastWest = GridFactory.getEdge(vertexEast, vertexWest);
		Edge edgeSouthWest = GridFactory.getEdge(vertexSouth, vertexWest);

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have four edges
		Assert.assertEquals(4, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(edgeNorthSouth));
		Assert.assertTrue(this.path.containsEdge(edgeNorthWest));
		Assert.assertTrue(this.path.containsEdge(edgeEastSouth));
		Assert.assertTrue(this.path.containsEdge(edgeEastWest));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edgeNorthEast));
		Assert.assertFalse(this.path.containsEdge(edgeSouthWest));

		// Check if the path is valid
		Assert.assertTrue(this.scenarioNorthEastSouthWest.isPathValid());
	}

	@Override
	protected void doTestScenarioFiveVertices() {
		Vertex vertex1 = GridFactory.getVertex(this.grid, "1");
		Vertex vertex2 = GridFactory.getVertex(this.grid, "2");
		Vertex vertex3 = GridFactory.getVertex(this.grid, "3");
		Vertex vertex4 = GridFactory.getVertex(this.grid, "4");
		Vertex vertex5 = GridFactory.getVertex(this.grid, "5");

		Edge edge12 = GridFactory.getEdge(vertex1, vertex2);
		Edge edge13 = GridFactory.getEdge(vertex1, vertex3);
		Edge edge14 = GridFactory.getEdge(vertex1, vertex4);
		Edge edge15 = GridFactory.getEdge(vertex1, vertex5);
		Edge edge23 = GridFactory.getEdge(vertex2, vertex3);
		Edge edge24 = GridFactory.getEdge(vertex2, vertex4);
		Edge edge25 = GridFactory.getEdge(vertex2, vertex5);
		Edge edge34 = GridFactory.getEdge(vertex3, vertex4);
		Edge edge35 = GridFactory.getEdge(vertex3, vertex5);
		Edge edge45 = GridFactory.getEdge(vertex4, vertex5);

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have five edges
		Assert.assertEquals(5, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(edge12));
		Assert.assertTrue(this.path.containsEdge(edge25));
		Assert.assertTrue(this.path.containsEdge(edge35));
		Assert.assertTrue(this.path.containsEdge(edge34));
		Assert.assertTrue(this.path.containsEdge(edge14));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edge13));
		Assert.assertFalse(this.path.containsEdge(edge15));
		Assert.assertFalse(this.path.containsEdge(edge23));
		Assert.assertFalse(this.path.containsEdge(edge24));
		Assert.assertFalse(this.path.containsEdge(edge45));

		// Check if the path is valid
		Assert.assertTrue(this.scenarioFiveVertices.isPathValid());
	}

	@Override
	protected void doTestScenarioFiveVerticesOneNonAccessibleEdge() {
		Vertex vertex1 = GridFactory.getVertex(this.grid, "1");
		Vertex vertex2 = GridFactory.getVertex(this.grid, "2");
		Vertex vertex3 = GridFactory.getVertex(this.grid, "3");
		Vertex vertex4 = GridFactory.getVertex(this.grid, "4");
		Vertex vertex5 = GridFactory.getVertex(this.grid, "5");

		Edge edge12 = GridFactory.getEdge(vertex1, vertex2);
		Edge edge13 = GridFactory.getEdge(vertex1, vertex3);
		Edge edge14 = GridFactory.getEdge(vertex1, vertex4);
		Edge edge15 = GridFactory.getEdge(vertex1, vertex5);
		Edge edge23 = GridFactory.getEdge(vertex2, vertex3);
		Edge edge24 = GridFactory.getEdge(vertex2, vertex4);
		Edge edge25 = GridFactory.getEdge(vertex2, vertex5);
		Edge edge34 = GridFactory.getEdge(vertex3, vertex4);
		Edge edge35 = GridFactory.getEdge(vertex3, vertex5);
		Edge edge45 = GridFactory.getEdge(vertex4, vertex5);

		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Check if we have five edges
		Assert.assertEquals(5, this.path.getNumberOfEdges());

		// Check if we went the right path
		Assert.assertTrue(this.path.containsEdge(edge15));
		Assert.assertTrue(this.path.containsEdge(edge25));
		Assert.assertTrue(this.path.containsEdge(edge23));
		Assert.assertTrue(this.path.containsEdge(edge34));
		Assert.assertTrue(this.path.containsEdge(edge14));

		// Check if these edges are not part of the path
		Assert.assertFalse(this.path.containsEdge(edge12));
		Assert.assertFalse(this.path.containsEdge(edge13));
		Assert.assertFalse(this.path.containsEdge(edge24));
		Assert.assertFalse(this.path.containsEdge(edge35));
		Assert.assertFalse(this.path.containsEdge(edge45));

		// Check if the path is valid
		Assert.assertTrue(this.scenarioFiveVerticesOneNonAccessibleEdge.isPathValid());
	}

	@Override
	protected void doTestScenarioUnsolvable() {
		// TODO: implement this test
		Assert.fail();

		// Check if the path is invalid
		Assert.assertFalse(this.scenarioUnsolvable.isPathValid());
	}

	@Override
	protected void doTestScenarioFortyoneVertex() {
		// Step through the algorithm
		do {
			Assert.assertTrue(this.algorithm.step());
		} while (!this.algorithm.hasFinishedSuccessfully());

		// Make sure that we can not take an other step
		Assert.assertFalse(this.algorithm.step());

		// Check if the path is valid
		Assert.assertTrue(this.scenarioFortyoneVertices.isPathValid());
	}

}
