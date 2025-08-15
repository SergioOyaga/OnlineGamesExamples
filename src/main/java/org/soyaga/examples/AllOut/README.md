# AllOut
For this https://www.mathsisfun.com/games/allout.html AllOut problem, we use a GA to solve a grid represented as an ArrayGenome.
In the [GA-representation](#ga-representation) we can see the genome design.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/AllOut/AllOut.gif"  title="AllOut example" alt="AllOut example"/>

## GA Representation:
The genome looks like:
````mermaid
block-beta
    columns 3
    block: Chromosome1:3
    Gen1("&emsp;Gen1 &emsp;") Gen2("&emsp;Gen2 &emsp;") Gen3("&emsp;Gen3 &emsp;")
    end
    block: Chromosome2:3
    Gen4("&emsp;Gen4 &emsp;") Gen5("&emsp;Gen5 &emsp;") Gen6("&emsp;Gen6 &emsp;")
    end
    block: Chromosome3:3
    Gen7("&emsp;Gen7 &emsp;") Gen8("&emsp;Gen8 &emsp;") Gen9("&emsp;Gen9 &emsp;")
    end

    
````
The Genome represents a M*M matrix of booleans. When it is True, it means we have to press that cell to obtain the result.

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [AllOutScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/AllOutScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the allout-web application.
    - Manages the flow of the program.
    - Instantiates the AllOutGA.
    - Introduces the solution in the web app.
2. [AllOutGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/AllOutGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
    - Implements runnable.
3. [AllOutFeasibilityFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/Evaluable/AllOutFeasibilityFunction.java): Class that evaluates whether the solution has been found or not.
4. [AllOutObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/Evaluable/AllOutObjectiveFunction.java): Class that evaluates the number of movements done. We want to find the solution with less possible movements.
5. [AllOutInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/Initializer/AllOutInitializer.java): Initializes an individual.
6. [TargetFeasibilityCriteriaPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/StoppingPolicy/TargetFeasibilityCriteriaPolicy.java): Evaluates if the solution has been found, or we need to keep looking.


## Results
For the GA, the set of parameters used are in the AllOutGA file.

The output looks like:
`````
Loading https://www.mathsisfun.com/games/allout.html: ...
Loaded.
Accepting cookies...
Cookies accepted.
Switching to Iframe...
Iframe switched.
Screenshot the board...
Board screenshot.
Transforming image...
    Loading OpenCV...
    OpenCV loaded.
    Cropping...
    Cropped
    Converting to Grayscale...
    Grayscale converted.
    Converting to Black&White...
    Black&White Converting.
Image transformed.
Computing grid...
Grid computed.
Mapping buttons...
Buttons mapped.
Building GA...
GA built.
Running GA...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 33.0000    | 66.0000    | 33.0000       | 66.0000       | 49.6400     | 8.8809      | 33.00 | 43.00 | 50.00 | 56.00 | 66.00 |                      |                   | 0.0105         |
| 100       | 20.0000    | 61.0000    | 20.0000       | 66.0000       | 32.5152     | 14.4560     | 20.00 | 20.00 | 22.00 | 46.00 | 61.00 | 0.1712               | 712.8261          | 0.2357         |
| 200       | 19.0000    | 62.0000    | 19.0000       | 66.0000       | 32.7879     | 15.2589     | 19.00 | 19.00 | 27.00 | 45.00 | 62.00 | -0.0027              | -61.9158          | 0.1454         |
| 300       | 19.0000    | 59.0000    | 19.0000       | 66.0000       | 32.9394     | 14.1784     | 19.00 | 19.00 | 29.00 | 44.00 | 59.00 | -0.0015              | -60.9042          | 0.0821         |
| 400       | 19.0000    | 66.0000    | 19.0000       | 66.0000       | 33.1515     | 14.6538     | 19.00 | 19.00 | 30.00 | 45.00 | 66.00 | -0.0021              | -69.3725          | 0.1009         |
| 500       | 16.0000    | 64.0000    | 16.0000       | 66.0000       | 28.0606     | 16.3075     | 16.00 | 16.00 | 16.00 | 43.00 | 64.00 | 0.0509               | 2428.6898         | 0.0692         |
| 600       | 16.0000    | 60.0000    | 16.0000       | 66.0000       | 31.8485     | 16.1435     | 16.00 | 16.00 | 26.00 | 48.00 | 60.00 | -0.0379              | -2590.5662        | 0.0483         |
| 700       | 16.0000    | 57.0000    | 16.0000       | 66.0000       | 30.7576     | 16.7911     | 16.00 | 16.00 | 16.00 | 49.00 | 57.00 | 0.0109               | 781.1415          | 0.0461         |
| 800       | 16.0000    | 67.0000    | 16.0000       | 67.0000       | 32.3030     | 16.7540     | 16.00 | 16.00 | 27.00 | 43.00 | 67.00 | -0.0155              | -1095.4361        | 0.0466         |
| 900       | 16.0000    | 69.0000    | 16.0000       | 69.0000       | 30.7879     | 16.4773     | 16.00 | 16.00 | 23.00 | 46.00 | 69.00 | 0.0152               | 1086.2151         | 0.0460         |
| 1000      | 16.0000    | 63.0000    | 16.0000       | 69.0000       | 31.4545     | 16.4576     | 16.00 | 16.00 | 29.00 | 47.00 | 63.00 | -0.0067              | -523.0685         | 0.0421         |
| 1100      | 16.0000    | 59.0000    | 16.0000       | 69.0000       | 29.4848     | 15.8212     | 16.00 | 16.00 | 16.00 | 47.00 | 59.00 | 0.0197               | 2030.2095         | 0.0320         |
| 1200      | 16.0000    | 64.0000    | 16.0000       | 69.0000       | 29.1212     | 16.3814     | 16.00 | 16.00 | 16.00 | 45.00 | 64.00 | 0.0036               | 332.1864          | 0.0361         |
| 1300      | 16.0000    | 64.0000    | 16.0000       | 69.0000       | 30.7273     | 17.7479     | 16.00 | 16.00 | 16.00 | 46.00 | 64.00 | -0.0161              | -1625.3132        | 0.0326         |
| 1400      | 16.0000    | 72.0000    | 16.0000       | 72.0000       | 31.9091     | 17.2000     | 16.00 | 16.00 | 27.00 | 48.00 | 72.00 | -0.0118              | -1180.8091        | 0.0330         |
| 1500      | 16.0000    | 61.0000    | 16.0000       | 72.0000       | 31.9697     | 16.1461     | 16.00 | 16.00 | 30.00 | 48.00 | 61.00 | -0.0006              | -62.0767          | 0.0322         |
| 1600      | 16.0000    | 56.0000    | 16.0000       | 72.0000       | 30.2121     | 15.9958     | 16.00 | 16.00 | 20.00 | 48.00 | 56.00 | 0.0176               | 1778.6664         | 0.0326         |
| 1700      | 16.0000    | 64.0000    | 16.0000       | 72.0000       | 29.1818     | 16.0084     | 16.00 | 16.00 | 16.00 | 43.00 | 64.00 | 0.0103               | 1096.0282         | 0.0310         |
| 1800      | 16.0000    | 60.0000    | 16.0000       | 72.0000       | 29.2121     | 14.6034     | 16.00 | 16.00 | 25.00 | 42.00 | 60.00 | -0.0003              | -28.8714          | 0.0346         |
| 1900      | 16.0000    | 63.0000    | 16.0000       | 72.0000       | 31.1818     | 17.0426     | 16.00 | 16.00 | 26.00 | 49.00 | 63.00 | -0.0197              | -2202.6656        | 0.0295         |
| 2000      | 16.0000    | 64.0000    | 16.0000       | 72.0000       | 32.1212     | 15.4915     | 16.00 | 16.00 | 27.00 | 47.00 | 64.00 | -0.0094              | -988.7537         | 0.0314         |
| 2100      | 16.0000    | 61.0000    | 16.0000       | 72.0000       | 29.3333     | 14.9579     | 16.00 | 16.00 | 26.00 | 45.00 | 61.00 | 0.0279               | 2955.5954         | 0.0311         |
| 2200      | 16.0000    | 62.0000    | 16.0000       | 72.0000       | 30.6667     | 17.2954     | 16.00 | 16.00 | 16.00 | 47.00 | 62.00 | -0.0133              | -1417.9597        | 0.0310         |
| 2300      | 16.0000    | 64.0000    | 16.0000       | 72.0000       | 31.6061     | 18.3517     | 16.00 | 16.00 | 16.00 | 53.00 | 64.00 | -0.0094              | -837.3490         | 0.0370         |
| 2400      | 16.0000    | 66.0000    | 16.0000       | 72.0000       | 28.7879     | 15.1593     | 16.00 | 16.00 | 16.00 | 38.00 | 66.00 | 0.0282               | 3047.8613         | 0.0305         |
| 2500      | 16.0000    | 72.0000    | 16.0000       | 72.0000       | 32.6364     | 18.2953     | 16.00 | 16.00 | 23.00 | 54.00 | 72.00 | -0.0385              | -4452.2661        | 0.0285         |
| 2600      | 16.0000    | 73.0000    | 16.0000       | 73.0000       | 30.8182     | 17.0444     | 16.00 | 16.00 | 23.00 | 49.00 | 73.00 | 0.0182               | 1870.4466         | 0.0321         |
| 2700      | 16.0000    | 59.0000    | 16.0000       | 73.0000       | 29.3939     | 15.6727     | 16.00 | 16.00 | 23.00 | 40.00 | 59.00 | 0.0142               | 1445.3089         | 0.0325         |
| 2800      | 16.0000    | 62.0000    | 16.0000       | 73.0000       | 30.4242     | 15.6535     | 16.00 | 16.00 | 26.00 | 46.00 | 62.00 | -0.0103              | -941.4006         | 0.0361         |
| 2900      | 16.0000    | 59.0000    | 16.0000       | 73.0000       | 31.2424     | 14.3928     | 16.00 | 16.00 | 29.00 | 44.00 | 59.00 | -0.0082              | -830.2532         | 0.0325         |
| 3000      | 16.0000    | 64.0000    | 16.0000       | 73.0000       | 29.3333     | 17.3322     | 16.00 | 16.00 | 16.00 | 47.00 | 64.00 | 0.0191               | 1966.1387         | 0.0320         |
| 3100      | 16.0000    | 59.0000    | 16.0000       | 73.0000       | 33.5758     | 15.8210     | 16.00 | 16.00 | 29.00 | 48.00 | 59.00 | -0.0424              | -4880.7357        | 0.0297         |
| 3200      | 16.0000    | 58.0000    | 16.0000       | 73.0000       | 30.7576     | 16.1471     | 16.00 | 16.00 | 21.00 | 46.00 | 58.00 | 0.0282               | 2731.9675         | 0.0330         |
| 3300      | 16.0000    | 66.0000    | 16.0000       | 73.0000       | 32.0606     | 17.4598     | 16.00 | 16.00 | 21.00 | 50.00 | 66.00 | -0.0130              | -1394.2434        | 0.0308         |
| 3400      | 16.0000    | 61.0000    | 16.0000       | 73.0000       | 29.7273     | 15.4337     | 16.00 | 16.00 | 24.00 | 41.00 | 61.00 | 0.0233               | 2481.4615         | 0.0310         |
| 3500      | 16.0000    | 64.0000    | 16.0000       | 73.0000       | 29.3030     | 16.7069     | 16.00 | 16.00 | 16.00 | 47.00 | 64.00 | 0.0042               | 429.5414          | 0.0326         |
| 3600      | 16.0000    | 70.0000    | 16.0000       | 73.0000       | 30.5152     | 15.4550     | 16.00 | 16.00 | 29.00 | 43.00 | 70.00 | -0.0121              | -1304.5719        | 0.0307         |
| 3700      | 16.0000    | 62.0000    | 16.0000       | 73.0000       | 30.9697     | 15.3869     | 16.00 | 16.00 | 30.00 | 42.00 | 62.00 | -0.0045              | -493.9865         | 0.0304         |
| 3800      | 16.0000    | 62.0000    | 16.0000       | 73.0000       | 30.4242     | 16.9313     | 16.00 | 16.00 | 16.00 | 45.00 | 62.00 | 0.0055               | 592.5380          | 0.0304         |
| 3900      | 16.0000    | 66.0000    | 16.0000       | 73.0000       | 31.9697     | 18.2516     | 16.00 | 16.00 | 16.00 | 52.00 | 66.00 | -0.0155              | -1638.3016        | 0.0311         |
| 4000      | 16.0000    | 56.0000    | 16.0000       | 73.0000       | 30.3333     | 14.7148     | 16.00 | 16.00 | 30.00 | 43.00 | 56.00 | 0.0164               | 1710.9778         | 0.0316         |
| 4100      | 16.0000    | 63.0000    | 16.0000       | 73.0000       | 29.1515     | 16.7822     | 16.00 | 16.00 | 16.00 | 48.00 | 63.00 | 0.0118               | 1276.4036         | 0.0306         |
| 4200      | 16.0000    | 70.0000    | 16.0000       | 73.0000       | 30.3333     | 16.4753     | 16.00 | 16.00 | 20.00 | 45.00 | 70.00 | -0.0118              | -1062.8093        | 0.0367         |
| 4300      | 16.0000    | 53.0000    | 16.0000       | 73.0000       | 28.7879     | 14.6469     | 16.00 | 16.00 | 16.00 | 45.00 | 53.00 | 0.0155               | 1590.8269         | 0.0321         |
| 4400      | 16.0000    | 61.0000    | 16.0000       | 73.0000       | 31.2727     | 15.9170     | 16.00 | 16.00 | 26.00 | 47.00 | 61.00 | -0.0248              | -2369.8600        | 0.0346         |
| 4500      | 16.0000    | 59.0000    | 16.0000       | 73.0000       | 31.0909     | 16.1045     | 16.00 | 16.00 | 26.00 | 47.00 | 59.00 | 0.0018               | 112.2748          | 0.0534         |
| 4600      | 16.0000    | 63.0000    | 16.0000       | 73.0000       | 30.7879     | 16.7146     | 16.00 | 16.00 | 26.00 | 49.00 | 63.00 | 0.0030               | 297.6075          | 0.0336         |
| 4700      | 16.0000    | 65.0000    | 16.0000       | 73.0000       | 32.0909     | 20.1244     | 16.00 | 16.00 | 16.00 | 56.00 | 65.00 | -0.0130              | -1190.7168        | 0.0361         |
| 4800      | 16.0000    | 58.0000    | 16.0000       | 73.0000       | 29.5152     | 16.7533     | 16.00 | 16.00 | 16.00 | 48.00 | 58.00 | 0.0258               | 1896.8599         | 0.0448         |
| 4900      | 16.0000    | 65.0000    | 16.0000       | 73.0000       | 30.8182     | 17.7191     | 16.00 | 16.00 | 16.00 | 51.00 | 65.00 | -0.0130              | -1298.6229        | 0.0331         |
| 5000      | 16.0000    | 61.0000    | 16.0000       | 73.0000       | 30.5758     | 16.9277     | 16.00 | 16.00 | 16.00 | 47.00 | 61.00 | 0.0024               | 234.0968          | 0.0342         |
| 5100      | 16.0000    | 59.0000    | 16.0000       | 73.0000       | 29.7576     | 15.9336     | 16.00 | 16.00 | 16.00 | 46.00 | 59.00 | 0.0082               | 897.3319          | 0.0301         |
| 5200      | 16.0000    | 66.0000    | 16.0000       | 73.0000       | 27.3939     | 14.8159     | 16.00 | 16.00 | 16.00 | 40.00 | 66.00 | 0.0236               | 2395.9159         | 0.0326         |
| 5300      | 16.0000    | 57.0000    | 16.0000       | 73.0000       | 30.7273     | 15.0741     | 16.00 | 16.00 | 29.00 | 46.00 | 57.00 | -0.0333              | -3660.6510        | 0.0300         |
| 5400      | 16.0000    | 59.0000    | 16.0000       | 73.0000       | 30.1212     | 15.3342     | 16.00 | 16.00 | 26.00 | 44.00 | 59.00 | 0.0061               | 641.4512          | 0.0312         |
GA run.
Gathering result...
Result gathered.
Introducing solution...
Solution introduced.
Clicking next...
Next clicked .
`````
As you see in the output example, the GA finds the solution in 0.3 seconds.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the GA parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
