package eg.edu.alexu.csd.filestructure.redblacktree;

import com.sun.corba.se.impl.resolver.INSURLOperationImpl;
import javafx.util.Pair;

public class RedBlackTree implements IRedBlackTree {
    INode root;
    @Override
    public INode getRoot() { return root; }

    @Override
    public boolean isEmpty() { return root==null; }

    @Override
    public void clear() {
        root=null;
    }

    @Override
    public Object search(Comparable key) {
        if(isEmpty())return null;
        INode temp=getNodeWithKey(key).getKey();
        if(temp==null)return null;
        return getNodeWithKey(key).getKey().getValue();
    }

    Pair<INode,INode> getNodeWithKey(Comparable key){
        INode curr=getRoot();
        if(curr==null)return new Pair<>(null,null);
        INode prev=curr;
        while (curr!=null&&!curr.isNull()&&curr.getKey().compareTo(key)!=0)
        {
            prev=curr;
            if(key.compareTo(curr.getKey())>0)
            {
                curr=curr.getRightChild();
            }
            else{
                curr=curr.getLeftChild();
            }
        }
        return new Pair<INode,INode>(curr,prev);
    }

    @Override
    public boolean contains(Comparable key) { return search(key)!=null; }

    public static void main(String[] args) {
        RedBlackTree redBlackTree=new RedBlackTree();
        redBlackTree.insert(311,1);
        redBlackTree.insert(6887,1);
        redBlackTree.insert(7095,1);
        redBlackTree.insert(4023,1);
        redBlackTree.insert(2482,1);
        redBlackTree=null;
    }
    @Override
    public void insert(Comparable key, Object value) {
        Pair<INode,INode> temp=getNodeWithKey(key);
        INode newNode=temp.getKey();
        INode p = temp.getValue();
        if(newNode!=null&&!newNode.isNull())
        {
            newNode.setValue(value);
            return;
        }
        else newNode =new Node();
        newNode.setKey(key);
        newNode.setValue(value);
        if (!isEmpty()){
            if(p.getKey().compareTo(key)>0)p.setLeftChild(newNode);
            else p.setRightChild(newNode);
            newNode.setParent(p);
        }
        rebalancedInsert(newNode);
        //if(newNode.getLeftChild()==null&&newNode.getRightChild()==null)newNode.setColor(INode.BLACK);
    }

    private void rebalancedInsert(INode newNode) {
        if (isEmpty())
        {
            this.root=newNode;
            root.setColor(INode.BLACK);
        }
        else {
            newNode.setColor(INode.RED);
            while (newNode.getParent()!=null&&newNode.getParent().getColor()==INode.RED&&newNode.getColor()==INode.RED)
            {
                INode s;
                if(newNode.getParent().getParent().getRightChild()==newNode.getParent())
                {
                    s=newNode.getParent().getParent().getLeftChild();
                }
                else s=newNode.getParent().getParent().getRightChild();
                if(s!=null&&s.getColor()==INode.RED)
                {
                    s.setColor(INode.BLACK);
                    newNode.getParent().setColor(INode.BLACK);
                    newNode.getParent().getParent().setColor(INode.RED);
                    newNode=newNode.getParent().getParent();
                }
                else if((s == null || s.getColor() == INode.BLACK)&&newNode.getParent().getRightChild()==newNode)
                {
                    if(newNode.getParent().getParent().getRightChild()==newNode.getParent())
                    {
                        newNode=newNode.getParent();
                        rotateLeft(newNode.getParent());
                        newNode.getLeftChild().setColor(INode.RED);
                        newNode.setColor(INode.BLACK);
                    }
                    else {
                        newNode=newNode.getParent();
                        rotateLeft(newNode);
                        //newNode.getLeftChild().setColor(INode.RED);
                        //newNode.setColor(INode.BLACK);
                    }
                }
                else if((s == null || s.getColor() == INode.BLACK)&&newNode==newNode.getParent().getLeftChild())
                {
                    if(newNode.getParent().getParent().getRightChild()==newNode.getParent())
                    {
                        newNode=newNode.getParent();
                        rotateRight(newNode);
                    }
                    else {
                        newNode.getParent().setColor(INode.BLACK);
                        newNode.getParent().getParent().setColor(INode.RED);
                        rotateRight(newNode.getParent().getParent());
                    }
                }
            }
            getRoot().setColor(INode.BLACK);
        }
    }


    boolean getRightOrLeft(INode parent, INode node){
        if(parent.getRightChild() == node) return true;
        return false;
    }

    void changeParent(INode parent, INode child, INode  node){
        if(parent == null){
            root  = child;
            child.setParent(null);
        }
        else if(getRightOrLeft(parent, node)){
            parent.setRightChild(child);
            if(child != null) child.setParent(parent);
        } else {
            parent.setLeftChild(child);
            if(child != null) child.setParent(parent);
        }
    }

    void rotateLeft(INode node) {
        INode child = node.getRightChild();
        INode parent = node.getParent();
        INode rlChild = node.getRightChild().getLeftChild();
        changeParent(parent,child,node);
        child.setLeftChild(node);
        node.setParent(child);
        node.setRightChild(rlChild);
        if(rlChild != null) rlChild.setParent(node);
    }

    void rotateRight(INode node) {
        INode child = node.getLeftChild();
        INode parent = node.getParent();
        INode lrChild = node.getLeftChild().getRightChild();
        changeParent(parent,child,node);
        child.setRightChild(node);
        node.setParent(child);
        node.setLeftChild(lrChild);
        if(lrChild!= null) lrChild.setParent(node);
    }

    INode getNode (INode node, Comparable key) {
        INode right = node.getRightChild();
        INode left = node.getLeftChild();
        if(node.getKey().compareTo(key) == 0) return node;
        if(right != null) if(node.getKey().compareTo(key) < 0 && key.compareTo(right.getKey()) < 0) return getNode(right,key);
        if(left != null) if(node.getKey().compareTo(key) > 0 && key.compareTo(left.getKey()) > 0) return getNode(left,key);
        return null;
    }

    INode getInOrderSuccessor(INode node) {
        if (node.getRightChild() != null) {
            INode successor = minValue(node.getRightChild());
            if (successor != null) successor.getParent().setLeftChild(null);
            return successor;
        }
    }
    INode minValue(INode node) {
        INode current = node;
        while (current.getLeftChild() != null) current = current.getLeftChild();
        return current;
    }

    INode bstDelete(INode root, Comparable key) {
        INode node = getNode(root, key);
        if (node == null) return null;
        if (node.getRightChild() == null && node.getRightChild() == null) {
            //if(node.getParent())
            node = null;
        }
        else if (node.getRightChild() != null && node.getLeftChild() != null) {
            INode inorderSuccessor = getInOrderSuccessor(node);
            changeParent(node.getParent(), inorderSuccessor, node);
            node.setKey(inorderSuccessor.getKey());
            node.setValue(inorderSuccessor.getValue());
        } else {
            INode left = node.getLeftChild();
            INode right = node.getRightChild();
            if (left != null) {
                changeParent(node.getParent(), left, node);
                node = left;
            }
            if (right != null) {
                changeParent(node.getParent(), right, node);
                node = right;
            }
        }
        return node;
    }
    
    @Override
    public boolean delete(Comparable key) {
        INode node = bstDelete(getRoot(),key);
        if(deleteComplete) {
            fix_up_delete();
            return true;
        } else {
            return false;
        }
        return false;
    }
}
