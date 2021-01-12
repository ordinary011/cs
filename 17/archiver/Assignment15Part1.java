package com.shpp.p2p.cs.ldebryniuk.assignment17.archiver;

public class Assignment15Part1 {

    /**
     * @param args possible user inputs below:
     *             0 params:
     *             {""},
     *             1 param:
     *             {"test3"},
     *             {"test3.txt"},
     *             {"test3.par"},
     *             {"test3.txt.par"},
     *             2 params: the operation (compress or decompress) is determined based on the ending of zero param
     *             Lots of combinations are possible. Three of them can be found below:
     *             {"test3", "archived"}
     *             {"test3.txt", "test3.txt.par"}
     *             {"test3.txt.par", "test3_res.txt"}
     *             3 params: lots of combinations are possible but zero parameter must be "-a" or "-u" flag:
     *             Two possible examples of combinations can be found below:
     *             {"-a" "test3.txt", "archived_poem_poem.par"}
     *             {"-u" "archived_poem_poem.par", "test3.txt"}
     */
    public static void main(String[] args) {
        new HuffmanArchiver().determineOperation(args);
    }
}
