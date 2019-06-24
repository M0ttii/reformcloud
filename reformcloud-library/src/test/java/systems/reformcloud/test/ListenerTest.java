/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.test;

import org.junit.Test;
import systems.reformcloud.event.DefaultEventManager;
import systems.reformcloud.event.abstracts.EventManager;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.event.utility.Listener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

public final class ListenerTest implements Serializable {
    @Test
    public void listenerTest() {
        EventManager eventManager = new DefaultEventManager();
        eventManager.registerListener(new ListenerTest0());
        eventManager.fire(new TestEvent(false));
    }

    public class TestEvent extends Event {
        boolean test;

        @java.beans.ConstructorProperties({"test"})
        TestEvent(boolean test) {
            this.test = test;
        }

        boolean isTest() {
            return this.test;
        }
    }

    public class ListenerTest0 implements Listener {
        @Handler
        public void handle(TestEvent testEvent) {
            System.out.println("THE EVENT SAYS: " + testEvent.isTest());
        }

        @Handler
        public void handle0(TestEvent testEvent) {
            System.out.println("THE SECOND HANDLER SAYS: " + testEvent.isTest());
        }
    }
}
