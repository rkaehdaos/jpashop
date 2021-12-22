package jpabook.jpashop.hello;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ClassWithThreadingProblem {
    private int lastIdUsed;

    public ClassWithThreadingProblem(int lastIdUsed) {
        this.lastIdUsed = lastIdUsed;
    }

    public int getNextId() {
        return ++lastIdUsed;
    }

    public int getLastIdUsed() {
        return lastIdUsed;
    }

    public static void main(String args[]) {
        final ClassWithThreadingProblem classWithThreadingProblem =
                new ClassWithThreadingProblem(42);

        Runnable runnable = new Runnable() {
            public void run() {
                StringBuilder str = new StringBuilder();
                str.append("thread").append(Thread.currentThread().getName())
                        .append(", id: ").append(classWithThreadingProblem.getNextId())
                        .append(", lastIdUsed: ").append(classWithThreadingProblem.lastIdUsed);
                log.info(str.toString());
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }
}