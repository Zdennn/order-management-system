package cz.common;

import java.io.Serializable;

import cz.common.services.ServerService;

public class NovyKlientResponse implements Serializable {
    private final int id;
    private final ServerService server;

    public NovyKlientResponse(int id, ServerService server) {
        this.id = id;
        this.server = server;
    }

    public int getId() { return id; }
    public ServerService getServer() { return server; }
}
