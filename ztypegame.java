// ZType game 

import tester.Tester;

import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.TextImage;
import java.util.Random;

class ZTypeWorld extends World {
  ILoWord loWords;
  Random rand;

  ZTypeWorld(ILoWord wrds) {
    this.loWords = wrds;
    this.rand = rand;
  }

  // renders the state of this ZTypeWorld  
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(300, 500);
    return this.loWords.draw(ws);
  }

  /// updates the world after every clock tick  
  public World onTick() {
    if (this.loWords.firstBottom()) {
      return this.endOfWorld("A message");
    }
    Utils u = new Utils();
    return new ZTypeWorld(this.loWords.moveWords()
        .addToEnd(new InactiveWord(u.randLetter(new Random().nextInt(26), "", 6),
            ((new Random().nextInt(4) * 50) + 50), 0)));
  } 


  /// handles key events for this ZTypeWorld 
  public World onKeyEvent(String key) {
    if (key.equals(key)) { 
      return new ZTypeWorld(this.loWords.filterOutEmpties().checkAndReduce(key).firstActive()); 
    }
    else { 
      return this; 
    }
  }

  public WorldScene lastScene(String msg) {
    return new WorldScene(300, 500)
        .placeImageXY(new TextImage("YOU LOSE!", 50, Color.black), 150, 250);
  }
} 

//represents a list of words
interface ILoWord {

  // creates an ILoWord where all ActiveWords are reduced by first letter,
  // if first letter matches given letter
  ILoWord checkAndReduce(String str);

  // creates an ILoWord that adds an IWord at the end
  ILoWord addToEnd(IWord w);

  // creates an ILoWord by removing all empty strings from a given ILoWord
  ILoWord filterOutEmpties();

  // draws all of the words in the ILoWord onto a World Scene
  WorldScene draw(WorldScene wrld);

  // creates an ILoWord that moves y-coordinates down by 25 pixels
  ILoWord moveWords();


  // creates an ILoWord where only the first IWord is Active
  ILoWord firstActive();

  //checks whether the first word has reached the bottom of the screen 
  boolean firstBottom(); // check if needs to be public
}


//represents an empty list of words
class MtLoWord implements ILoWord {

  /*
   *  TEMPLATE: --------- 
   * Methods: 
   * ... this.checkAndReduce(String)...     -- ILoWord 
   * ... this.addToEnd(IWord)...            -- ILoWord 
   * ... this.filterOutEmpties()...         -- ILoWord 
   * ... this.draw(WorldScene)...           -- WorldScene 
   * ... this.moveWords() ...               -- ILoWord 
   * ... this.firstActive()...              -- ILoWord 
   * ... this.firstBottom()...              -- boolean 
   * 
   * Methods for Fields: ...
   *  ... this.checkAndReduce(String s)... -- ILoWord 
   *  ...this.addToEnd(IWord, s) ...       -- ILoWord 
   *  ...this.filterOutEmpties()...        -- ILoWord 
   *  ... this.draw(WorldScene, wrld)...   -- WorldScene
   *  ... this.moveWords()...              -- ILoWord 
   *  ... this.firstActive()...            -- ILoWord 
   *  ... this.firstBottom()...            -- ILoWord 
   */


  // creates an ILoWord that has any first characters that begin 
  // with the given string removed 
  public ILoWord checkAndReduce(String str) {
    return new MtLoWord();
  }

  // creates an ILoWord that adds a random IWord at the end
  public ILoWord addToEnd(IWord w) {
    return new ConsLoWord(w, new MtLoWord());
  }

  // creates an ILoWord by removing all empty strings from a given ILoWord
  public ILoWord filterOutEmpties() {
    return this;
  }

  // draws all of the words in this ILoWord onto a world scene
  public WorldScene draw(WorldScene wrld) {
    return wrld;
  }

  // creates an ILoWord with all y-coordinates decreased by 25 pixels 
  public ILoWord moveWords() {
    return this;
  }
  
  public ILoWord firstActive() {
    return this;
  }

  //checks whether the first word has reached the bottom of the screen 
  public boolean firstBottom() {
    return false;
  }


}

