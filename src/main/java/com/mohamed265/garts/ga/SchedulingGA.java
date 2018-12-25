package com.mohamed265.garts.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mohamed265.garts.pojo.GAChromosome;
import com.mohamed265.garts.pojo.GAProcessor;
import com.mohamed265.garts.pojo.GATask;
import com.mohamed265.garts.utils.statics.Factory;
import com.mohamed265.garts.utils.statics.Statics;

public class SchedulingGA {

	public final static int POPULATION_SIZE = 100;

	public final static int NUM_OF_GENERATIONS = 5;

	public final static int A = 1;

	public final static float CROSS_OVER_PROB = 0.8f;

	public final static float HALF_PROP = 0.5f;

	public final static float MUTATION_PROP = 0.02f;

	public final static Random rand = new Random();

	private List<GAChromosome> getInitPopulation() {

		List<GAChromosome> initPopulation = new ArrayList<>();

		for (int i = 0; i < POPULATION_SIZE; i++) {
			GAChromosome gaChromosome = Factory.generateChromosome();
			gaChromosome.calculateFitnessValue(A);
			initPopulation.add(gaChromosome);
		}

		return initPopulation;
	}

	public GAChromosome startGA() {

		List<GAChromosome> population = getInitPopulation();

		for (int i = 0; i < NUM_OF_GENERATIONS; i++) {

			if (rand.nextFloat() < CROSS_OVER_PROB) {

				int parent1 = getBestTournamentSelection(population);

				int parent2 = getBestTournamentSelection(population);

				GAChromosome offspring1 = new GAChromosome();

				GAChromosome offspring2 = new GAChromosome();

				performCrossover(population.get(parent1), population.get(parent2), offspring1, offspring2);

				offspring1.calculateFitnessValue(A);

				offspring2.calculateFitnessValue(A);

				int worst = getWorstTournamentSelection(population);

				if (population.get(worst).getFitnessValue() < offspring1.getFitnessValue()) {

					population.set(worst, offspring1);

					worst = getWorstTournamentSelection(population);
				}

				if (population.get(worst).getFitnessValue() < offspring2.getFitnessValue()) {

					population.set(worst, offspring2);
				}

			} else {
				int parent1 = getBestTournamentSelection(population);

				GAChromosome offspring = new GAChromosome();

				performMutation(population.get(parent1), offspring);

				offspring.calculateFitnessValue(A);

				int worst = getWorstTournamentSelection(population);

				if (population.get(worst).getFitnessValue() < offspring.getFitnessValue()) {
					population.set(worst, offspring);
				}

			}
		}

		Statics.waitingTasks.stream().forEach(task -> task.setTaskToScheduled());

		int solution = getBestTournamentSelection(population);

		System.out.println("the new schedule is");
		for (int i = 0; i < population.get(solution).getProcessors().length; i++) {

			GAProcessor gaProcessor = population.get(solution).getProcessors()[i];

			GATask gaTask = population.get(solution).getTasks()[i];

			System.out.println(
					"\tTask: " + gaTask.getTaskId() + ", scheduled on processor: " + gaProcessor.getProcessorId());
		}

		return population.get(solution);
	}

	private void performMutation(GAChromosome parent, GAChromosome offspring) {

		GAProcessor[] offspringMapping = new GAProcessor[parent.getProcessors().length];

		System.arraycopy(parent.getProcessors(), 0, offspringMapping, 0, parent.getProcessors().length);

		GATask[] offspringTasks = new GATask[parent.getTasks().length];

		System.arraycopy(parent.getTasks(), 0, offspringTasks, 0, parent.getTasks().length);

		for (int i = 0; i < offspringMapping.length; i++) {

			if (rand.nextFloat() < MUTATION_PROP) {
				offspringMapping[i] = Statics.gaProcessors.get(rand.nextInt(Statics.numOfProcessors));
			}
		}

		offspring.setProcessors(offspringMapping);

		offspring.setTasks(offspringTasks);
	}

	private void performCrossover(GAChromosome parent1, GAChromosome parent2, GAChromosome offspring1,
			GAChromosome offspring2) {
		if (rand.nextFloat() < HALF_PROP) {
			int crossOverPoint = rand.nextInt(parent1.getProcessors().length) + 1;

			GAProcessor[] offspringMapping1 = new GAProcessor[parent1.getProcessors().length];

			GAProcessor[] offspringMapping2 = new GAProcessor[parent1.getProcessors().length];

			System.arraycopy(parent1.getProcessors(), 0, offspringMapping1, 0, crossOverPoint);
			System.arraycopy(parent2.getProcessors(), crossOverPoint, offspringMapping1, crossOverPoint,
					parent1.getProcessors().length - crossOverPoint);

			System.arraycopy(parent2.getProcessors(), 0, offspringMapping2, 0, crossOverPoint);
			System.arraycopy(parent1.getProcessors(), crossOverPoint, offspringMapping2, crossOverPoint,
					parent2.getProcessors().length - crossOverPoint);

			offspring1.setProcessors(offspringMapping1);
			offspring1.setTasks(parent1.getTasks());

			offspring2.setProcessors(offspringMapping2);
			offspring2.setTasks(parent2.getTasks());
		} else {
			int crossOverPoint = rand.nextInt(parent1.getTasks().length) + 1;

			GATask[] offspringTasks1 = new GATask[parent1.getTasks().length];

			System.arraycopy(parent2.getTasks(), 0, offspringTasks1, 0, crossOverPoint);

			for (int i = 0, j = crossOverPoint; i < parent1.getTasks().length; i++) {
				if (!in(offspringTasks1, parent1.getTasks()[i])) {
					offspringTasks1[j] = parent1.getTasks()[i];
					j++;
				}
			}
			GATask[] offspringTasks2 = new GATask[parent2.getTasks().length];

			System.arraycopy(parent1.getTasks(), 0, offspring2, 0, crossOverPoint);

			for (int i = 0, j = crossOverPoint; i < parent2.getTasks().length; i++) {
				if (!in(offspringTasks2, parent2.getTasks()[i])) {
					offspringTasks2[j] = parent2.getTasks()[i];
					j++;
				}
			}

			offspring1.setProcessors(parent1.getProcessors());
			offspring1.setTasks(offspringTasks1);

			offspring2.setProcessors(parent2.getProcessors());
			offspring2.setTasks(offspringTasks2);
		}

	}

	private boolean in(GATask[] tasks, GATask task) {
		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i] != null && tasks[i].getTaskId() == task.getTaskId())
				return true;
		}
		return false;
	}

	private int getBestTournamentSelection(List<GAChromosome> population) {

		int bestIndex = -1, index;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			index = rand.nextInt(POPULATION_SIZE);

			if (bestIndex == -1
					|| population.get(index).getFitnessValue() > population.get(bestIndex).getFitnessValue()) {
				bestIndex = index;
			}
		}

		return bestIndex;
	}

	private int getWorstTournamentSelection(List<GAChromosome> population) {

		int worstIndex = -1, index;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			index = rand.nextInt(POPULATION_SIZE);

			if (worstIndex == -1
					|| population.get(index).getFitnessValue() < population.get(worstIndex).getFitnessValue()) {
				worstIndex = index;
			}
		}

		return worstIndex;
	}

}
