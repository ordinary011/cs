package com.shpp.p2p.cs.ldebryniuk.assignment16;

public class Testing {

    public void stackTests() {
        MyStack<Integer> stack = new MyStack<>();

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        popAndCheck();
        System.out.println();
        System.out.println(stack.pop());
        System.out.println(stack.pop());

        stack.push(6);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push(7);
    }

    private void popAndCheck() {
        
    }

}
