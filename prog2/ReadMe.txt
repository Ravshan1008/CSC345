This ReadMe.txt file should have been found packaged with the
following Java .class and .java files:

    Bones.class
    ComputerStrategy.class
    ComputerStrategy$TPair.class
    MilquetoastStrategy.class
    MilquetoastStrategy.java
    Player.class
    Strategy.class
    Strategy.java
    StrategyLoader.class

The .class files were compiled using Java 16.0.2 on lectura; if you try to
use them on your own computer, they may not work.  You should place
these files in an otherwise un-cluttered folder/subdirectory on lectura.  

To create .class versions of your Graph, Map, and Territory classes,
just use javac to compile each of those files.  E.g.:

  javac Graph.java

With or without a main() method in Graph.java, javac will create a Graph.class
file that can be used with your other two .class files and those that we
have supplied.  Read on!

When you have compiled your Territory.class, Map.class, and Graph.class files,
copy them into the folder/subdirectory containing the prog2.zip files.
Then, just run the game from the command line:

  java Bones

If you wrote your classes correctly and completely, the game should be
completely playable.  If the game crashes, well, odds are good that
you have some debugging to do.

--lim, 2024/09/22
