package tspsolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tspsolver.controller.AlgorithmRunner;
import tspsolver.controller.Controller;
import tspsolver.controller.scenario.ScenarioLoader;
import tspsolver.controller.scenario.xml.XMLScenarioLoader;
import tspsolver.model.algorithm.OptimizerAlgorithm;
import tspsolver.model.algorithm.StartAlgorithm;
import tspsolver.model.algorithm.optimizer.LinKernighanHeuristic;
import tspsolver.model.algorithm.optimizer.TwoOptHeuristic;
import tspsolver.model.algorithm.start.BruteForceAlgorithm;
import tspsolver.model.algorithm.start.MinimumSpanningTreeHeuristic;
import tspsolver.model.algorithm.start.NearestNeighborHeuristic;
import tspsolver.model.algorithm.start.RandomHeuristic;
import tspsolver.model.scenario.Scenario;
import tspsolver.model.validator.TSPValidator;
import tspsolver.model.validator.Validator;
import tspsolver.view.MainFrame;

public class Launcher {

	public static final int NUMBER_OF_ALGORITHM_RUNNERS = 2;
	public static final String SCENARIO_DIRECTORY = "data/scenario";

	public static void main(String[] args) {
		ScenarioLoader scenarioLoader = new XMLScenarioLoader();

		// Check if the scenario directory exits
		File scenarioDirectory = new File(Launcher.SCENARIO_DIRECTORY);
		if (!scenarioDirectory.exists() && !scenarioDirectory.isDirectory()) {
			System.err.println("Scenario directory does not exit: " + Launcher.SCENARIO_DIRECTORY);
			System.exit(1);
		}

		// Create the validator
		Validator validator = new TSPValidator();

		// Create the scenarios
		List<Scenario> scenarioList = new ArrayList<Scenario>();
		for (File scenarioFile : scenarioDirectory.listFiles()) {
			if (scenarioFile.isDirectory()) {
				continue;
			}
			try {
				InputStream inputStream = new FileInputStream(scenarioFile);
				Scenario scenario = new Scenario(validator);

				scenarioLoader.loadScenario(scenario, inputStream);
				scenarioList.add(scenario);
			}
			catch (IllegalArgumentException exception) {
				exception.printStackTrace();
			}
			catch (FileNotFoundException exception) {
				exception.printStackTrace();
			}
		}
		Scenario[] scenarios = scenarioList.toArray(new Scenario[scenarioList.size()]);

		// Create the algorithm runners
		AlgorithmRunner[] algorithmRunners = new AlgorithmRunner[Launcher.NUMBER_OF_ALGORITHM_RUNNERS];
		for (int i = 0; i < Launcher.NUMBER_OF_ALGORITHM_RUNNERS; i++) {
			// Create the start-algorithms
			StartAlgorithm[] startAlgorithms = new StartAlgorithm[4];
			startAlgorithms[0] = new BruteForceAlgorithm();
			startAlgorithms[1] = new NearestNeighborHeuristic();
			startAlgorithms[2] = new MinimumSpanningTreeHeuristic();
			startAlgorithms[3] = new RandomHeuristic();

			// Create the optimizer-algorithms
			OptimizerAlgorithm[] optimizerAlgorithms = new OptimizerAlgorithm[2];
			optimizerAlgorithms[0] = new TwoOptHeuristic();
			optimizerAlgorithms[1] = new LinKernighanHeuristic();

			algorithmRunners[i] = new AlgorithmRunner(startAlgorithms, optimizerAlgorithms);
		}

		// Create the main runner
		Controller controller = new Controller(scenarios, algorithmRunners);

		// Create the main view
		MainFrame mainFrame = new MainFrame(controller);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
}
