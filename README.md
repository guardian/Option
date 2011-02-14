Java Option Class
=================
Implementation of Scala Option in Java.

Usage
=====

Including in Maven
------------------
Available from the Guardian github repository:

    <dependency>
        <groupId>com.gu</groupId>
        <artifactId>option</artifactId>
        <version>1.2</version>
    </dependency>

And check for the latest version number.

Creating options
----------------

    import static com.gu.option.Option.none;
    import static com.gu.option.Option.some;
    import static com.gu.option.Option.option;
    import com.gu.option.Option;
    
    ...

    Option<Integer> s1 = some(6174);
    Option<Integer> n1 = none();
    Option<Integer> s2 = option(6174);   // is Some(6174)
    Option<Integer> n2 = option(null);   // is None

You may have a headache with ensuring correct typing if you use `none()` in expressions.


Accessing options
-----------------
Iterable for-syntax is available:

    Option<Integer> s = some(6174);
    Option<Integer> n = none();

    for (Integer i : s) {
	// Do something with i = 6174
    }

    for (Integer i : n) {
        // Not executed
    }

As are `get` and `getOrElse`:
    Option<Integer> s = some(6174);
    Option<Integer> n = none();

    System.out.println(s.get());            // prints 6174
    System.out.println(s.getOrElse(5740));  // prints 6174
    System.out.println(n.getOrElse(5740));  // prints 5740


Operating with functions and Options
------------------------------------
To map, flatMap, foreach, etc on `Option`s, function interfaces are provided:

    import com.gu.option.Option.Function;
    import com.gu.option.Option.UnitFunction;

    Option<Integer> s = some(1);
    Option<Integer> n = none();

    final Function<Integer,Integer> f = new Function<Integer,Integer>() {
       public Integer apply(Integer x) { return x * 2; }
    };

    System.out.println(s.map(f));        // prints Some(2)
    System.out.println(n.map(f));        // prints None

    final Function<Integer,Option<Integer>> f2 = new Function<Integer,Option<Integer>>() {
       public Option<Integer> apply(Integer x) { return some(x * 2); }
    };

    System.out.println(s.flatMap(f2));   // prints Some(2)
    System.out.println(n.flatMap(f2));   // prints None

    final UnitFunction<Integer> f3 = new UnitFunction<Integer>() {
       public void apply(Integer x) { System.out.println(x * 2); }
    };

    s.foreach(f3);                      // prints 2
    n.foreach(f3);                      // does nothing


Filtering and testing
---------------------
The methods `isDefined`, `isEmpty`, `filter`, `exists` are implemented as their Scala counterparts.








