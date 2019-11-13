package com.flamingo.comeon.rpc.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ServiceConsumerMediator {
    Map<Class<? extends ServiceConsumer>, ? extends ServiceConsumer> serviceConsumerRegistry;
    Map<Class<? extends ServiceConsumer>, Transporter> transporterRegistry = new HashMap<>();
    Map<Call, Object> waitingCalls = new HashMap<>();
    ExecutorService remoteCallExecutor = Executors.newSingleThreadExecutor();

    public <T extends ServiceConsumer> Object remoteCallSync(Class<T> serviceConsumer, Call call) {
        try {
            return remoteCallFuture(serviceConsumer, call).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends ServiceConsumer> Future remoteCallFuture(Class<T> serviceConsumer, Call call) {
        Transporter transporter = transporterRegistry.get(serviceConsumer);
        waitingCalls.put(call, null);
        transporter.send(call);
        FutureTask future = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Object ret = waitingCalls.get(call);
                while (ret == null) {
                    synchronized (call) {
                        call.wait();
                        ret = waitingCalls.get(call);
                    }
                }
                ret = waitingCalls.get(call);
                waitingCalls.remove(ret);
                return ret;
            }
        });
        remoteCallExecutor.execute(future);
        return future;
    }

    public void wireUpReturnForCall(Call call, Object ret) {
        waitingCalls.put(call, ret);
        synchronized (call) {
            call.notifyAll();
        }
    }

    public void registerTransporter(Class<? extends ServiceConsumer> consumer, Transporter transporter) {
        transporterRegistry.put(consumer, transporter);
    }

    public void shutdownExecutor() {
        remoteCallExecutor.shutdown();
        try {
            if (!remoteCallExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                remoteCallExecutor.shutdownNow();
                remoteCallExecutor.awaitTermination(10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            remoteCallExecutor.shutdownNow();
        }
    }
}
