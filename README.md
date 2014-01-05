LargestRectangle
================
Finding Largest Text Container In Random Polygon Using JSwarm

Description
-----------
This project is a little demo for the following use case
+ Draw a *Shop* in the shape of a random polygon
+ Input the title of the *Shop*
+ Select a font family for the title
+ Start the program to get the largest rectangle holding the title within the *Shop Polygon*

UI Usage
--------
(See archive in ./artifacts/LargestRectangle_PSO.jar)
The left panel is Canvas Pannel used to draw *Shop Polygon*.
The right panel is the Function Panel holding some functional buttons and parameter inputtings.

*To Draw A Polygon*:
+ Press "Line" button in Function Panel to start drawing.
+ Click mouse on the Canvas Panel to draw a *Shop Polygon*.
+ Press "Close" button to stop.

*To Run The Program*:
+ Input shop title
+ Select a preferred font family
+ Input some parameters of PSO(not inputting means using default)
+ Press "Start"

Demo
----
![image](https://github.com/dawnwords/LargestRectangle/raw/master/artifacts/Demo.png)

Experiment
----------
(See archive in ./artifacts/LargestRectangle_Experiment.jar)
5 Experiments on different algorithm settings on Concole

+ Experiment on Neighborhood
+ Experiment on Praticle Number
+ Experiment on Iteration Number
+ Experiment on Parameter
+ Experiment on Min Velocity

*To Run The Experiment*
+ Input Experiment code(0-4)
+ Input 4 basic argument(neighborNum particleNum iterationTimes experimentTimes)
+ Press Enter to start

References
---------
+ [Particle Swarm Optimization(PSO)](http://en.wikipedia.org/wiki/Particle_swarm_optimization)
+ [JSwarm](http://jswarm-pso.sourceforge.net/)
