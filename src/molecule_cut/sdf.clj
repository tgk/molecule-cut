(ns molecule-cut.sdf
  (:import 
    java.io File.FileInputStream
    org.openscience.cdk.DefaultChemObjectBuilder
    org.openscience.cdk.io.iterator.IteratingMDLReader))
		
(defn read-sdf-file [filename]
  (let [file (File. filename)
        stream (FileInputStream. file)
        reader 
          (IteratingMDLReader. stream (DefaultChemObjectBuilder/getInstance))]
    (iterator-seq reader)))
	
