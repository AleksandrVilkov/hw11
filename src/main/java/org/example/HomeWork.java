package org.example;


import lombok.SneakyThrows;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class HomeWork {

    private static final String SPACE = " ";
    private final TreeMap<Integer, Integer> alterationsMap = new TreeMap<>();
    private final TreeMap<Integer, Integer> lengths = new TreeMap<>();

    private int choreographyLength;
    private int alterations;

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу Step из файла contest6_tasks.pdf
     */
    @SneakyThrows
    public void stepDanceValue(InputStream in, OutputStream out) {
        var reader = new BufferedReader(new InputStreamReader(in));
        var writer = new PrintWriter(out);

        fillPrimaryData(reader);

        for (int i = 0; i < alterations; i++) {
            int inputKey = Integer.parseInt(reader.readLine()) - 1;
            Map.Entry<Integer, Integer> pair = alterationsMap.higherEntry(inputKey);
            removeChoreography(pair);

            int key = pair.getKey();
            int nextKey = key;
            Map.Entry<Integer, Integer> higherPair = alterationsMap.higherEntry(pair.getKey());
            if (key != choreographyLength)
                nextKey = higherPair != null ? higherPair.getKey() : choreographyLength;


            int value = pair.getValue();
            int nextValue = value == 0 ? value : alterationsMap.get(value);
            if (value != 0 && value == inputKey) removeChoreography(alterationsMap.lowerEntry(pair.getKey()));
            if (key == getNextKey(inputKey) && key != choreographyLength && higherPair != null)
                removeChoreography(higherPair);


            if (inputKey == value) {
                if (key == getNextKey(inputKey)) {
                    addChoreography(nextValue, nextKey);
                } else {
                    addChoreography(nextValue, getNextKey(inputKey));
                    addChoreography(getNextKey(inputKey), key);
                }
                writer.println(lengths.lastKey());
                continue;
            }
            if (key == getNextKey(inputKey)) {
                addChoreography(value, inputKey);
                addChoreography(inputKey, nextKey);
                writer.println(lengths.lastKey());
                continue;
            }
            addChoreography(value, inputKey);
            addChoreography(inputKey, getNextKey(inputKey));
            addChoreography(getNextKey(inputKey), key);
            writer.println(lengths.lastKey());
        }
        writer.flush();
    }

    private int getNextKey(int inputKey) {
        return inputKey + 1;
    }

    @SneakyThrows
    private void fillPrimaryData(BufferedReader reader) {
        var arr = reader.readLine().split(SPACE);
        choreographyLength = Integer.parseInt(arr[0]);
        alterations = Integer.parseInt(arr[1]);
        for (int i = 0; i < choreographyLength; i++) addChoreography(i, i + 1);

    }

    private void removeChoreography(Map.Entry<Integer, Integer> entry) {
        int length = entry.getKey() - entry.getValue();
        int count = lengths.get(length);
        if (count == 1) lengths.remove(length);
        else lengths.put(length, count - 1);
        alterationsMap.remove(entry.getKey());
    }

    private void addChoreography(int start, int end) {
        lengths.put(end - start,
                lengths.getOrDefault(end - start, 0) + 1);
        alterationsMap.put(end, start);
    }
}
