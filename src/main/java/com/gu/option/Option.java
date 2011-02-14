package com.gu.option;

import java.util.Collections;
import java.util.Iterator;

public abstract class Option<T> implements Iterable<T> {

    interface Function<T, S> {
        S apply(T x);
    }

    interface UnitFunction<T> {
        void apply(T x);
    }

    public abstract T get();
    public abstract T getOrElse(T orElse);
    public abstract Option<T> orElse(T orElse);
    public abstract boolean isDefined();
    public boolean isEmpty() { return !isDefined(); }
    public abstract boolean exists(Function<T,Boolean> f);
    public abstract Option<T> filter(Function<T,Boolean> f);
    public abstract <S> Option<S> map(Function<T,S> f);
    public abstract <S> Option<S> flatMap(Function<T,Option<S>> f);
    public abstract void foreach(UnitFunction<T> f);

    public static <S> Option<S> some(S s) { return new Some<S>(s); }
    public static <S> Option<S> none() { return new None<S>(); }

    public static <S> Option<S> option(S s) {
        if (s == null) {
            return none();
        }

        return some(s);
    }

    public static class Some<T> extends Option<T> {
        private final T value;

        public Some(T value) { this.value = value; }

        public T get() { return value; }
        public T getOrElse(T orElse) { return this.get(); }
        public Option<T> orElse(T orElse) { return this; }

        public boolean isDefined() { return true; }

        public boolean exists(Function<T,Boolean> f) {
            return this.map(f).get();
        }

        public Option<T> filter(Function<T,Boolean> f) {
            if (!this.exists(f)) {
                return none();
            }

            return this;
        }

        public <S> Option<S> map(Function<T,S> f) {
            return some(f.apply(this.get()));
        }

        public <S> Option<S> flatMap(Function<T,Option<S>> f) {
            return f.apply(this.get());
        }

        public void foreach(UnitFunction<T> f) {
            f.apply(this.get());
        }

        public Iterator<T> iterator() {
            return Collections.singletonList(this.get()).iterator();
        }

        public String toString() {
            return String.format("Some(%s)", get().toString());
        }
    }

    public static class None<T> extends Option<T> {
        public None() {}

        public T get() {
            throw new UnsupportedOperationException("Cannot resolve value on None");
        }

        public T getOrElse(T orElse) { return orElse; }
        public Option<T> orElse(T orElse) { return some(orElse); }

        public boolean isDefined() { return false; }
        public boolean exists(Function<T,Boolean> f) { return false; }
        public Option<T> filter(Function<T,Boolean> f) { return this; }

        public <S> Option<S> map(Function<T,S> f) { return none(); }
        public <S> Option<S> flatMap(Function<T,Option<S>> f) { return none(); }

        public void foreach(UnitFunction<T> f) { }

        public Iterator<T> iterator() { return Collections.<T>emptyList().iterator(); }

        public String toString() { return "None"; }
    }
}

