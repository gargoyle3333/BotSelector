package net.bot.genome;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.bot.entities.EntityBot;

public class GenomeBuilder {
	
	// Standard constructor
	
	public static Genome generateRandomGenome() {
		return new Genome(ChromosomeBuilder.generateRandomChromosome(), ChromosomeBuilder.generateRandomChromosome());
	}
	
	public static void writeListToFile(EntityBot[] list, String fileLoc) throws FileNotFoundException, IOException {
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileLoc));
		for(EntityBot bot : list) {
			stream.writeObject(bot.getGenome());
		}
		stream.close();
	}

}
