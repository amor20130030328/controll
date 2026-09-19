package com.amore.springboot.explore.test;

import java.util.Arrays;
import java.util.List;

public class Main7 {

    private interface Tree {
        void invoke(Visitor visitor);
    }

    private static class BinaryTree implements Tree {

        public void invoke(Visitor visitor) {
            visitor.visit(this);
        }
    }

    private static class MultiwayTree implements Tree {
        public void invoke(Visitor visitor) {
            visitor.visit(this);
        }
    }

    private interface Visitor {
        void visit(BinaryTree binaryTree);

        void visit(MultiwayTree tree);
    }

    private static class  BFSVisitor implements Visitor {

        @Override
        public void visit(BinaryTree binaryTree) {
            System.out.println("BFSVisitor-BinaryTree visit");
        }

        @Override
        public void visit(MultiwayTree multiwayTree) {
            System.out.println("BFSVisitor-MultiwayTree visit");
        }
    }

    private static class DFSVisitor implements Visitor {

        @Override
        public void visit(BinaryTree binaryTree) {
            System.out.println("DFSVisitor-BinaryTree visit");
        }

        @Override
        public void visit(MultiwayTree tree) {
            System.out.println("DFSVisitor-MultiwayTree visit");
        }
    }

    public static void main(String[] args) {

        List<Tree> treeList = Arrays.asList(
                new BinaryTree(),
                new MultiwayTree()
        );

        List<Visitor> visitorList = Arrays.asList(new BFSVisitor(), new DFSVisitor());
        treeList.forEach(tree->{visitorList.forEach(tree::invoke);});

    }
}
