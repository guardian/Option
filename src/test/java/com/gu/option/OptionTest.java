package com.gu.option;

import org.junit.Test;

import static com.gu.option.Option.none;
import static com.gu.option.Option.some;
import static com.gu.option.Option.option;
import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.gu.option.Function;
import com.gu.option.UnitFunction;
import java.util.List;

public class OptionTest {

    @Test
    public void shouldGetSome() { // :)
        assertThat(some(true).get(), is(true));
    }

    @Test
    public void shouldBuildOption() {
        assertThat(option(null).isDefined(), is(false));
        assertThat(option(1).isDefined(), is(true));
    }

    @Test
    public void shouldSupportIterableSyntax() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        for (Integer i : s) {
            assertThat(i, is(1));
        }
        for (Integer i : n) {
            fail();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldErrorGettingNone() {
        none().get();
    }

    @Test
    public void shouldGetOrElse() {
        Option<Boolean> s = some(true);
        Option<Boolean> n = none();

        assertThat(s.getOrElse(false), is(true));
        assertThat(n.getOrElse(false), is(false));
    }

    @Test
    public void shouldOrElse() {
        Option<Boolean> s = some(true);
        Option<Boolean> n = none();

        assertThat(s.orElse(false).get(), is(true));
        assertThat(n.orElse(false).get(), is(false));
    }

    @Test
    public void shouldReportIsDefined() {
        Option<Boolean> s = some(true);
        Option<Boolean> n = none();

        assertThat(s.isDefined(), is(true));
        assertThat(n.isDefined(), is(false));
    }

    @Test
    public void shouldReportIsEmpty() {
        Option<Boolean> s = some(true);
        Option<Boolean> n = none();

        assertThat(s.isEmpty(), is(false));
        assertThat(n.isEmpty(), is(true));
    }

    @Test
    public void shouldReportExists() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        final Function<Integer,Boolean> f = new Function<Integer,Boolean>() {
            public Boolean apply(Integer x) {
                return x < 100;
            }
        };
        Function<Integer,Boolean> notf = new Function<Integer,Boolean>() {
            public Boolean apply(Integer x) {
                return !f.apply(x);
            }
        };

        assertThat(s.exists(f), is(true));
        assertThat(n.exists(f), is(false));
        assertThat(s.exists(notf), is(false));
        assertThat(n.exists(notf), is(false));
    }

    @Test
    public void shouldFilter() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        final Function<Integer,Boolean> f = new Function<Integer,Boolean>() {
            public Boolean apply(Integer x) {
                return x < 100;
            }
        };
        Function<Integer,Boolean> notf = new Function<Integer,Boolean>() {
            public Boolean apply(Integer x) {
                return !f.apply(x);
            }
        };

        assertThat(s.filter(f).isDefined(), is(true));
        assertThat(n.filter(f).isDefined(), is(false));
        assertThat(s.filter(notf).isDefined(), is(false));
        assertThat(n.filter(notf).isDefined(), is(false));
    }

    @Test
    public void shouldMap() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        final Function<Integer,Integer> f = new Function<Integer,Integer>() {
            public Integer apply(Integer x) {
                return x * 2;
            }
        };

        assertThat(s.map(f).isDefined(), is(true));
        assertThat(s.map(f).get(), is(2));
        assertThat(n.map(f).isDefined(), is(false));
    }

    @Test
    public void shouldFlatMap() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        final Function<Integer,Option<Integer>> f = new Function<Integer,Option<Integer>>() {
            public Option<Integer> apply(Integer x) {
                return some(x * 2);
            }
        };
        final Function<Integer,Option<Integer>> f2 = new Function<Integer,Option<Integer>>() {
            public Option<Integer> apply(Integer x) {
                return none();
            }
        };

        assertThat(s.flatMap(f).isDefined(), is(true));
        assertThat(s.flatMap(f).get(), is(2));
        assertThat(n.flatMap(f).isDefined(), is(false));

        assertThat(s.flatMap(f2).isDefined(), is(false));
        assertThat(n.flatMap(f2).isDefined(), is(false));
    }

    @Test
    public void shouldForeach() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        final UnitFunction<Integer> f = new UnitFunction<Integer>() {
            public void apply(Integer x) {
                throw new RuntimeException();
            }
        };

        try {
            s.foreach(f);
        } catch(Exception e) {
            // Correctly applied f
        }

        try {
            n.foreach(f);
        } catch(Exception e) {
            // Incorrectly applied f
            fail();
        }
    }

    @Test
    public void shouldToString() {
        Option<Integer> s = some(1);
        Option<Integer> n = none();

        assertThat(s.toString(), is("Some(1)"));
        assertThat(n.toString(), is("None"));      
    }

    @Test
    public void shouldToList() {
        Option<Integer> s = some(1111);
        Option<Integer> n = none();

        List<Integer> sl = s.toList();
        assertThat(sl.size(), is(1));
        assertThat(sl.get(0), is(1111));


        List<Integer> nl = n.toList();
        assertThat(nl.size(), is(0));
    }

}
