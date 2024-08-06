package com.assets.binfinder.util;


import androidx.annotation.Nullable;

public class Event<T> {
    private final T content;
    private boolean hasBeenHandled;

    public Event(T content) {
        this.content = content;
    }

    public final boolean getHasBeenHandled() {
        return this.hasBeenHandled;
    }


    @Nullable
    public final T getContentIfNotHandled() {
        if (this.hasBeenHandled) {
            return null;
        } else {
            this.hasBeenHandled = true;
            return this.content;
        }
    }

    public final T peekContent() {
        return this.content;
    }
}