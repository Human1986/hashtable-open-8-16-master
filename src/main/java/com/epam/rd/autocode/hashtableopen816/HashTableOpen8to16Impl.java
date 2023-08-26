package com.epam.rd.autocode.hashtableopen816;


import java.util.ArrayList;
import java.util.List;

class HashtableOpen8to16Impl implements HashtableOpen8to16 {

    private static final int INITIAL_CAPACITY = 8;
    private static final int MAX_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.25;
    int count;
    private int capacity;
    private int size;
    private List<Integer> keys;
    private Object[] values;


    public HashtableOpen8to16Impl() {
        this.capacity = INITIAL_CAPACITY;
        this.size = 0;
//        this.keys = new int[capacity];
        this.values = new Object[capacity];
        this.keys = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            keys.add(0);
        }

    }

    @Override
    public void insert(int key, Object value) {

        int index = hash(key);

        for (int i = 0; i < capacity; i++) {
            if (key == 0 && count != 1) {
                keys.set(index, key);
                values[index] = value;
                size++;
                count = 1;
                break;
            }
            if (keys.get(index) == key) {
                values[index] = value;
                break;
            } else {
                if (keys.get(index) == 0 && values[index] == null) {

                    keys.set(index, key);
                    values[index] = value;
                    size++;
                    break;
                } else {
                    index = (index + 1) % capacity;
                    if (index >= capacity) index = 0;
                }
            }
        }
        if (size == capacity) {
            resize();
        }
    }


    private void resize() {
        count = 0;
        int newCapacity = Math.max(capacity * 2, MAX_CAPACITY);
        List<Integer> newKeys = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newKeys.add(0);
        }
        Object[] newValues = new Object[newCapacity];

        for (int i = 0; i < capacity; i++) {
            int newIndex = Math.abs(keys.get(i) % newCapacity);
            if (keys.get(i) == 0 && count != 1) {
                newKeys.set(newIndex, keys.get(i));
                newValues[newIndex] = values[i];
                count = 1;
            } else {
                if (keys.get(i) != 0 && values[i] != null) {
                    while (newKeys.get(newIndex) != 0) {
                        newIndex = (newIndex + 1) % newCapacity;
                    }
                    newKeys.set(newIndex, keys.get(i));
                    newValues[newIndex] = values[i];
                }
            }
        }
        capacity = newCapacity;
        keys = newKeys;
        values = newValues;
    }

    private int hash(int key) {
        return Math.abs(key % capacity);
    }

    @Override
    public Object search(int key) {
        int index = hash(key);
        for (int i = 0; i < capacity; i++) {
            if (keys.get(index) != 0 && keys.get(index) == key) {
                return values[index];
            } else {
                index++;
                if (index == keys.size()) return null;
            }
        }
        return null;
    }

    @Override
    public void remove(int key) {
        int index = hash(key);
        while (keys.get(index) != 0) {
            if (keys.get(index) == key) {
                keys.set(index, 0);
                values[index] = null;
                size--;
                if (size > 0 && size <= capacity * LOAD_FACTOR_THRESHOLD) {
                    shrink();
                }
                return;
            }
            index = (index + 1) % capacity;
        }

    }

    private void shrink() {
        int newCapacity = Math.min(capacity / 2, INITIAL_CAPACITY);
        List<Integer> newKeys = new ArrayList<>(newCapacity);
        Object[] newValues = new Object[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newKeys.add(0);
        }

        for (int i = 0; i < capacity; i++) {
            if (keys.get(i) != 0) {
                int newIndex = Math.abs(keys.get(i) % newCapacity);
                while (newKeys.get(newIndex) != 0) {
                    newIndex = (newIndex + 1) % newCapacity;
                }
                newKeys.set(newIndex, keys.get(i));
                newValues[newIndex] = values[i];
            }
        }
        capacity = newCapacity;
        keys = newKeys;
        values = newValues;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int[] keys() {
        int index = 0;
        int[] arrayKeys = new int[capacity];
        for (int key : keys) {
            arrayKeys[index++] = key;
        }
        return arrayKeys;
    }

}
