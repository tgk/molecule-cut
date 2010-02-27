(ns molecule-cut.smiles
	(:import 
		(org.openscience.cdk DefaultChemObjectBuilder)
		(org.openscience.cdk.smiles SmilesParser SmilesGenerator))
	(:use [clojure.contrib.io :only (read-lines)]))
	
(def smiles-parser (new SmilesParser (DefaultChemObjectBuilder/getInstance)))	
(defn molecule-from-smiles [smiles-string] 
	(.parseSmiles smiles-parser smiles-string))

(def smiles-generator (new SmilesGenerator))
(defn smiles-from-molecule [molecule] 
		(.createSMILES smiles-generator molecule))

(defn parse-line [line]
	(let [[smiles name] (.split line "\t")
				molecule (molecule-from-smiles smiles)]
		{:name name :molecule molecule}))

(defn parse-file [filename]
	(map parse-line (read-lines filename)))
