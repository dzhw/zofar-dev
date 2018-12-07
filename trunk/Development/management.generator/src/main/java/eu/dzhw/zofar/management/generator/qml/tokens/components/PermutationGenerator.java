package eu.dzhw.zofar.management.generator.qml.tokens.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.utils.string.StringUtils;

public class PermutationGenerator {
	
	private static final PermutationGenerator INSTANCE = new PermutationGenerator();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PermutationGenerator.class);

	private PermutationGenerator() {
		super();
	}

	public static PermutationGenerator getInstance() {
		return INSTANCE;
	}
	
	private List<String> doComputations(String inputString, final int length) {
		List<String> totalList = new ArrayList<String>();
		totalList.addAll(getCombinationsPerLength(inputString, length));
		return totalList;
	}

	private ArrayList<String> getCombinationsPerLength(String inputString, int i) {
		ArrayList<String> combinations = new ArrayList<String>();
		if (i == 1) {
			char[] charArray = inputString.toCharArray();
			for (int j = 0; j < charArray.length; j++) {
				combinations.add(((Character) charArray[j]).toString());
			}
			return combinations;
		}
		for (int j = 0; j < inputString.length(); j++) {

			ArrayList<String> combs = getCombinationsPerLength(inputString, i - 1);
			for (String string : combs) {
				combinations.add(inputString.charAt(j) + string);
			}
		}
		return combinations;
	}

//	public List<String> calculatePermutations(final String chars,final int minLength,final int maxLength,final int count,final boolean shuffel) throws Exception{
//		PermutationGenerator permutationGenerator = new PermutationGenerator();
//		int neededSlots = minLength;
//		while(neededSlots<=maxLength){
//			final List<String> possiblePermutations =  permutationGenerator.doComputations(chars,neededSlots);
//			if(possiblePermutations.size() >= count){
//				if(shuffel)Collections.shuffle(possiblePermutations);
//				return possiblePermutations.subList(0, count);
//			}
//			neededSlots = neededSlots+1;
//		}
//		throw new Exception("too less variations of Characters for "+maxLength+" digits");
//	}
	
	public List<String> calculatePermutations(final String chars,final int minLength,final int maxLength,final int count,final boolean shuffel) throws Exception{
		PermutationGenerator permutationGenerator = new PermutationGenerator();
		int neededSlots = 1;
		StringUtils stringUtils = StringUtils.getInstance();
		while(neededSlots<=maxLength){
			List<String> possiblePermutations =  permutationGenerator.doComputations(chars,neededSlots);
			if(possiblePermutations.size() >= count){
				if(shuffel)Collections.shuffle(possiblePermutations);
				if(neededSlots < minLength){
					//fill up
					final List<String> filledUpPossiblePermutations = new ArrayList<String>();
					for(final String token : possiblePermutations.subList(0, count)){
						String salt = stringUtils.randomString(chars, minLength-neededSlots);
						final String filledUpToken = token+salt;
						filledUpPossiblePermutations.add(filledUpToken);
					}
					possiblePermutations = filledUpPossiblePermutations;
					
				}
				return possiblePermutations.subList(0, count);
			}
			neededSlots = neededSlots+1;
		}
		throw new Exception("too less variations of Characters for "+maxLength+" digits");
	}
}
