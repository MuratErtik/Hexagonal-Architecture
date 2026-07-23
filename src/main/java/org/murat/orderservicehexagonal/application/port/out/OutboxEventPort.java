package org.murat.orderservicehexagonal.application.port.out;

import org.murat.orderservicehexagonal.domain.event.OutboxMessage;

public interface OutboxEventPort {

    void save(OutboxMessage message);
}
