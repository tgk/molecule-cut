(ns molecule-cut.distance
  (:use molecule-cut.cut-traversal))
	
(defn smiles-map [molecule]
  (let [cuts (bfs-molecule-cuts molecule)]
    (zipmap (map :smiles cuts) (map :level cuts))))
	
;; TODO: (first (filter pred coll)) MUST have a shorter form
;; either in clojure or in clojure.contrib
(defn cut-distance-fn [target-molecule]
  (let [target-map (smiles-map target-molecule)]
    (fn [query-molecule]
      (let [first-common 
              (first (filter 
                #(target-map (:smiles %))
                (bfs-molecule-cuts query-molecule)))]
        (+ 
          (:level first-common) 
          (target-map (:smiles first-common)))))))