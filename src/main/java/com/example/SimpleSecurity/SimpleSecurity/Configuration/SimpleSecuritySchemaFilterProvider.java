package com.example.SimpleSecurity.SimpleSecurity.Configuration;

import org.hibernate.tool.schema.spi.SchemaFilter;
import org.hibernate.tool.schema.spi.SchemaFilterProvider;


public class SimpleSecuritySchemaFilterProvider implements SchemaFilterProvider {
    @Override
    public SchemaFilter getCreateFilter() {
        return SimpleSecuritySchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getDropFilter() {
        return SimpleSecuritySchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getMigrateFilter() {
        return SimpleSecuritySchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getValidateFilter() {
        return SimpleSecuritySchemaFilter.INSTANCE;
    }
}
