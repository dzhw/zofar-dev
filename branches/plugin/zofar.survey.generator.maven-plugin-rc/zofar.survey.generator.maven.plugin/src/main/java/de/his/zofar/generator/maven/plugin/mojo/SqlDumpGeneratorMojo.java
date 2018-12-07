/**
 *
 */
package de.his.zofar.generator.maven.plugin.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * creates sql dump of participant tokens and preloads.
 *
 * @author le
 *
 */
@Mojo(name = "generate-sqldump")
public class SqlDumpGeneratorMojo extends AbstractMojo {

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public final void execute() throws MojoExecutionException,
            MojoFailureException {
        this.getLog().info(
                "Generating zofar sql dump of participants and their preloads.");
    }

}
