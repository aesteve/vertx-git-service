package com.github.aesteve.vertx.git;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.github.aesteve.vertx.git.io.EventBusProgressMonitor;

@RunWith(VertxUnitRunner.class)
public class GitServiceTestBase {

	protected static final String NUBES_URL = "https://github.com/aesteve/nubes.git";
	protected static final String PROJUICE_URL = "https://github.com/aesteve/projuice.git";
	protected static final String TEST_WORKDIR = "C:/tmp/test-dir";

	protected Vertx vertx;
	protected GitService gitService;

	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		gitService = GitService.create(vertx, TEST_WORKDIR);
		vertx.fileSystem().deleteRecursive(TEST_WORKDIR, true, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	protected void registerMonitorHandler(EventBusProgressMonitor monitor) {
		vertx.eventBus().consumer(monitor.address(), message -> {
			System.out.println("Event: " + message.headers().get("event"));
			System.out.println(message.body());
		});
	}
}
