(ns molecule-cut.basic-cuts
	(:import 
		org.openscience.cdk.Molecule
		org.openscience.cdk.graph.SpanningTree
		org.openscience.cdk.ringsearch.AllRingsFinder))

;; Algorithm for finding cuttable bonds
(defn bond-is-outermost? [molecule bond] 
	"Examines a bond in a molecule context, to see if it is 
	an outermost bond. Returns true if this is the case, nil
	 otherwise."
	(some #(= 1 (.getConnectedAtomsCount molecule %)) (.atoms bond)))

(defn find-outermost-bonds [molecule]
	(filter (partial bond-is-outermost? molecule) (.bonds molecule)))
	
;; Might factor the bond checker out, for unit test purposes.
(def rings-finder (new AllRingsFinder))
(defn ringset-find-cycle-bonds [molecule] 
	"Function for extracting bonds from a molecule.
	There is no nice way of getting bonds out from a ring set,
	so we have to filter the bonds based on their atom container
	count."
	(let [ring-set (.findAllRings rings-finder molecule)] 
		(filter 
			#(not (zero? (.getAtomContainerCount (.getRings ring-set %)))) 
			(.bonds molecule))))
			
(defn spanningtree-find-cycle-bonds [molecule]
	"Function for extracting bonds from a molecule."
	(iterator-seq
		(.. 
			(SpanningTree. molecule) 
			(getCyclicFragmentsContainer) (bonds) (iterator))))
					
(def find-cycle-bonds spanningtree-find-cycle-bonds)

(defn find-cuttable-bonds [molecule]
	"Finds all cuttable bonds from a molecule."
	(into 
		(find-outermost-bonds molecule) 
		(find-cycle-bonds molecule)))

;; Algorithm for cutting bonds
(defn atom-unconnected? [molecule atom]
	"Examines if an atom is unconnected in the given molecule."
	(= 0 (.getConnectedAtomsCount molecule atom)))

(defn cut-bond [molecule bond]
	"Returns a copy of the molecule without the bond.
	Does not check if it is a double bond that should just be reduced."
	(let [copy (doto (Molecule. molecule) (.removeBond bond))
				unconnected-atoms (filter (partial atom-unconnected? copy) (.atoms bond))]
		(dorun (map #(.removeAtom copy %) unconnected-atoms))
		copy))