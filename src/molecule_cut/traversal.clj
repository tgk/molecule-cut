(ns molecule-cut.traversal)

(def empty-queue clojure.lang.PersistentQueue/EMPTY)
(defn initial-queue [item]
  (conj empty-queue item))
	
(defn bfs-traversal [queue children]
  "Performs a bfs-traversal with an initial queue
  (typically only one item) and a children function.
  The children function returns the children of a 
  node. A nice example is just using a map of vectors
  for the children:
  (bfs-traversal (initial-queue :a) {:a [:b :c] :c [:d]})"
  (if (empty? queue)
    nil
    (lazy-seq
      (cons 
        (peek queue)
        (bfs-traversal
          (into 
            (pop queue) 
            (children (peek queue)))
          children)))))

(defn create-seen? []
  "Creates a function that answers true the first time
  it sees a new item, and false every time after that."
  (let [seen-items (ref #{})]
    (fn [item]
      (if (contains? @seen-items item)
        true
        (do 
          (dosync (alter seen-items conj item))
          false)))))
					
(defn visit-children-once-wrapper
  "Wrapper for a children function which gurantees
  that a child is only reported once. Can for example
  be used for a bfs traversal of a graph with loops to
  avoid infinit recursion. Not needed for trees."
  ([children initial-node] 
    (visit-children-once-wrapper 
      children initial-node identity))
  ([children initial-node key-fn]
    (let [node-seen? (create-seen?)]
      (node-seen? initial-node)
      (fn [node]
        (filter 
          #(not (node-seen? (key-fn %)))
          (children node))))))
