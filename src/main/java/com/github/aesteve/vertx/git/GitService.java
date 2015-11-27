package com.github.aesteve.vertx.git;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Ref;

import com.github.aesteve.vertx.git.impl.GitServiceImpl;
import com.github.aesteve.vertx.git.io.EventBusProgressMonitor;

public interface GitService {

	public static GitService create(Vertx vertx, String workDir) {
		return new GitServiceImpl(vertx, workDir);
	}

	public String getWorkDir();

	public <T> void exec(GitCommand<T> command, Handler<AsyncResult<T>> handler);

	public EventBusProgressMonitor clone(String relPath, CloneCommand command, Handler<AsyncResult<Git>> handler);

	public EventBusProgressMonitor pull(PullCommand pull, Handler<AsyncResult<PullResult>> handler);

	public void listAllBranches(ListBranchCommand command, Handler<AsyncResult<List<Ref>>> handler);
}
