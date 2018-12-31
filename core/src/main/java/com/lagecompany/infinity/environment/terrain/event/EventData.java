package com.lagecompany.infinity.environment.terrain.event;

import java.io.Serializable;

import org.nustaq.serialization.FSTConfiguration;

public class EventData {
	private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	private final EventType type;
	private final byte[] buffer;

	public EventData(EventType type) {
		this.type = type;
		this.buffer = null;
	}

	public EventData(EventType type, Serializable data) {
		this.type = type;
		this.buffer = conf.asByteArray(data);
	}

	public EventType getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) conf.asObject(buffer);
	}
}
