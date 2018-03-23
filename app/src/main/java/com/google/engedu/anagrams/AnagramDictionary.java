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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    ArrayList<String> wordList=new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String,ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    int wordLength=DEFAULT_WORD_LENGTH;
    HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if(lettersToWord.containsKey(sortLetters(word))) {
                ArrayList<String> anagrams = lettersToWord.get(sortLetters(word));
                anagrams.add(word);
                lettersToWord.put(sortLetters(word),anagrams);
            }
            else {
                ArrayList<String> anagrams= new ArrayList<String>();
                anagrams.add(word);
                lettersToWord.put(sortLetters(word),anagrams);
            }
            if (sizeToWords.containsKey(word.length())) {
                ArrayList<String> anagrams = sizeToWords.get(word.length());
                anagrams.add(word);
                sizeToWords.put(word.length(),anagrams);
            }
            else {
                ArrayList<String> anagrams = new ArrayList<String>();
                anagrams.add(word);
                sizeToWords.put(word.length(),anagrams);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (base.contains(word)) {
            return false;
        }
        else if (!wordSet.contains(word)) {
            return false;
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        int i;
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord= sortLetters(targetWord);
        for (i=0;i<wordList.size();i++) {
            if(wordList.get(i).length() == targetWord.length()) {
                String sortedListWord=sortLetters(wordList.get(i));
                if (sortedWord.equalsIgnoreCase(sortedListWord)) {
                    result.add(wordList.get(i));
                }
            }
        }
        return result;
    }
    public String sortLetters (String targetWord) {
        char c[]=targetWord.toCharArray();
        Arrays.sort(c);
        return new String(c);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String temp;
        for (char ch = 'a'; ch<='z';ch++) {
            temp=word+Character.toString(ch);
            if(lettersToWord.containsKey(sortLetters(temp))) {
                ArrayList<String> listAnagrams = lettersToWord.get(sortLetters(temp));
                for(int i=0;i<listAnagrams.size();i++) {
                    if(isGoodWord(word,listAnagrams.get(i))) {
                        result.add(listAnagrams.get(i));
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        if (wordLength==MAX_WORD_LENGTH+1) {
            wordLength=DEFAULT_WORD_LENGTH;
        }
        while (true) {
            ArrayList<String> l = sizeToWords.get(wordLength);
            int i = random.nextInt(l.size());
            String temp = l.get(i);
            ArrayList<String> anagrams= getAnagramsWithOneMoreLetter(temp);
            if (anagrams.size() >= MIN_NUM_ANAGRAMS) {
                wordLength++;
                return temp;
            }
        }
    }
}
