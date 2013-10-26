package net.bot.genome;

public class GenomeBuilder {
	
	// Standard constructor
	
	public static Genome generateRandomGenome() {
		return new Genome(ChromosomeBuilder.generateRandomChromosome(), ChromosomeBuilder.generateRandomChromosome());
	}

}