class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }
  /*
   * 
   * TEMPLATE: --------- 
   * Fields: 
   * ... this.first ... -- IWord 
   * ... this.rest ... -- ILoWord
   * 
   * Methods: 
   * ... this.checkAndReduce(String)...     -- ILoWord 
   * ... this.addToEnd(IWord)...            -- ILoWord 
   * ... this.filterOutEmpties()...         -- ILoWord 
   * ... this.draw(WorldScene)...           -- WorldScene 
   * ... this.moveWords() ...               -- ILoWord 
   *... this.firstActive()...              -- ILoWord 
   * ... this.firstBottom()...              -- boolean 
   * 
   * Methods for Fields: ...
   *  ... this.rest.checkAndReduce(String s)... -- ILoWord 
   *  ... this.rest.addToEnd(IWord, s) ...       -- ILoWord 
   *  ... this.rest.filterOutEmpties()...        -- ILoWord 
   *  ... this.rest.draw(WorldScene, wrld)...   -- WorldScene
   *  ... this.rest.moveWords()...              -- ILoWord 
   *  ... this.rest.firstActive()...            -- ILoWord 
   *  ... this.rest.firstBottom()...            -- ILoWord 
   */


  // creates an ILoWord where all ActiveWords are reduced by first letter,
  // if first letter matches given letter
  public ILoWord checkAndReduce(String str) {
    if (this.first.substringCheck(str)) {
      return new ConsLoWord(this.first.substringHelp(), this.rest.checkAndReduce(str));
    }
    else {
      return new ConsLoWord(this.first, this.rest.checkAndReduce(str));
    }
  }

  // creates an ILoWord that adds an IWord at the end of the list
  public ILoWord addToEnd(IWord w) {
    /*
     * methods of w:
     * w.substringCheck(String)               -- boolean
     * w.isEmpty()                            -- boolean 
     * w.drawWord(WorldScene)                 -- WorldScene
     * w.moveWord()                           -- IWord 
     * w.makeActive()                         -- IWord 
     * w.reachBottom()                        -- boolean
     * 
     */

    return new ConsLoWord(this.first, this.rest.addToEnd(w)); 
  }

  // creates an ILoWord by removing all empty strings from a given ILoWorde
  public ILoWord filterOutEmpties() {
    if (this.first.isEmpty()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // draws all of the words in this ILoWord onto a world scene
  public WorldScene draw(WorldScene wrld) {
    return this.rest.draw(this.first.drawWord(wrld));
  }

  // creates an ILoWord with all y coordinates decreased by 25 pixels 
  public ILoWord moveWords() {
    return new ConsLoWord(this.first.moveWord(), this.rest.moveWords());
  }

  // creates an ILoWord where the first in the list is an Active word 
  public ILoWord firstActive() {
    return new ConsLoWord(this.first.makeActive(), this.rest);
  }

  //checks whether the first word has reached the bottom of the screen 
  public boolean firstBottom() {
    return this.first.reachBottom();
  }
}

//represents a word in the ZType game
interface IWord {


  // checks that the first letter is the same as the string
  boolean substringCheck(String str);

  // removes the first letter of an IWord
  IWord substringHelp();

  // checks if the IWord is an empty string
  boolean isEmpty();

  // draws an IWord onto a world scene
  WorldScene drawWord(WorldScene wrld);

  // creates a new IWord with the y coordinate decreased by 25 pixels 
  IWord moveWord();

  // turns an Inactive IWord into an Active IWord
  IWord makeActive();

  // checks if the IWord has reached the bottom of the screen 
  boolean reachBottom();


}

abstract class AWord implements IWord {
  String word;
  int x;
  int y;

  AWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  //checks that the first letter is the same as the string
  public boolean substringCheck(String str) {
    return this.word.toLowerCase().substring(0, 1).equals(str);
  }

  //removes the first letter of an IWord
  abstract public IWord substringHelp();

  //checks if the IWord is an empty string  
  public boolean isEmpty() {
    return this.word.equals("");   
  }

  //draws an Active IWord on a World Scene
  abstract public WorldScene drawWord(WorldScene wrld);

  //creates a new IWord with the y coordinate decreased by 25 pixels
  abstract public IWord moveWord(); 

  // turns an Inactive IWord into an Active IWord
  abstract public IWord makeActive();

  // checks if the IWord has reached the bottom of the screen 
  public boolean reachBottom() {
    return this.y >= 500;
  }
}

//represents an active word in the ZType game 
class ActiveWord extends AWord {
  ActiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  /* TEMPLATE ------ -
   * Fields: 
   * ...this.word...        -- String 
   * ...this.x...           -- int 
   * ...this.y...           -- int 
   * 
   * Methods: 
   * ...this.substringCheck(String str)....       -- boolean 
   * ...this.substringHelp()...                   -- IWord 
   * ...this.isEmpty()...                         -- boolean 
   * ...this.drawWord(WorldScene wrld)...         -- WorldScene 
   * ...this.moveWord()...                        -- IWord 
   * ...this.makeActive()...                      -- IWord 
   * ...this.reachBottom()...                     -- boolean 
   * 
   * Methods for Fields: 
   * 
   */

  //removes the first letter of an ActiveWord
  public IWord substringHelp() {
    return new ActiveWord(this.word.substring(1), this.x, this.y);
  }

  //draws an Active Word on a WorldScene (fix field of field)
  public WorldScene drawWord(WorldScene wrld) {
    return wrld.placeImageXY(new TextImage(this.word, 20, Color.blue), this.x, this.y);
  }

  //creates a new IWord with the y coordinate decreased by 25 pixels
  public IWord moveWord() {
    return new ActiveWord(this.word, this.x, (this.y + 25));
  }

  //turns an Inactive IWord into an Active IWord 
  public IWord makeActive() {
    return this; 
  }
}


//represents an inactive word in the ZType game
class InactiveWord extends AWord {
  InactiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  /* TEMPLATE ------ -
   * Fields: 
   * ...this.word...        -- String 
   * ...this.x...           -- int 
   * ...this.y...           -- int 
   * 
   * Methods: 
   * ...this.substringCheck(String str)....       -- boolean 
   * ...this.substringHelp()...                   -- IWord 
   * ...this.isEmpty()...                         -- boolean 
   * ...this.drawWord(WorldScene wrld)...         -- WorldScene 
   * ...this.moveWord()...                        -- IWord 
   * ...this.makeActive()...                      -- IWord 
   * ...this.reachBottom()...                     -- boolean 
   * 
   * Methods for Fields: 
   * 
   */

  // removes the first letter of an InactiveWord 
  public IWord substringHelp() {
    return this;
  }

  // draws an Inactive Word on a WorldScene
  public WorldScene drawWord(WorldScene wrld) {
    return wrld.placeImageXY(new TextImage(this.word, 20, Color.red), this.x, this.y);
  }

  // creates a new IWord with the y coordinate decreased by 25 pixels 
  public IWord moveWord() {
    return new InactiveWord(this.word, this.x, (this.y + 25));
  }

  // turns an Inactive IWord into an Active IWord
  public IWord makeActive() {
    return new ActiveWord(this.word, this.x, this.y);
  }
}

class Utils { 
  String LETTERS = "abcdefghijklmnopqrstuvwxyz";

  String randLetter(int number, String str, int max) {
    if (str.length() < max) {
      return randLetter(new Random().nextInt(26), 
          str + LETTERS.substring(number, number + 1), max);
    }
    else if (1 > str.length()) {
      return randLetter(new Random().nextInt(26), 
          LETTERS.substring(number, number + 1), max);
    }
    else {
      return str;
    }
  }

  // test for randLetter 
  String testingrandLetter(int number, String str, int max, int tester) {
    if (str.length() < max) {
      return testingrandLetter(new Random(tester).nextInt(tester), 
          str + LETTERS.substring(number, number + 1),
          max, tester);
    }
    else if (1 > str.length()) {
      return testingrandLetter(new Random(tester).nextInt(tester), 
          LETTERS.substring(number, number + 1), max, tester);  
    }
    else {
      return str;
    }
  }
}



class ExamplesZType {

  IWord apple = new ActiveWord("apple", 150, 10);
  IWord zapple = new ActiveWord("zapple", 120, 200);
  IWord Apple = new ActiveWord("Apple", 110, 300);
  IWord dogs = new ActiveWord("dogs", 150, 230);
  IWord French = new ActiveWord("French", 111, 30);
  IWord computer = new ActiveWord("computer", 125, 210);
  IWord desks = new ActiveWord("desks", 124, 50);
  IWord zebra = new ActiveWord("zebra", 7, 19);
  IWord trash = new ActiveWord("trash", 9, 12);
  IWord window = new ActiveWord("window", 14, 18);
  IWord war = new ActiveWord("war", 21, 17);
  IWord Wish = new ActiveWord("Wish", 14, 18);
  IWord ar = new ActiveWord("ar", 21, 17);
  IWord ish = new ActiveWord("ish", 14, 18);
  IWord mtStr = new ActiveWord("", 12, 2);
  IWord desksmoved = new ActiveWord("desks", 124, 75);
  IWord frenchmoved = new ActiveWord("French", 111, 55);
  IWord applemoved = new ActiveWord("apple", 150, 35);

  IWord laptop = new InactiveWord("laptop", 13, 17);
  IWord shoes = new InactiveWord("cry", 21, 25);
  IWord trees = new InactiveWord("trees", 26, 29);
  IWord waste = new InactiveWord("waste", 13, 2);
  IWord bottom = new InactiveWord("bottom", 50, 500);

  ILoWord mt = new MtLoWord();

  ILoWord listInactive = new ConsLoWord(this.laptop, this.mt);
  ILoWord list1 = new ConsLoWord(this.apple, this.mt);
  ILoWord list2 = new ConsLoWord(this.French, this.list1); 
  ILoWord list3 = new ConsLoWord(this.desks, this.list2);
  ILoWord list5 = new ConsLoWord(this.French, this.mt);
  ILoWord list6 = new ConsLoWord(this.window, this.list1);
  ILoWord list7 = new ConsLoWord(this.zebra, this.list6);
  ILoWord zebraMt = new ConsLoWord(this.zebra, this.mt);

  ILoWord list8 = new ConsLoWord(this.computer,
      new ConsLoWord(this.dogs, new ConsLoWord(this.trash, this.mt)));


  ILoWord list9 = new ConsLoWord(this.computer,
      new ConsLoWord(this.dogs, new ConsLoWord(this.trash, new ConsLoWord(this.apple, this.mt))));

  ILoWord similarWordsList1 = new ConsLoWord(this.trash,
      new ConsLoWord(this.war, new ConsLoWord(this.Wish, new ConsLoWord(this.waste, this.mt))));

  ILoWord reducedList1 = new ConsLoWord(this.trash,
      new ConsLoWord(this.ar, new ConsLoWord(this.ish, new ConsLoWord(this.waste, this.mt))));

  ILoWord listWEmpties1 = new ConsLoWord(this.computer,
      new ConsLoWord(this.mtStr, new ConsLoWord(this.trash, this.mt)));

  ILoWord listRemovedEmpties1 = new ConsLoWord(this.computer, new ConsLoWord(this.trash, this.mt));

  ILoWord list3Moved = new ConsLoWord(this.desksmoved, 
                         new ConsLoWord(this.frenchmoved, 
                             new ConsLoWord(this.applemoved, this.mt)));

  ILoWord listBottom = new ConsLoWord(this.bottom, new ConsLoWord(this.apple, this.mt));



  // test substringCheck
  boolean testsubstringCheck(Tester t) {
    return t.checkExpect(this.dogs.substringCheck("d"), true)
        && t.checkExpect(this.dogs.substringCheck("f"), false)
        && t.checkExpect(this.French.substringCheck("f"), true);
  }

  // test substringHelp
  boolean testsubstringHelp(Tester t) {
    return t.checkExpect(this.war.substringHelp(), this.ar)
        && t.checkExpect(this.Wish.substringHelp(), this.ish);
  }

  // test checkAndReduce
  boolean testcheckAndReduce(Tester t) {
    return t.checkExpect(this.similarWordsList1.checkAndReduce("w"), reducedList1)
        && t.checkExpect(this.mt.checkAndReduce("f"), this.mt)
        && t.checkExpect(this.similarWordsList1.checkAndReduce("r"), similarWordsList1);
  }

  // test addToEnd
  boolean testcheckaddToEnd(Tester t) {
    return t.checkExpect(this.list8.addToEnd(this.apple), list9)
        && t.checkExpect(this.mt.addToEnd(this.apple), new ConsLoWord(this.apple, this.mt));
  }

  // test isEmpty
  boolean testisEmpty(Tester t) {
    return t.checkExpect(this.mtStr.isEmpty(), true) && t.checkExpect(this.apple.isEmpty(), false);
  }


  // test filterOutEmpties
  boolean testfilterOutEmpties(Tester t) {
    return t.checkExpect(this.listWEmpties1.filterOutEmpties(), listRemovedEmpties1)
        && t.checkExpect(this.list8.filterOutEmpties(), list8)
        && t.checkExpect(this.mt.filterOutEmpties(), this.mt);
  }

  // test moveWord 
  boolean testmoveWord(Tester t) {
    return t.checkExpect(this.apple.moveWord(), new ActiveWord("apple", 150, 35))
        && t.checkExpect(this.laptop.moveWord(), new InactiveWord("laptop", 13, 42));
  }

  // test moveWords 
  boolean testmoveWords(Tester t) {
    return t.checkExpect(this.mt.moveWords(), this.mt)
        && t.checkExpect(this.list3.moveWords(), this.list3Moved);
  }

  // test for makeActive 
  boolean testmakeActive(Tester t) {
    return t.checkExpect(this.laptop.makeActive(), new ActiveWord("laptop", 13, 17))
        && t.checkExpect(this.waste.makeActive(), new ActiveWord("waste", 13, 2));
  }

  // test for reachBottom
  boolean testreachBottom(Tester t) {
    return t.checkExpect((new ActiveWord("reach", 200, 500).reachBottom()), true)
        && t.checkExpect((new InactiveWord("bottom", 200, 501).reachBottom()), true)
        && t.checkExpect((new ActiveWord("hi", 200, 499).reachBottom()), false);
  }

  // test for firstBottom
  boolean testfirstBottom(Tester t) {
    return //t.checkExpect(this.mt, false)
        t.checkExpect(this.listBottom, true)
        && t.checkExpect(this.list3, false);
  }


  // big bang 
  boolean testBigBang(Tester t) {
    ZTypeWorld w = new ZTypeWorld(this.list1);
    int worldWidth = 300;
    int worldHeight = 500; 
    double tickRate = .8;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }
}

