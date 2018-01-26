/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private Random random = new Random();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String key = sortLetters(word); // Used to check if word is in hashmap
            wordSet.add(word);
            wordList.add(word);
            if (lettersToWord.containsKey(key))
                lettersToWord.get(key).add(word);
            else {
                ArrayList<String> anagrams = new ArrayList<String>(); // Adds ArrayList to key
                lettersToWord.put(key, anagrams);
            }
        }
        for (int i = 0; i < wordList.size(); i++) {
            if (!sizeToWords.containsKey(wordList.get(i).length())) {
                ArrayList<String> arrayOfWords = new ArrayList<String>();
                arrayOfWords.add(wordList.get(i));
                sizeToWords.put(wordList.get(i).length(), arrayOfWords);
            }
            else
                sizeToWords.get(wordList.get(i).length()).add(wordList.get(i));
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word) || word.contains(base))
            return false;
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        result.add(sortLetters(targetWord));
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        int lengthOfTheAlphabet = 26;
        for (int i = 0; i < lengthOfTheAlphabet; i++) {
            char newLetter =  (char)(i + 'a');
            String newWord = word + Character.toString(newLetter);
            String checkForWordInDictionary = sortLetters(newWord);
            if (lettersToWord.containsKey(checkForWordInDictionary)) {
                result.addAll(lettersToWord.get(checkForWordInDictionary));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        String randomWord;
        String randomWordSortedChars;
        int randomWordIndex = rand.nextInt(sizeToWords.get(wordLength).size()) + 1;
        randomWord = sizeToWords.get(wordLength).get(randomWordIndex);
        wordLength++;
        if (wordLength == MAX_WORD_LENGTH)
            wordLength = DEFAULT_WORD_LENGTH;
        return randomWord;
    }

    private static String sortLetters(String word) {
        char[] wordAsCharacterArray = word.toCharArray();
        Arrays.sort(wordAsCharacterArray);
        return String.valueOf(wordAsCharacterArray);
    }
}