package tspsolver.model.scenario.grid;

import java.io.Serializable;
import java.util.Observable;

public class Edge extends Observable implements Serializable {

	private static final long serialVersionUID = -1032951953663147952L;

	private final Vertex firstVertex;
	private final Vertex secondVertex;
	private final int weight;

	protected Edge(Vertex firstVertex, Vertex secondVertex, int weight) throws IllegalArgumentException {
		if (firstVertex.equals(secondVertex)) {
			throw new IllegalArgumentException("The vertices must not be equal");
		}

		this.firstVertex = firstVertex;
		this.secondVertex = secondVertex;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;

		result = prime * result + ((this.firstVertex == null) ? 0 : this.firstVertex.hashCode());
		result = prime * result + ((this.secondVertex == null) ? 0 : this.secondVertex.hashCode());
		result = prime * result + this.weight;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		Edge other = (Edge) obj;

		if (this.weight != other.weight) {
			return false;
		}

		if (this.firstVertex == null) {
			if (other.firstVertex != null) {
				return false;
			}
		}
		else if (!this.firstVertex.equals(other.firstVertex) && !this.firstVertex.equals(other.secondVertex)) {
			return false;
		}

		if (this.secondVertex == null) {
			if (other.secondVertex != null) {
				return false;
			}
		}
		else if (!this.secondVertex.equals(other.secondVertex) && !this.secondVertex.equals(other.firstVertex)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.getFirstVertex());
		builder.append(" -> ");
		builder.append(this.getSecondVertex());

		return builder.toString();
	}

	public Vertex getFirstVertex() {
		return this.firstVertex;
	}

	public Vertex getSecondVertex() {
		return this.secondVertex;
	}

	public int getWeight() {
		return this.weight;
	}

	protected static int calcLinearDistance(Vertex firstVertex, Vertex secondVertex) {
		int deltaX = secondVertex.getX() - firstVertex.getX();
		int deltaY = secondVertex.getY() - firstVertex.getY();

		// Make deltaX positive
		if (deltaX < 0) {
			deltaX *= -1;
		}

		// Make deltaY positive
		if (deltaY < 0) {
			deltaY *= -1;
		}

		// Calculate the linear distance and round it to an integer
		double linearDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		int returnValue = (int) (linearDistance + 0.5);

		return returnValue;
	}

}
