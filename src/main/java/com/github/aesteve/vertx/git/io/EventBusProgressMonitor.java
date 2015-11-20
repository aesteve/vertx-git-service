package com.github.aesteve.vertx.git.io;

import static com.github.aesteve.vertx.git.io.notif.TaskEvent.begin;
import static com.github.aesteve.vertx.git.io.notif.TaskEvent.end;
import static com.github.aesteve.vertx.git.io.notif.TaskEvent.progress;
import static com.github.aesteve.vertx.git.io.notif.TaskEvent.start;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

import org.eclipse.jgit.lib.ProgressMonitor;

public class EventBusProgressMonitor implements ProgressMonitor {

	private Vertx vertx;
	private String address;
	private boolean cancelled;

	public EventBusProgressMonitor(Vertx vertx, String address) {
		this.vertx = vertx;
		this.address = address;
	}

	@Override
	public void start(int totalTasks) {
		JsonObject message = new JsonObject();
		DeliveryOptions options = new DeliveryOptions();
		options.addHeader("event", start.toString());
		message.put("total", totalTasks);
		vertx.eventBus().publish(address, message, options);
	}

	@Override
	public void beginTask(String title, int totalWork) {
		JsonObject message = new JsonObject();
		DeliveryOptions options = new DeliveryOptions();
		options.addHeader("event", begin.toString());
		message.put("title", title);
		message.put("total", totalWork);
		vertx.eventBus().publish(address, message, options);
	}

	@Override
	public void update(int completed) {
		JsonObject message = new JsonObject();
		DeliveryOptions options = new DeliveryOptions();
		options.addHeader("event", progress.toString());
		message.put("completed", completed);
		vertx.eventBus().publish(address, message, options);
	}

	@Override
	public void endTask() {
		JsonObject message = new JsonObject();
		DeliveryOptions options = new DeliveryOptions();
		options.addHeader("event", end.toString());
		vertx.eventBus().publish(address, message, options);
	}

	public String address() {
		return address;
	}

	public void cancel() {
		cancelled = true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

}
