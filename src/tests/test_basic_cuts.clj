(ns tests.test-basic-cuts
  (:use 
    (molecule-cut smiles basic-cuts)
    clojure.set
    clojure.contrib.test-is)
  (:import 
    (org.openscience.cdk Atom Bond Molecule DefaultChemObjectBuilder)
    (org.openscience.cdk.smiles SmilesParser)))

;; Convenience functions for creating test molecules
(defn new-molecule [] (new Molecule))

(defn new-atom [molecule element] 
  (let [atom (Atom. element)]
    (do
      (.addAtom molecule atom)
      atom)))

(defn connect-atoms [molecule atom1 atom2]
  (let [bond (Bond. atom1 atom2)]
    (do
      (.addBond molecule bond)
      bond)))

;; Molecules used for testing
(def star-molecule (new-molecule))
(def c (new-atom star-molecule "C"))
(def h1 (new-atom star-molecule "H"))
(def h2 (new-atom star-molecule "H"))
(def h3 (new-atom star-molecule "H"))
(def h4 (new-atom star-molecule "H"))
(def c-h1 (connect-atoms star-molecule c h1))
(def c-h2 (connect-atoms star-molecule c h2))
(def c-h3 (connect-atoms star-molecule c h3))
(def c-h4 (connect-atoms star-molecule c h4))

(def chain-molecule (new-molecule))
(def c1 (new-atom chain-molecule "C"))
(def c2 (new-atom chain-molecule "C"))
(def c3 (new-atom chain-molecule "C"))
(def c4 (new-atom chain-molecule "C"))
(def c1-c2 (connect-atoms chain-molecule c1 c2))
(def c2-c3 (connect-atoms chain-molecule c2 c3))
(def c3-c4 (connect-atoms chain-molecule c3 c4))

(def cycle-molecule (new-molecule))
(def x1 (new-atom cycle-molecule "C"))
(def x2 (new-atom cycle-molecule "C"))
(def x3 (new-atom cycle-molecule "C"))
(def x4 (new-atom cycle-molecule "C"))
(def x1-x2 (connect-atoms cycle-molecule x1 x2))
(def x2-x3 (connect-atoms cycle-molecule x2 x3))
(def x3-x1 (connect-atoms cycle-molecule x3 x1))
(def x3-x4 (connect-atoms cycle-molecule x3 x4))

;; Tests
(deftest test-bond-is-outermost? 
  (is (bond-is-outermost? star-molecule c-h1))
  (is (bond-is-outermost? star-molecule c-h2))
  (is (bond-is-outermost? star-molecule c-h3))
  (is (bond-is-outermost? star-molecule c-h4))

  (is (bond-is-outermost? chain-molecule c1-c2))
  (is (not (bond-is-outermost? chain-molecule c2-c3)))
  (is (bond-is-outermost? chain-molecule c3-c4))

  (is (not (bond-is-outermost? cycle-molecule x1-x2)))
  (is (not (bond-is-outermost? cycle-molecule x2-x3)))
  (is (not (bond-is-outermost? cycle-molecule x3-x1)))
  (is (bond-is-outermost? cycle-molecule x3-x4)))
  
(deftest test-find-outermost-bonds
  (is (= [c-h1 c-h2 c-h3 c-h4] (find-outermost-bonds star-molecule)))
  (is (= [c1-c2 c3-c4] (find-outermost-bonds chain-molecule)))
  (is (= [x3-x4] (find-outermost-bonds cycle-molecule))))

(deftest test-find-cycle-bonds
  (is (= nil (find-cycle-bonds star-molecule)))
  (is (= nil (find-cycle-bonds chain-molecule)))
  (is (= #{x1-x2 x2-x3 x3-x1} (set (find-cycle-bonds cycle-molecule)))))

(def cycle-molecule-without-appendix (cut-bond cycle-molecule x3-x4))
(deftest test-cut-bond
  (is (= "CC1CC1" (smiles-from-molecule cycle-molecule)))
  (is (= "C1CC1" (smiles-from-molecule cycle-molecule-without-appendix))))

(deftest test-find-cuttable-bonds 
  (is (= [c-h1 c-h2 c-h3 c-h4] (find-cuttable-bonds star-molecule)))
  (is (= [c1-c2 c3-c4] (find-cuttable-bonds chain-molecule)))
  (is (= #{x3-x4 x1-x2 x2-x3 x3-x1} (set (find-cuttable-bonds cycle-molecule))))
  (is 
    (= #{x3-x4 x1-x2 x2-x3 x3-x1} 
    (set (find-cuttable-bonds cycle-molecule))))
  (is 
    (= #{x1-x2 x2-x3 x3-x1} 
    (set (find-cuttable-bonds cycle-molecule-without-appendix)))))

(deftest test-atom-unconnected?
  (is (not (atom-unconnected? cycle-molecule x4))))