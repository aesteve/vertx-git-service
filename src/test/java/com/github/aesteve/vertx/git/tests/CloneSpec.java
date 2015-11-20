package com.github.aesteve.vertx.git.tests;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.junit.Test;

import com.github.aesteve.vertx.git.GitServiceTestBase;
import com.github.aesteve.vertx.git.io.EventBusProgressMonitor;

public class CloneSpec extends GitServiceTestBase {

	@Test
	public void cloneSomeRepo(TestContext context) {
		Async async = context.async();
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(NUBES_URL);
		EventBusProgressMonitor monitor = gitService.clone(cloneCommand, res -> {
			if (res.failed()) {
				res.cause().printStackTrace();
			}
			context.assertFalse(res.failed());
			Git git = res.result();
			git.branchCreate();
			async.complete();
		});
		registerMonitorHandler(monitor);
	}

}
