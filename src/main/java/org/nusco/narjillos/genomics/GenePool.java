package org.nusco.narjillos.genomics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.utilities.RanGen;

/**
 * A pool of DNA strands.
 */
public abstract class GenePool {

	private long dnaSerialId = 0;
	private final Map<Long, DNA> currentPool = new LinkedHashMap<>();
	private transient DNALog dnaLog;

	public GenePool(DNALog dnaLog) {
		this.dnaLog = dnaLog;
	}

	public DNA createDNA(String dna) {
		DNA result = new DNA(dnaLog.getNextAvailableDnaId(), dna, DNA.NO_PARENT);
		addToPool(result);
		return result;
	}

	public DNA createRandomDNA(RanGen ranGen) {
		DNA result = DNA.random(dnaLog.getNextAvailableDnaId(), ranGen);
		addToPool(result);
		return result;
	}

	public DNA mutateDNA(DNA parent, RanGen ranGen) {
		DNA result = parent.mutate(dnaLog.getNextAvailableDnaId(), ranGen);
		addToPool(result);
		return result;
	}

	public void remove(DNA dna) {
		currentPool.remove(dna.getId());
	}

	public Set<Long> getCurrentPool() {
		return currentPool.keySet();
	}

	public abstract DNA getDna(Long id);

	public abstract List<DNA> getAncestryOf(DNA dna);

	public abstract Set<Long> getHistoricalPool();
	
	public abstract DNA getMostSuccessfulDNA();

	public abstract int getGenerationOf(DNA dna);

	public long getCurrentSerialId() {
		return dnaSerialId;
	}

	public double getAverageGeneration() {
		Set<Long> currentPool = getCurrentPool();
		if (currentPool.size() == 0)
			return 0;
		
		double generationsSum = 0;
		for (Long id : currentPool) {
			DNA dna = getDna(id);
			generationsSum += getGenerationOf(dna);
		}
		return generationsSum / currentPool.size();
	}

	abstract Map<Long, Long> getChildrenToParents();
	abstract Map<Long, List<Long>> getParentsToChildren();

	private void addToPool(DNA dna) {
		currentPool.put(dna.getId(), dna);
	}
}
