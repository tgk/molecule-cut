(ns molecule-cut.cut-traversal
  (:use 
    (molecule-cut smiles basic-cuts traversal)
    clojure.contrib.def))

(defn molecule-item [level molecule]
  {	:level level 
    :molecule molecule
    :smiles (smiles-from-molecule molecule)})
	
(defn all-basic-cuts [{level :level molecule :molecule}]
  (map 
    (partial molecule-item (inc level))
    (map 
      (partial cut-bond molecule) 
      (find-cuttable-bonds molecule))))
		
(defn bfs-molecule-cuts [molecule]
  (let [root (molecule-item 0 molecule)]
    (bfs-traversal
      (initial-queue root)
      (visit-children-once-wrapper 
        all-basic-cuts 
        root
        :smiles))))

;; Method that does not retain the molecule, but which uses
;; memorization.
(defn-memo all-basic-smiles-cuts [smiles]
  (if (= "" smiles)
    []
    (let [molecule (molecule-from-smiles smiles)
          bonds (find-cuttable-bonds molecule)
          cuts (map (partial cut-bond molecule) bonds)]
      (map smiles-from-molecule cuts))))

(defn-memo bfs-smiles-cuts [smiles]
  "A memorized version that only returns the smiles
  strings. Cannot use levels as that would not make sense
  when only the SMILES strings are remembered."
  (let [molecule (molecule-from-smiles smiles)
        canonical-smiles (smiles-from-molecule molecule)]
    (bfs-traversal
      (initial-queue canonical-smiles)
      (visit-children-once-wrapper
        all-basic-smiles-cuts
        canonical-smiles))))