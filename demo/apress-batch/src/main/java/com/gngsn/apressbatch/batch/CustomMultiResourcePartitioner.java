package com.gngsn.apressbatch.batch;

import lombok.Data;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


@Data @Component
public class CustomMultiResourcePartitioner implements Partitioner {

    private static final String DEFAULT_KEY_NAME = "fileName";

    private static final String PARTITION_KEY = "partition";

    private Resource[] resources = new Resource[0];

    private String keyName = DEFAULT_KEY_NAME;


    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        int i = 0, k = 1;

        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: " + resource);

            context.putString(keyName, resource.getFilename());
            context.putString("opFileName", "output"+k+++".xml");
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        return map;
    }
}