package cn.it.learning.refactor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * java8中自带的BiConsumer是不会抛出异常的，为了支持执行自定义操作时能够将具体的异常抛出，重写了一个可以抛出异常的Consumer
 * 支持两个参数的BiConsumer
 * @author it-learning-diary
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {
    /**
     * 重写accept方法，捕获并抛出异常
     *
     * @param t
     */
    @Override
    default void accept(T t, U u) {
        try {
            acceptBase(t, u);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void acceptBase(T t, U u);
}