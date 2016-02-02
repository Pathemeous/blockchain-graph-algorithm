package nl.tudelft.ti2306.blockchain.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic binary graph datastructure. Each node can have at most two parents
 * and two children.
 * @author Maarten
 *
 * @param <T> Type of the data of the nodes.
 */
public class BinaryGraph<T> {

    protected Node<T> root;

    /**
     * Constructs a new BinaryGraph with a root.
     * @param rootData the data to be stored in the root.
     */
    public BinaryGraph(T rootData) {
        root = new Node<>(rootData);
    }

    /**
     * @return the root
     */
    public Node<T> getRoot() {
        return root;
    }


    /**
     * Represents a Node in the Graph.
     * @author Maarten
     *
     * @param <T> Type of the data of the nodes.
     */
    public static class Node<T> {

        protected T data;
        protected Node<T> parentLeft;
        protected Node<T> parentRight;
        protected Node<T> childLeft;
        protected Node<T> childRight;

        /**
         * Construct a new node.
         * @param data the data in this node.
         */
        protected Node(T data) {
            this.data = data;
        }

        /**
         * Adds a parent to this node, if possible.
         * @param parent The parent to add to this node.
         * @return true if the parent was added, false if there can be no more
         * parents.
         */
        public boolean addParent(T childData) {
            return addParent(new Node<T>(childData));
        }

        /**
         * Adds a child to this node, if possible.
         * @param child The child to add to this node.
         * @return true if the child was added, false if there can be no more
         * children.
         */
        public boolean addChild(T childData) {
            return addChild(new Node<T>(childData));
        }

        /**
         * Adds a parent to this node, if possible.
         * @param parent The parent to add to this node.
         * @return true if the parent was added, false else.
         */
        public boolean addParent(Node<T> parent) {
            if (this.addParentIntern(parent)) {
                if (parent.addChildIntern(this)) {
                    return true;
                } else {
                    parent.removeParentIntern(this);
                }
            }
            return false;
        }

        /**
         * Adds a child to this node, if possible.
         * @param child The child to add to this node.
         * @return true if the child was added, false else.
         */
        public boolean addChild(Node<T> child) {
            if (child.addParentIntern(this)) {
                if (this.addChildIntern(child)) {
                    return true;
                } else {
                    child.removeParentIntern(this);
                }
            }
            return false;
        }

        protected boolean removeParentIntern(Node<T> node) {
            if (parentLeft.equals(node)) {
                parentLeft = null;
            } else if (parentRight.equals(node)) {
                parentRight = null;
            } else return false;
            return true;
        }

        protected boolean removeChildIntern(Node<T> node) {
            if (childLeft.equals(node)) {
                childLeft = null;
            } else if (childRight.equals(node)) {
                childRight = null;
            } else return false;
            return true;
        }

        /**
         * Adds a child to this node, if possible.
         * @param child the child to add to this node.
         * @return true if the child was added, false else.
         */
        protected boolean addChildIntern(Node<T> child) {
            if (childLeft == null) {
                childLeft = child;
            }
            else if (childRight == null) {
                childRight = child;
            }
            else return false;
            return true;
        }

        /**
         * Adds a parent to this node, if possible.
         * @param parent the parent to add to this node.
         * @return true if the parent was added, false else.
         */
        protected boolean addParentIntern(Node<T> parent) {
            if (parentLeft == null) {
                parentLeft = parent;
            }
            else if (parentRight == null) {
                parentRight = parent;
            }
            else return false;
            return true;
        }

        /**
         * @return the data
         */
        public T getData() {
            return data;
        }

        /**
         * @return the parents
         */
        public List<Node<T>> getParents() {
            ArrayList<Node<T>> res = new ArrayList<>(2);
            if (parentLeft != null)
                res.add(parentLeft);
            if (parentRight != null)
                res.add(parentRight);
            return res;
        }

        /**
         * @return the children
         */
        public List<Node<T>> getChildren() {
            ArrayList<Node<T>> res = new ArrayList<>(2);
            if (childLeft != null)
                res.add(childLeft);
            if (childRight != null)
                res.add(childRight);
            return res;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((data == null) ? 0 : data.hashCode());
            return result;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node<T> other;
            try {
                other = (Node<T>) obj;
            } catch (ClassCastException e) {
                return false;
            }
            if (data == null) {
                if (other.data != null)
                    return false;
            } else if (!data.equals(other.data))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Node[" + data.toString() + "]";
        }

    }

}
