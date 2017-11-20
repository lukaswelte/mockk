package io.mockk.proxy;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.Callable;

public class MockKProxyDispatcher extends MockKDispatcher {
    private static final Random RNG = new Random();
    private final long id = RNG.nextLong();

    private final MockKWeakMap<Object, MockKInvocationHandler> handlers;

    public MockKProxyDispatcher(MockKWeakMap<Object, MockKInvocationHandler> handlers) {
        this.handlers = handlers;
    }

    public long getId() {
        return id;
    }

    @Override
    public Callable<?> handle(final Object self, Method method, final Object[] arguments) throws Exception {
        final MockKInvocationHandler handler = handlers.get(self);
        if (handler == null) {
            return null;
        }
        if (MockKSelfCall.SELF_CALL.get() == self) {
            return null;
        }

        return new MockKCallProxy(handler, self, method, arguments);
    }
}
