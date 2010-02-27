(ns runners.extract-names
	(:use (molecule-cut sdf molecule-utils)))
	
(let [filename (first *command-line-args*)
			molecules (read-sdf-file filename)
			names (map molecule-name molecules)]
	(dorun (map println names)))
	
