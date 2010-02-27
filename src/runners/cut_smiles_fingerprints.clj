(ns runners.cut-smiles-fingerprints
  (:use 
    (molecule-cut 
      sdf
      smiles
      molecule-utils 
      cut-traversal 
      fingerprints)))
			
(defn print-usage []
  (println "
Program for generating fingerprints for a file of molecules using 
the cut distance. Only uses smiles strings for memorazation. 
Therefore, a max level can not be set.

Supplied arguments should be
  - A sdf-file.
"))
		
;; TODO: Remember that all fingerprints should be generated in one
;; go. The files must be concatinated first.
;; Also: we cannot use doseq as it will scrample it up, least we 
;; extract the proper fingerprints afterwards using their names.
(if (= 1 (count *command-line-args*))
  (let [filename (first *command-line-args*)
        molecules (read-sdf-file filename)
        names (map molecule-name molecules)
        smiles-strings (map smiles-from-molecule molecules)
        smiles-cuts (map bfs-smiles-cuts smiles-strings)
        molecule-fingerprints (fingerprints smiles-cuts)]
    (doseq [[name fingerprint] (zipmap names molecule-fingerprints)]
      (println name (apply str (interpose \, (sort fingerprint))))))
  (print-usage))