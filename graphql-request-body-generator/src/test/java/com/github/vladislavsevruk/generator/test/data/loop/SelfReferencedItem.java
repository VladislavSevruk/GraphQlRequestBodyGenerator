package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class SelfReferencedItem {

    @GqlField
    private Long id;
    @GqlField(withSelectionSet = true)
    private SelfReferencedItem item;
}
