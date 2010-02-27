(ns molecule-cut.sdf
	(:import 
		(java.io File FileInputStream)
		org.openscience.cdk.DefaultChemObjectBuilder
		org.openscience.cdk.io.iterator.IteratingMDLReader))
		
(defn read-sdf-file [filename]
	(let [file (File. filename)
				reader (IteratingMDLReader. 
									(FileInputStream. file) 
									(DefaultChemObjectBuilder/getInstance))]
		(iterator-seq reader)))
	
