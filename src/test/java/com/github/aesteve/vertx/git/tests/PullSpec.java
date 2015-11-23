package com.github.aesteve.vertx.git.tests;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.junit.Test;

import com.github.aesteve.vertx.git.GitServiceTestBase;
import com.github.aesteve.vertx.git.io.EventBusProgressMonitor;

public class PullSpec extends GitServiceTestBase {

	@Test
	public void testPull(TestContext context) {
		Async async = context.async();
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(PROJUICE_URL);
		gitService.clone("nubes", cloneCommand, cloneRes -> {
			Git git = cloneRes.result();
			PullCommand pull = git.pull();
			EventBusProgressMonitor monitor = gitService.pull(pull, res -> {
				context.assertFalse(res.failed());
				PullResult result = res.result();
				context.assertNotNull(result);
				context.assertTrue(result.isSuccessful());
				async.complete();
			});
			registerMonitorHandler(monitor);
		});
	}

}
