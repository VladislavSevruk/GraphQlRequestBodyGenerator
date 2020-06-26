package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class LongLoopedItem2 {

    @GqlField
    private long field2;
    @GqlField(withSelectionSet = true)
    private LongLoopedItem3 longLoopedItem3;
}
