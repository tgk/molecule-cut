(ns tests.test-cut-traversal
	(:use 
		(molecule-cut cut-traversal smiles)
		clojure.contrib.test-is))
		
(deftest test-cut-traversal-on-chain 
	(is (=
		["CCCC" "CCC" "CC" ""] 
		(map :smiles (bfs-molecule-cuts (molecule-from-smiles "CCCC")))))
	(is (=
		[0 1 2 3] 
		(map :level (bfs-molecule-cuts (molecule-from-smiles "CCCC")))))		
	)
	
(deftest test-cut-traversal-on-cycle
	(is (=
		["CC1CCC1" "CCCCC" "CCC(C)C" "C1CCC1" "CCCC" "CC(C)C" "CCC" "CC" ""] 
		(map :smiles (bfs-molecule-cuts (molecule-from-smiles "C1CCC1C")))))
	(is (=
		[0 1 1 1 2 2 3 4 5]
		(map :level (bfs-molecule-cuts (molecule-from-smiles "C1CCC1C")))))		
	)

(deftest test-cut-smiles-traversal-on-chain 
	(is (=
		["CCCC" "CCC" "CC" ""] 
		(bfs-smiles-cuts "CCCC"))))
		
(deftest test-cut-smiles-traversal-on-cycle
	(is (=
		["CC1CCC1" "CCCCC" "CCC(C)C" "C1CCC1" "CCCC" "CC(C)C" "CCC" "CC" ""] 
		(bfs-smiles-cuts "C1CCC1C"))))
