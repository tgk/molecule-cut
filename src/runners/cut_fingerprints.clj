(ns runners.cut-fingerprints
	(:use 
		(molecule-cut 
			sdf
			molecule-utils 
		 	cut-traversal 
		 	fingerprints)))
			
(defn print-usage []
	(println "
Program for generating fingerprints for a file of molecules using 
the cut distance.

Supplied arguments should be
  - A sdf-file.
  - Maximum number of cuts to perform
		"))
		
;; TODO: Remember that all fingerprints should be generated in one
;; go. The files must be concatinated first.
;; Also: we cannot use doseq as it will scrample it up, least we 
;; extract the proper fingerprints afterwards using their names.
(if (= 2 (count *command-line-args*))
	(let [
		filename (first *command-line-args*)
		max-level (Integer/parseInt (second *command-line-args*))
		molecules (read-sdf-file filename)
		names (map molecule-name molecules)
		molecules-cuts 
			(map bfs-molecule-cuts molecules)
		molecules-cuts-until-level 
			(map (partial take-while #(<= (:level %) max-level)) molecules-cuts)
		molecules-cuts-smiles 
			(map #(map :smiles %) molecules-cuts-until-level)
		molecule-fingerprints 
			(fingerprints molecules-cuts-smiles)]
		(doseq [[name fingerprint] (zipmap names molecule-fingerprints)]
			(println name (apply str (interpose \, (sort fingerprint))))))
	(print-usage))