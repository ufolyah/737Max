# For anyone that implements GUI and Test: 

### About Trips Class

The code of class "Trips" may be hard to understand so I write comments here.

Trips can handle multiple filters and one sort method. And the filter and sorting are implemented with functors.

One filter is defined by one name and a function (Trip) -> bool, and only the trips returned true from the function will be selected. When multiple filters are applied, each filter can be delete independently by its name.

One sort method is defined by a standard Comparator<Trip>. For easier use, you can sort the trips by key with something like Comparator.comparing(Trip::getTravelTime)

The Trips is lazy evaluated, which means the list will get filtering and sorting only when you call getTrips().

And if you call getTrips() multiple times without change the filters and sorting, the method will do the filter and sort only in the first call and save the result for latter calls.  

# 737Max Timeline

### 03/06/2020 D. Xu
All of the source code should be put under B737Max/src.  
All of the pre-built library in jar should be put under lib/

package App for GUI (currently using JavaFX, we can change it if any of you prefers another one.).  
package Driver for CLI entry point (to make a custom test or prototype demo).  
package Test for future tests using JUnit (not included now).  
package Components for all kinds of internal functionality.

I updated object model for fixing CamelCase.  
I have create the class files in Components based on our object model.  
I implemented Flight class for your reference.  
The QueryFactory is copied from the sample, remaining incomplete.

To avoid conflicts and repeat work, before you start working on a file, tell us in this Readme.  
And tell us again once you finished. 

Do not delete anything in this timeline, just append it.

Also, if there is any mistake in the object model, like CamelCase, email me or update it yourself and inform others here.

PS: use code generator and "fix doc comment" in intellij will save your life.

---

### 03/12/2020 D. Xu
UML: Fix a bug for input parameter of ServerInterface.reserve  
Code: general http Get and Post function set up in ServerInterface.

Issue: I cannot reserve flight & reset Database. Their response code is weird. 

---

### 03/14/2020 D.Xu
UML: Mark the property of Airplane read-only to make the methods consistent with Airports.  

Code:
  1. QueryFactory, Airport, Airports, Airplane, Airplanes are completed.  
  2. GetAirport part of XML,ServerInterface are completed and tested with Test2.  
  
      
     

