package com.example.SimpleSecurity.SimpleSecurity.Configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;

import java.util.Objects;

//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleSecuritySchemaFilter implements SchemaFilter {

    public static final SimpleSecuritySchemaFilter INSTANCE = new SimpleSecuritySchemaFilter();

    @Override
    public boolean includeNamespace(Namespace namespace) {
        return true;
    }

    @Override
    public boolean includeTable(Table table) {
        return LoginConfiguration.REQUIRED_ACTIVATION ||
                !Objects.equals(table.getName(), LoginConfiguration.ACTIVATION_LINK_TABLE_NAME);
    }

    @Override
    public boolean includeSequence(Sequence sequence) {
        return true;
    }
}
