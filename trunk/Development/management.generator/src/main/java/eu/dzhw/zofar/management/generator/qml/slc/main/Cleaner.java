package eu.dzhw.zofar.management.generator.qml.slc.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;

public class Cleaner {

	protected final static FileClient fileC = FileClient.getInstance();
	protected final static ReplaceClient replaceC = ReplaceClient.getInstance();

	public static void main(String[] args) {
		final File baseDir = new File(File.separator + "home" + File.separator + "hisuser" + File.separator + "Projekte"
				+ File.separator + "SLC" + File.separator + "generator" + File.separator + "Studium");
		
		final String module = "st";

		final Map<String, String> replacements = new HashMap<String, String>();
		replacements.put(module+"00", "[[modul]][[loop00]][[split00]]");
		
		replacements.put(module+"aa", "[[modul]][[loop0]][[split0]]");
		replacements.put(module+"ab", "[[modul]][[loop0]][[split1]]");
		replacements.put(module+"ac", "[[modul]][[loop0]][[split2]]");
		replacements.put(module+"ad", "[[modul]][[loop0]][[split3]]");

		replacements.put(module+"ba", "[[modul]][[loop1]][[split0]]");
		replacements.put(module+"bb", "[[modul]][[loop1]][[split1]]");
		replacements.put(module+"bc", "[[modul]][[loop1]][[split2]]");
		replacements.put(module+"bd", "[[modul]][[loop1]][[split3]]");

		replacements.put(module+"ca", "[[modul]][[loop2]][[split0]]");
		replacements.put(module+"cb", "[[modul]][[loop2]][[split1]]");
		replacements.put(module+"cc", "[[modul]][[loop2]][[split2]]");
		replacements.put(module+"cd", "[[modul]][[loop2]][[split3]]");
		
		replacements.put(module+"da", "[[modul]][[loop3]][[split0]]");
		replacements.put(module+"db", "[[modul]][[loop3]][[split1]]");
		replacements.put(module+"dc", "[[modul]][[loop3]][[split2]]");
		replacements.put(module+"dd", "[[modul]][[loop3]][[split3]]");

		final List<String> templateNames = new ArrayList<String>();
		templateNames.add("Template_ModulePrefix.xml");
		templateNames.add("Template_ModulePostfix.xml");
		templateNames.add("Template_LoopPrefix.xml");
		templateNames.add("Template_LoopPostfix.xml");
		templateNames.add("Template_SplitPrefix.xml");
		templateNames.add("Template_SplitPostfix.xml");
		templateNames.add("Template_Split.xml");

		for (final String templateName : templateNames) {
			final File templateFile = new File(baseDir, templateName);
			final String template = fileC.readAsString(templateFile);
			final String cleanedTemplate = replacePlaceholder(template,replacements);
			try {
				fileC.writeToFile(templateFile.getParentFile().getAbsolutePath()+File.separator+(templateName.substring(0,templateName.lastIndexOf(".")))+"_cleaned.xml", cleanedTemplate, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String replacePlaceholder(String template, Map<String, String> replacements) {
		final StringBuffer back = new StringBuffer();

		if ((replacements == null))
			return template;
		if ((replacements.isEmpty()))
			return template;

		String generatedStr = template;
		for (Map.Entry<String, String> replacement : replacements.entrySet()) {
			generatedStr = replaceC.replaceTag(generatedStr, replacement.getKey(), replacement.getValue());
		}
		back.append(generatedStr);

		return back.toString();
	}

}
