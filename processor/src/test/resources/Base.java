package test;

import java.util.List;
import java.util.Map;

abstract class Base<T1, T2> {
  abstract T1 _a();
  abstract List<T2> _b();
  abstract Map<T2, List<T1>> _c();
}
