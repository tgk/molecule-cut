(ns runners.create-smiles
	(:use (molecule-cut sdf smiles)))
	
(let [filename (first *command-line-args*)
	molecules (read-sdf-file filename)]
	(dorun (map #(println (smiles-from-molecule %)) molecules)))