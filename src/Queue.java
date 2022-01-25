/**Generic queue with general queue logic
 * @author Seth Beckett
 */
public class Queue<E> {

    private Node<E> head;
    private Node<E> tail;
    public int currentSize = 0;
    public int totalRemoved = 0;
    public int totalAdded = 0;

    /*simple node class
     */
    private static class Node<E>{
        E nodeValue;
        Node<E> next; //reference to next node

        /**Node constructor given an item
         * @param value: value/object to be stored in node
         */
        Node(E value) {
            this.next = null;
            this.nodeValue = value;
        }

    }

    /** adds an element to queue tail
     * @param element : element to be added to queue
     */
    public void enqueue(E element) {
        Node<E> newNode = new Node<>(element);
        if (this.isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.next = newNode;
            this.tail = newNode;
        }
        this.currentSize++;
        this.totalAdded++;
    }

    /**    removes head item and moves head to the next item
     * @return temp: returns the node which is dequeued
     */
    public E dequeue() {
        if (this.isEmpty()) {
            this.tail = null;
            return null;
        }

        else {
            Node<E> popped = this.head;
            this.head = this.head.next;
            this.totalRemoved++;
            this.currentSize--;
            if (this.head == null) this.tail = null;
            return popped.nodeValue;
        }
    }

    /*prints a string stating the queue data fields
     */
    public void printQueueInfo() {
        System.out.printf("Queue Info: \nTotal Added = %d \nTotal Removed = %d \nCurrent Size = %d \n",
                this.totalAdded, this.totalRemoved, this.currentSize);
    }

    public void displayQueue() {
        Node<E> dummyNode = this.head;
        while (dummyNode != null) {
            System.out.println(dummyNode.nodeValue);
            dummyNode = dummyNode.next;
        }
    }

    public boolean isEmpty() {
        return (this.head == null && this.tail == null);
    }

    // Test to make sure that the queue works
//    public static void main(String[] args) {
//        Queue<String> que = new Queue<>();
//        for (int i = 0; i < 10; i++) {
//            String msg = "Hello %d".formatted(i);
////        System.out.println(msg);
//            que.enqueue(msg);
////        que.displayQueue();
////        que.printQueueInfo();
//        }
////
//        for (int j = 0; j < 10; j++) {
//            String s = que.dequeue();
////        System.out.println(s);
////        que.displayQueue();
////        que.printQueueInfo();
//            System.out.println(s);    }
//
//    }
}
