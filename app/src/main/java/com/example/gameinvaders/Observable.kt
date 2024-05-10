package com.example.gameinvaders

typealias Observer<T> = (T) -> Unit

class Observable<T>(initialValue: T) {
    private var observers = mutableListOf<Observer<T>>()
    private var value: T = initialValue

    fun get(): T = value

    fun set(newValue: T) {
        if (value != newValue) {
            value = newValue
            notifyObservers()
        }
    }

    fun addObserver(observer: Observer<T>) {
        observers.add(observer)
    }

    fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it(value) }
    }
}
