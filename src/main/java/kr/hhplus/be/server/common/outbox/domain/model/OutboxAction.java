package kr.hhplus.be.server.common.outbox.domain.model;

public enum OutboxAction {

    PROCESS,
    PUBLISH,
    FAIL,
    RETRY,
    DEAD
}