<h1 align="center">🦸‍♂️ Marvel 🦸‍♂️</h1>

Model-View-ViewModel (ie MVVM) is a template of a client application architecture, proposed by John Gossman as an alternative to MVC and MVP patterns when using Data Binding technology. Its concept is to separate data presentation logic from business logic by moving it into particular class for a clear distinction.

(https://user-images.githubusercontent.com/1812129/68319232-446cf900-00be-11ea-92cf-cad817b2af2c.png)


**Why Promoting MVVM VS MVP:**
- ViewModel has Built in LifeCycleOwerness, on the other hand Presenter not, and you have to take this responsiblty in your side.
- ViewModel doesn't have a reference for View, on the other hand Presenter still hold a reference for view, even if you made it as weakreference.
- ViewModel survive configuration changes, while it is your own responsiblities to survive the configuration changes in case of Presenter. (Saving and restoring the UI state)


**Keep your code clean according to MVVM**
-----------------------------
- Yes , liveData is easy , powerful , but you should know how to use.
- For livedate which will emit data stream , it has to be in your
  data layer , and don't inform those observables any thing else like
  in which thread those will consume , cause it is another
- For livedata which will emit UI binding events, it has to be in your ViewModel Layer.
- Observers in UI Consume and react to live data values and bind it.
  responsibility , and according to `Single responsibility principle`
  in `SOLID (object-oriented design)` , so don't break this concept by
  mixing the responsibilities .

![mvvm2](https://user-images.githubusercontent.com/1812129/68319008-e9d39d00-00bd-11ea-9245-ebedd2a2c067.png)