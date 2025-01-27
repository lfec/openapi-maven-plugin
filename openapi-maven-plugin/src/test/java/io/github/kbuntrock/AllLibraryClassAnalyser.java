package io.github.kbuntrock;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.kbuntrock.configuration.ApiConfiguration;
import io.github.kbuntrock.configuration.library.Library;
import io.github.kbuntrock.configuration.library.TagAnnotation;
import io.github.kbuntrock.resources.endpoint.generic.ActionResource;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

/**
 * Test for the 3 libraries together :
 * - SpringMVC
 * - JaxRS
 * - JakartaRS
 *
 * @author Kévin Buntrock
 */
public class AllLibraryClassAnalyser extends AbstractTest {

	private MavenProject createBasicMavenProject() {
		final MavenProject mavenProjet = new MavenProject();
		mavenProjet.setName("My Project");
		mavenProjet.setVersion("10.5.36");
		mavenProjet.setFile(new File(new File("pom.xml").getAbsolutePath()));
		return mavenProjet;
	}

	private DocumentationMojo createBasicJaxRsMojo(final String apiLocation) {
		final DocumentationMojo mojo = new DocumentationMojo();
		final ApiConfiguration apiConfiguration = new ApiConfiguration();
		apiConfiguration.setLibrary(Library.JAVAX_RS.name());
		apiConfiguration.setAttachArtifact(false);
		apiConfiguration.setLocations(Collections.singletonList(apiLocation));
		mojo.setTestMode(true);
		mojo.setApis(Collections.singletonList(apiConfiguration));
		mojo.setProject(createBasicMavenProject());
		return mojo;
	}

	private DocumentationMojo createBasicJakartaRsMojo(final String apiLocation) {
		final DocumentationMojo mojo = new DocumentationMojo();
		final ApiConfiguration apiConfiguration = new ApiConfiguration();
		apiConfiguration.setLibrary(Library.JAKARTA_RS.name());
		apiConfiguration.setAttachArtifact(false);
		apiConfiguration.setLocations(Collections.singletonList(apiLocation));
		mojo.setTestMode(true);
		mojo.setApis(Collections.singletonList(apiConfiguration));
		mojo.setProject(createBasicMavenProject());
		return mojo;
	}

	private DocumentationMojo createBasicSpringMvcMojo(final String apiLocation) {
		final DocumentationMojo mojo = new DocumentationMojo();
		final ApiConfiguration apiConfiguration = new ApiConfiguration();
		apiConfiguration.setLibrary(Library.SPRING_MVC.name());
		apiConfiguration.setTagAnnotations(Collections.singletonList(TagAnnotation.SPRING_MVC_REQUEST_MAPPING.getAnnotationClassName()));
		apiConfiguration.setAttachArtifact(false);
		apiConfiguration.setLocations(Collections.singletonList(apiLocation));
		mojo.setTestMode(true);
		mojo.setApis(Collections.singletonList(apiConfiguration));
		mojo.setProject(createBasicMavenProject());
		return mojo;
	}

	@Test
	public void genericity_on_endpoint() throws MojoFailureException, MojoExecutionException, IOException {

		final DocumentationMojo mojoJaxRs = createBasicJaxRsMojo(ActionResource.class.getCanonicalName());
		final List<File> generatedJaxRs = mojoJaxRs.documentProject();
		checkGenerationResult(mojoJaxRs.documentProject());

		final DocumentationMojo mojoJakartaRs = createBasicJakartaRsMojo(ActionResource.class.getCanonicalName());
		final List<File> generatedJakartaRs = mojoJakartaRs.documentProject();
		assertThat(generatedJakartaRs.get(0)).hasSameTextualContentAs(generatedJaxRs.get(0));

		final DocumentationMojo mojoSpringMvc = createBasicSpringMvcMojo(ActionResource.class.getCanonicalName());
		final List<File> generatedSpringMvc = mojoSpringMvc.documentProject();
		assertThat(generatedSpringMvc.get(0)).hasSameTextualContentAs(generatedJaxRs.get(0));
	}

}
