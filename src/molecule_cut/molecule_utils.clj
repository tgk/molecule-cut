(ns molecule-cut.molecule-utils)

(defn molecule-name [molecule]
	(.get (.getProperties molecule) "cdk:Title"))

