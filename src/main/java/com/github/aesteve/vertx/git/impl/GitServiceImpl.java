package com.github.aesteve.vertx.git.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;

import com.github.aesteve.vertx.git.GitService;
import com.github.aesteve.vertx.git.io.EventBusProgressMonitor;

public class GitServiceImpl implements GitService {

	private final static Logger log = LoggerFactory.getLogger(GitService.class);

	private Vertx vertx;
	private File workDir;

	public GitServiceImpl(Vertx vertx, String workDir) {
		this.vertx = vertx;
		this.workDir = new File(workDir);
		log.info("Git service will work within : " + this.workDir.getAbsolutePath());
	}

	@Override
	public <T> void exec(GitCommand<T> command, Handler<AsyncResult<T>> handler) {
		vertx.executeBlocking(future -> {
			try {
				future.complete(command.call());
			} catch (Exception e) {
				future.fail(e);
			}
		}, handler);
	}

	@Override
	public EventBusProgressMonitor clone(String relPath, CloneCommand command, Handler<AsyncResult<Git>> handler) {
		Objects.requireNonNull(relPath);
		String address = "git.command.clone." + new Date().getTime();
		EventBusProgressMonitor monitor = new EventBusProgressMonitor(vertx, address);
		command.setDirectory(new File(workDir.getAbsolutePath() + "/" + relPath));
		command.setProgressMonitor(monitor);
		exec(command, res -> {
			if (res.result() != null) {
				res.result().close();
			}
			handler.handle(res);
		});
		return monitor;
	}

	@Override
	public EventBusProgressMonitor pull(PullCommand pull, Handler<AsyncResult<PullResult>> handler) {
		String address = "git.command.pull." + new Date().getTime();
		EventBusProgressMonitor monitor = new EventBusProgressMonitor(vertx, address);
		pull.setProgressMonitor(monitor);
		exec(pull, handler);
		return monitor;
	}

	@Override
	public String getWorkDir() {
		return workDir.getAbsolutePath();
	}

}
