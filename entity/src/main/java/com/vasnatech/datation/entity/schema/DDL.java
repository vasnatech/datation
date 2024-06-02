package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public interface DDL extends Node {

    @Override
    default String name() {
        return "ddl";
    }

    record Schema(String schema) implements DDL {
        public static Builder builder() {
                return new Builder();
            }

        public static class Builder {
                String schema;

        public Builder schema(String schema) {
                this.schema = schema;
                return this;
            }

        public Schema build() {
                return new Schema(schema);
            }
        }
    }

    record Table(String table) implements DDL {
        public static Builder builder() {
                return new Builder();
            }

        public static class Builder {
                String table;

        public Builder table(String table) {
                this.table = table;
                return this;
            }

        public Table build() {
                return new Table(table);
            }
        }
    }

    interface Column extends DDL {
        default boolean isSimple() {
            return false;
        }
        default boolean isRelational() {
            return false;
        }

        static Builder builder() {
            return new Builder();
        }

        class Builder {
            String column;
            RelationType type;
            RelationColumn.TableRelation.Builder tableBuilder;
            RelationColumn.TableRelation.Builder inverseTableBuilder;

            public Builder column(String column) {
                this.column = column;
                return this;
            }

            public Builder type(RelationType type) {
                this.type = type;
                return this;
            }

            RelationColumn.TableRelation.Builder tableBuilder() {
                if (tableBuilder == null) {
                    tableBuilder = RelationColumn.TableRelation.builder();
                }
                return tableBuilder;
            }

            public Builder table(String table) {
                tableBuilder().name(table);
                return this;
            }

            public Builder columns(LinkedHashMap<String, String> columns) {
                tableBuilder().columns(columns);
                return this;
            }

            public Builder column(String from, String to) {
                tableBuilder().column(from, to);
                return this;
            }

            RelationColumn.TableRelation.Builder inverseTableBuilder() {
                if (inverseTableBuilder == null) {
                    inverseTableBuilder = RelationColumn.TableRelation.builder();
                }
                return inverseTableBuilder;
            }

            public Builder inverseTable(String table) {
                inverseTableBuilder().name(table);
                return this;
            }

            public Builder inverseColumns(LinkedHashMap<String, String> columns) {
                inverseTableBuilder().columns(columns);
                return this;
            }

            public Builder inverseColumn(String from, String to) {
                inverseTableBuilder().column(from, to);
                return this;
            }
            public Column build() {
                return type == null
                        ? new SimpleColumn(column)
                        : new RelationColumn(
                                type,
                                tableBuilder == null ? null : tableBuilder.build(),
                                inverseTableBuilder == null ? null : inverseTableBuilder.build()
                        );
            }
        }
    }

    record SimpleColumn(String column) implements Column {

        public boolean isSimple() {
            return true;
        }
    }

    record RelationColumn(
            RelationType type,
            DDL.RelationColumn.TableRelation table,
            DDL.RelationColumn.TableRelation inverseTable
    ) implements Column {

        public boolean isRelational() {
                return true;
            }

        public record TableRelation(String name, LinkedHashMap<String, String> columns) {

            static TableRelation.Builder builder() {
                return new TableRelation.Builder();
            }

            static class Builder {
                String name;
                LinkedHashMap<String, String> columns = new LinkedHashMap<>(1);

                public TableRelation.Builder name(String name) {
                    this.name = name;
                    return this;
                }

                public TableRelation.Builder columns(LinkedHashMap<String, String> columns) {
                    this.columns = columns;
                    return this;
                }

                public TableRelation.Builder column(String from, String to) {
                    this.columns.put(from, to);
                    return this;
                }

                TableRelation build() {
                    return new TableRelation(name, columns);
                }
            }
        }
    }
}
