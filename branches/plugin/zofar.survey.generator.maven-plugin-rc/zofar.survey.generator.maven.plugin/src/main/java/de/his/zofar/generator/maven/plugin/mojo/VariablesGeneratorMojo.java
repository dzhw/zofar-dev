/**
 *
 */
package de.his.zofar.generator.maven.plugin.mojo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import de.his.zofar.generator.maven.plugin.generator.page.PageManager;
import de.his.zofar.generator.maven.plugin.generator.variable.VariableGenerator;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import de.his.zofar.xml.questionnaire.VariableType;

/**
 * @author le
 *
 */
public class VariablesGeneratorMojo {

    private final QuestionnaireDocument questionnaire;

    private final VariableGenerator generator = new VariableGenerator();

    private final File outputDirectory;

    public VariablesGeneratorMojo(final QuestionnaireDocument questionnaire,
            final File outputDirectory) {
        super();
        this.questionnaire = questionnaire;
        this.outputDirectory = outputDirectory;
    }

    public final void execute() throws MojoExecutionException,
            MojoFailureException {
        VariableType[] variables = null;
        if (questionnaire != null
                && questionnaire.getQuestionnaire().getVariables() != null) {
            variables = questionnaire.getQuestionnaire().getVariables()
                    .getVariableArray();
        }

        // create basic survey-variable-context.xml
        generator.createDocument();

        if (variables != null) {
            for (final VariableType variable : variables) {
                final VariableType.Type.Enum type = variable.getType();
                switch (type.intValue()) {
                case VariableType.Type.INT_BOOLEAN:
                    generator.addBooleanVariable(variable, generator
                            .findItemUidOfBoolean(variable.getName(),
                                    questionnaire));
                    break;
                case VariableType.Type.INT_NUMBER:
                    generator.addNumberVariable(variable);
                    break;
                case VariableType.Type.INT_SINGLE_CHOICE_ANSWER_OPTION:
                    generator.addSingleChoiceAnswerOption(variable,
                            questionnaire);
                    break;
                case VariableType.Type.INT_STRING:
                    generator.addStringVariable(variable);
                    break;
                default:
                    throw new MojoFailureException("unknown variable type: "
                            + type.toString() + ". maybe not yet implemented.");
                }
            }
        }

        // XXX this should maybe happen in the
        // SurveyGeneratorMojo.generateVariables().
        // add automatically created variables (during generatePages()).
        final Map<VariableType, Map<String,String>> variableOptions = PageManager.getInstance().getAdditionalVariableOptions();
        for (final VariableType variable : PageManager.getInstance()
                .getAdditionalVariables()) {
            final VariableType.Type.Enum type = variable.getType();
            switch (type.intValue()) {
            case VariableType.Type.INT_BOOLEAN:
                generator.addBooleanVariable(variable,
                        generator.findItemUidOfBoolean(variable.getName(),
                                questionnaire));
                break;
            case VariableType.Type.INT_NUMBER:
                generator.addNumberVariable(variable);
                break;
            case VariableType.Type.INT_SINGLE_CHOICE_ANSWER_OPTION:
                generator.addSingleChoiceAnswerOption(variable, questionnaire,variableOptions.get(variable));
                break;
            case VariableType.Type.INT_STRING:
                generator.addStringVariable(variable);
                break;
            default:
                throw new MojoFailureException("unknown variable type: "
                        + type.toString() + ". maybe not yet implemented.");
            }
        }

        save();
    }

    /**
     * @throws MojoFailureException
     */
    public void save() throws MojoFailureException {
        try {
            generator.saveDocument(outputDirectory.getAbsolutePath());
        } catch (final IOException e) {
            throw new MojoFailureException(
                    "could not save survey-variable-context.xml to the file system.");
        }
    }
}
