# quarkus-cxf-context-propagation-bug

Reproducer for showing that async Quarkus CXF calls do not correctly propagate context.

Note that the soap endpoint just returns null. I couldn't immediately figure out how to give a proper response, but that's not really the point of this reproducer anyway.