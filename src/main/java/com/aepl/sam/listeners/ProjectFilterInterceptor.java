package com.aepl.sam.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.List;

public class ProjectFilterInterceptor implements IMethodInterceptor {
	private static final Logger logger = LogManager.getLogger(ProjectFilterInterceptor.class);

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		String activeProject = System.getProperty("project", System.getenv("PROJECT"));
		if (activeProject == null || activeProject.isEmpty()) {
			activeProject = "sampark"; // Default to sampark if not specified
		}
		activeProject = activeProject.toLowerCase();

		logger.info("ProjectFilterInterceptor: Active project for filtering: {}", activeProject);

		List<IMethodInstance> filtered = new ArrayList<>();
		String[] allKnownProjects = {"atcu", "lct", "sampark", "swaraj", "trio"};

		for (IMethodInstance m : methods) {
			String[] groups = m.getMethod().getGroups();
			boolean hasOtherProjectMarker = false;
			boolean belongsToActiveProject = false;

			for (String group : groups) {
				String g = group.toLowerCase();
				if (g.equals(activeProject)) {
					belongsToActiveProject = true;
				}
				for (String kp : allKnownProjects) {
					if (g.equals(kp)) {
						hasOtherProjectMarker = true;
						break;
					}
				}
			}

			// If the test doesn't target any of the known projects, run it by default.
			// If it does, it must explicitly target the active project.
			if (!hasOtherProjectMarker || belongsToActiveProject) {
				filtered.add(m);
			} else {
				logger.info("ProjectFilterInterceptor: Skipping test '{}' because it is not marked for active project '{}'",
						m.getMethod().getMethodName(), activeProject);
			}
		}

		logger.info("ProjectFilterInterceptor: Filtered tests count: {}/{}", filtered.size(), methods.size());
		return filtered;
	}
}
