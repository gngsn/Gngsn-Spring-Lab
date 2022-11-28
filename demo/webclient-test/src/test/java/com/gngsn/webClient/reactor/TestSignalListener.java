package com.gngsn.webClient.reactor;

import reactor.core.Fuseable;
import reactor.core.observability.SignalListener;
import reactor.core.publisher.SignalType;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TestSignalListener<T> implements SignalListener<T> {

    /**
     * The String representation of the events, or doOnXxx methods.
     */
    public final Deque<String> events = new ConcurrentLinkedDeque<>();

    /**
     * The errors passed to the {@link #handleListenerError(Throwable)} hook. Unused by default.
     */
    public final Deque<Throwable> listenerErrors = new ConcurrentLinkedDeque<>();

    @Override
    public void doFirst() throws Throwable {
        events.offer("doFirst");
    }

    @Override
    public void doFinally(SignalType terminationType) throws Throwable {
        events.offer("doFinally:" + terminationType.name());
    }

    @Override
    public void doOnSubscription() throws Throwable {
        events.offer("doOnSubscription");
    }

    @Override
    public void doOnFusion(int negotiatedFusion) throws Throwable {
        events.offer("doOnFusion:" + Fuseable.fusionModeName(negotiatedFusion));
    }

    @Override
    public void doOnRequest(long requested) throws Throwable {
        events.offer("doOnRequest:" + (requested == Long.MAX_VALUE ? "unbounded" : requested));
    }

    @Override
    public void doOnCancel() throws Throwable {
        events.offer("doOnCancel");
    }

    @Override
    public void doOnNext(T value) throws Throwable {
        events.offer("doOnNext:" + value);
    }

    @Override
    public void doOnComplete() throws Throwable {
        events.offer("doOnComplete");
    }

    @Override
    public void doOnError(Throwable error) throws Throwable {
        events.offer("doOnError:" + error);
    }

    @Override
    public void doAfterComplete() throws Throwable {
        events.offer("doAfterComplete");
    }

    @Override
    public void doAfterError(Throwable error) throws Throwable {
        events.offer("doAfterError:" + error);
    }

    @Override
    public void doOnMalformedOnNext(T value) throws Throwable {
        events.offer("doOnMalformedOnNext:" + value);
    }

    @Override
    public void doOnMalformedOnError(Throwable error) throws Throwable {
        events.offer("doOnMalformedOnError:" + error);
    }

    @Override
    public void doOnMalformedOnComplete() throws Throwable {
        events.offer("doOnMalformedOnComplete");
    }

    @Override
    public void handleListenerError(Throwable listenerError) {
        listenerErrors.offer(listenerError);
    }
}
