/**
 *
 */
package de.his.zofar.generator.maven.plugin.generator.page;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.his.zofar.generator.maven.plugin.mojo.PagesGeneratorMojo;
import de.his.zofar.xml.questionnaire.VariableType;

/**
 * singleton class to store the current page.
 *
 * @author le
 *
 */
public final class PageManager {

    private static final PageManager INSTANCE = new PageManager();

    private final Set<VariableType> additionalVariables = new HashSet<>();
    private final Map<VariableType, Map<String,String>> additionalVariableOptions = new HashMap<>();

    private ZofarWebPage currentPage;

    private PagesGeneratorMojo mojo;

    private PageManager() {
        super();
    }

    public static final PageManager getInstance() {
        return INSTANCE;
    }

    /**
     * @return the currentPage
     */
    public ZofarWebPage getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage
     *            the currentPage to set
     */
    public void setCurrentPage(final ZofarWebPage currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the mojo
     */
    public PagesGeneratorMojo getMojo() {
        return mojo;
    }

    /**
     * @param mojo
     *            the mojo to set
     */
    public void setMojo(final PagesGeneratorMojo mojo) {
        this.mojo = mojo;
    }

    /**
     * @return the additionalVariables
     */
    public Set<VariableType> getAdditionalVariables() {
        return additionalVariables;
    }
    
    /**
     * @return the additionalVariables
     */
    public Map<VariableType, Map<String,String>> getAdditionalVariableOptions() {
        return additionalVariableOptions;
    }

}
