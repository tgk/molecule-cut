(ns runners.ranker
  (:use (molecule-cut smiles distance)))
	
(defn print-usage []
  (println "
Program for ranking a file of molecules
according to the cut distance.

Supplied arguments should be
  - The SMILES string for the query
  - A file where each line is a molecule id 
    and a SMILES string
"))

(defn rank-molecules [query molecules]
  (let [distance (cut-distance-fn query)]
    (apply sorted-set-by 
      #(compare (:distance %1) (:distance %2))  ; must exist idiomatic form 
      (map 
        #(array-map 
          :name (:name %) 
          :distance (distance (:molecule %))) 
      molecules))))

(if (= 2 (count *command-line-args*))
  (let [query (molecule-from-smiles (first *command-line-args*))
        filename (second *command-line-args*)
        molecules (parse-file filename)]
    (dorun (map 
      #(println (:name %) (:distance %)) 
    (rank-molecules query molecules))))
  (print-usage))