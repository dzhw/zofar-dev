package eu.dzhw.zofar.management.generator.qml.slc.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.zofar.management.generator.qml.slc.GenericGenerator;
import eu.dzhw.zofar.management.generator.qml.slc.LoopGenerator;
import eu.dzhw.zofar.management.generator.qml.slc.ModuleGenerator;
import eu.dzhw.zofar.management.generator.qml.slc.SplitGenerator;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class Generator {

	public static void main(String[] args) {
		final GenericGenerator module = ModuleGenerator.getInstance();
		final GenericGenerator loop = LoopGenerator.getInstance();
		final GenericGenerator split = SplitGenerator.getInstance();

		final FileClient fileC = FileClient.getInstance();

		final char[] loops = "abcdefghijklmnopqrstuvwxyz".toCharArray();
//		final char[] splits = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		final char[] splits = "bcdefghijklmnopqrstuvwxyz".toCharArray();

		final String modulePrefixName = "Template_ModulePrefix_cleaned.xml";
		final String modulePostfixName = "Template_ModulePostfix_cleaned.xml";

		final String loopPrefixName = "Template_LoopPrefix_cleaned.xml";
		final String loopPostfixName = "Template_LoopPostfix_cleaned.xml";
		
		final String splitPrefixName = "Template_SplitPrefix_cleaned.xml";
		final String splitPostfixName = "Template_SplitPostfix_cleaned.xml";

		final String splitName = "Template_Split_cleaned.xml";

		final String modul = "st";
		int loopLimit = 4;
		int splitLimit = 4;
		//int splitPageLimit = 1;
		//int postfixLimit = 1;
		
		
		final Map<String,String> globalReplacements = new HashMap<String,String>();
		globalReplacements.put("modul", modul);
		
		globalReplacements.put("loop00", "0");
		globalReplacements.put("split00", "0");

		globalReplacements.put("loop0", "a");
		globalReplacements.put("loop1", "b");
		globalReplacements.put("loop2", "c");
		globalReplacements.put("loop3", "d");
		globalReplacements.put("loop4", "e");
		globalReplacements.put("loop5", "f");
		globalReplacements.put("loop6", "g");
		globalReplacements.put("loop7", "h");
		globalReplacements.put("loop8", "i");
		globalReplacements.put("loop9", "j");
		globalReplacements.put("loop10", "k");
		globalReplacements.put("loop11", "l");
		globalReplacements.put("loop12", "m");
		globalReplacements.put("loop13", "n");
		globalReplacements.put("loop14", "o");
		globalReplacements.put("loop15", "p");
		globalReplacements.put("loop16", "q");
		globalReplacements.put("loop17", "r");
		globalReplacements.put("loop18", "s");
		globalReplacements.put("loop19", "t");
		globalReplacements.put("loop20", "u");
		globalReplacements.put("loop21", "v");
		globalReplacements.put("loop22", "w");
		globalReplacements.put("loop23", "x");
		globalReplacements.put("loop24", "y");
		globalReplacements.put("loop25", "z");

		globalReplacements.put("split0", "a");
		globalReplacements.put("split1", "b");
		globalReplacements.put("split2", "c");
		globalReplacements.put("split3", "d");
		globalReplacements.put("split4", "e");
		globalReplacements.put("split5", "f");
		globalReplacements.put("split6", "g");
		globalReplacements.put("split7", "h");
		globalReplacements.put("split8", "i");
		globalReplacements.put("split9", "j");
		globalReplacements.put("split10", "k");
		globalReplacements.put("split11", "l");
		globalReplacements.put("split12", "m");
		globalReplacements.put("split13", "n");
		globalReplacements.put("split14", "o");
		globalReplacements.put("split15", "p");
		globalReplacements.put("split16", "q");
		globalReplacements.put("split17", "r");
		globalReplacements.put("split18", "s");
		globalReplacements.put("split19", "t");
		globalReplacements.put("split20", "u");
		globalReplacements.put("split21", "v");
		globalReplacements.put("split22", "w");
		globalReplacements.put("split23", "x");
		globalReplacements.put("split24", "y");
		globalReplacements.put("split25", "z");
		

		final File baseDir = new File(File.separator + "home" + File.separator + "hisuser" + File.separator + "Projekte"
				+ File.separator + "SLC" + File.separator + "generator" + File.separator + "Studium");
				
		final File templateDir = new File(baseDir, "templates");

		final String modulePrefixTemplate = fileC.readAsString(new File(templateDir, modulePrefixName));
		final String modulePostfixTemplate = fileC.readAsString(new File(templateDir, modulePostfixName));

		final String loopPrefixTemplate = fileC.readAsString(new File(templateDir, loopPrefixName));
		final String loopPostfixTemplate = fileC.readAsString(new File(templateDir, loopPostfixName));

		final String splitPrefixTemplate = fileC.readAsString(new File(templateDir, splitPrefixName));
		final String splitPostfixTemplate = fileC.readAsString(new File(templateDir, splitPostfixName));
		final String splitTemplate = fileC.readAsString(new File(templateDir, splitName));

		final StringBuilder moduleContent = new StringBuilder();

		
		//Module Prefix
		final List<Map<String, String>> modulePrefixReplacements = new ArrayList<Map<String, String>>();
		modulePrefixReplacements.add(globalReplacements);

		final String modulePrefixResult = module.generatePrefix(modulePrefixTemplate, modulePrefixReplacements);
		moduleContent.append(modulePrefixResult + "\n");

		// Loops
		for (int loopIndex = 0; loopIndex < loopLimit; loopIndex++) {
			final String loopID = loops[loopIndex] + "";
			final StringBuffer loopContent = new StringBuffer();

			
			// Loop Prefix
			final List<Map<String, String>> loopPrefixReplacements = new ArrayList<Map<String, String>>();
			for (int lft = 0; lft < 1; lft++) {
				final Map<String, String> replacement = new HashMap<String, String>();
				replacement.putAll(globalReplacements);
				replacement.put("loop", loopID);
				loopPrefixReplacements.add(replacement);
			}

			final String loopPrefixResult = loop.generatePrefix(loopPrefixTemplate, loopPrefixReplacements);
			loopContent.append(loopPrefixResult + "\n");

			//Splits 
			for (int splitIndex = 0; splitIndex < splitLimit; splitIndex++) {
				final String splitID = splits[splitIndex] + "";
				
				// Split Prefix
				final List<Map<String, String>> splitPrefixReplacements = new ArrayList<Map<String, String>>();
				for (int lft = 0; lft < 1; lft++) {
					final Map<String, String> replacement = new HashMap<String, String>();
					replacement.putAll(globalReplacements);
					replacement.put("loop", loopID);
					replacement.put("split", splitID);
					splitPrefixReplacements.add(replacement);
				}

				final String splitPrefixResult = split.generatePrefix(splitPrefixTemplate, splitPrefixReplacements);
				loopContent.append(splitPrefixResult + "\n");

				
				//Split Content
				final List<Map<String, String>> splitReplacements = new ArrayList<Map<String, String>>();
				for (int lft = 0; lft < 1; lft++) {
					final Map<String, String> replacement = new HashMap<String, String>();
					replacement.putAll(globalReplacements);
					replacement.put("loop", loopID);
					replacement.put("split", splitID);
					splitReplacements.add(replacement);
				}

				final String splitsResult = split.process(splitTemplate, splitReplacements);
				loopContent.append(splitsResult + "\n");
				
				
				// Split Postfix
				final List<Map<String, String>> splitPostfixReplacements = new ArrayList<Map<String, String>>();
				for (int lft = 0; lft < 1; lft++) {
					final Map<String, String> replacement = new HashMap<String, String>();
					replacement.putAll(globalReplacements);
					replacement.put("loop", loopID);
					replacement.put("split", splitID);
					splitPostfixReplacements.add(replacement);
				}

				final String splitPostfixResult = split.generatePrefix(splitPostfixTemplate, splitPostfixReplacements);
				loopContent.append(splitPostfixResult + "\n");
			}
			splitLimit = splitLimit - 1;
			// Loop Postfix
			final List<Map<String, String>> loopPostfixReplacements = new ArrayList<Map<String, String>>();
			for (int lft = 0; lft < 1; lft++) {
				final Map<String, String> replacement = new HashMap<String, String>();
				replacement.putAll(globalReplacements);
				replacement.put("loop", loopID);
				loopPostfixReplacements.add(replacement);
			}
			
			final String loopPostfixResult = loop.generatePostfix(loopPostfixTemplate, loopPostfixReplacements);
			loopContent.append(loopPostfixResult + "\n");

			final List<Map<String, String>> loopReplacements = new ArrayList<Map<String, String>>();
			final Map<String, String> replacements = new HashMap<String, String>();
			replacements.putAll(globalReplacements);
			replacements.put("loop", loopID);
			loopReplacements.add(replacements);
			
			final String loopResult = loop.process(loopContent.toString(), loopReplacements);
			moduleContent.append(loopResult + "\n");
		}

		
		//Module Postfix
		final List<Map<String, String>> modulePostfixReplacements = new ArrayList<Map<String, String>>();
		for (int lft = 0; lft < 1; lft++) {
			final Map<String, String> replacements = new HashMap<String, String>();
			replacements.putAll(globalReplacements);
			modulePostfixReplacements.add(replacements);
		}

		final String postfixResult = module.generatePostfix(modulePostfixTemplate, modulePostfixReplacements);
		moduleContent.append(postfixResult + "\n");
		
		final List<Map<String, String>> moduleReplacements = new ArrayList<Map<String, String>>();
		moduleReplacements.add(globalReplacements);
		
		final String moduleResult = module.process(moduleContent.toString(), moduleReplacements);

		try {
			final File resultFile = fileC.createOrGetFile("generated__" + modul, ".xml", baseDir);
			fileC.writeToFile(resultFile,moduleResult, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
