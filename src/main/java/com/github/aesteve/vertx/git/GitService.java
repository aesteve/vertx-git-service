package com.github.aesteve.vertx.git;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;

import com.github.aesteve.vertx.git.impl.GitServiceImpl;
import com.github.aesteve.vertx.git.io.EventBusProgressMonitor;

public interface GitService {

	public static GitService create(Vertx vertx, String workDir) {
		return new GitServiceImpl(vertx, workDir);
	}

	public <T> void exec(GitCommand<T> command, Handler<AsyncResult<T>> handler);

	public EventBusProgressMonitor clone(CloneCommand command, Handler<AsyncResult<Git>> handler);

	public EventBusProgressMonitor pull(PullCommand pull, Handler<AsyncResult<PullResult>> handler);
}
