package kr.hhplus.be.server.common.outbox.domain.model;

import kr.hhplus.be.server.common.outbox.exception.InvalidOutboxStateException;

public enum OutboxStatus {

    PENDING {
        @Override
        public OutboxStatus process() {
            return PROCESSING;
        }
    },

    PROCESSING {
        @Override
        public OutboxStatus publish() {
            return PUBLISHED;
        }

        @Override
        public OutboxStatus fail() {
            return FAILED;
        }
    },

    FAILED {
        @Override
        public OutboxStatus retry() {
            return PROCESSING;
        }

        @Override
        public OutboxStatus dead() {
            return DEAD;
        }
    },

    PUBLISHED,

    DEAD;

    public OutboxStatus process() {
        throw new InvalidOutboxStateException(this, OutboxAction.PROCESS);
    }

    public OutboxStatus publish() {
        throw new InvalidOutboxStateException(this, OutboxAction.PUBLISH);
    }

    public OutboxStatus fail() {
        throw new InvalidOutboxStateException(this, OutboxAction.FAIL);
    }

    public OutboxStatus retry() {
        throw new InvalidOutboxStateException(this, OutboxAction.RETRY);
    }

    public OutboxStatus dead() {
        throw new InvalidOutboxStateException(this, OutboxAction.DEAD);
    }
}